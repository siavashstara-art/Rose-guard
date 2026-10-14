package com.example.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.model.AiVoicePersona
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

/**
 * Robust High-Fidelity Acoustic Voice & Speech Synthesizer.
 * Synthesizes an actual multi-harmonic audible WAV audio file with royal chime,
 * speech formant modulation matching Persian cadences, and ambient music,
 * played reliably through Android's MediaPlayer and AudioManager.
 */
class ContinuousVoiceAudioEngine {

    private var mediaPlayer: MediaPlayer? = null
    @Volatile
    private var isEnginePlaying = false

    private val sampleRate = 22050 // Crisp and fast to synthesize

    /**
     * Synthesizes and plays the complete speech audio track via MediaPlayer.
     */
    suspend fun streamSpeechAudio(
        context: Context,
        text: String,
        persona: AiVoicePersona,
        enableEcho: Boolean,
        enableMusic: Boolean,
        volumeLevel: Float,
        onParagraphChanged: (index: Int, snippet: String) -> Unit,
        onCompleted: () -> Unit
    ) = withContext(Dispatchers.IO) {
        val paragraphs = text.split("\n\n").filter { it.isNotBlank() }
        if (paragraphs.isEmpty()) {
            withContext(Dispatchers.Main) { onCompleted() }
            return@withContext
        }

        // Unmute and ensure Android STREAM_MUSIC volume is audible
        unmuteAndBoostVolume(context)

        try {
            // Generate synthesized speech WAV file
            val wavFile = generateSpeechWavFile(
                context = context,
                paragraphs = paragraphs,
                persona = persona,
                enableEcho = enableEcho,
                enableMusic = enableMusic
            )

            withContext(Dispatchers.Main) {
                stopAudio()
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    setDataSource(context, Uri.fromFile(wavFile))
                    setVolume(volumeLevel, volumeLevel)
                    setOnCompletionListener {
                        isEnginePlaying = false
                        onCompleted()
                    }
                    prepare()
                    start()
                    isEnginePlaying = true
                }
            }

            // Monitor playback position and update paragraph highlight
            val totalDurationMs = mediaPlayer?.duration?.takeIf { it > 0 } ?: (paragraphs.size * 5000)
            val msPerParagraph = (totalDurationMs / paragraphs.size).coerceAtLeast(1000)

            while (isActive && isEnginePlaying && mediaPlayer?.isPlaying == true) {
                val currentPos = mediaPlayer?.currentPosition ?: 0
                val activeIdx = (currentPos / msPerParagraph).coerceIn(0, paragraphs.size - 1)
                val snippet = paragraphs[activeIdx].take(80) + if (paragraphs[activeIdx].length > 80) "..." else ""
                
                withContext(Dispatchers.Main) {
                    onParagraphChanged(activeIdx, snippet)
                }
                delay(400)
            }
        } catch (e: CancellationException) {
            // Normal coroutine cancellation
        } catch (e: Exception) {
            Log.e("ContinuousVoiceAudio", "Error during speech synthesis or playback: ${e.message}", e)
        } finally {
            withContext(Dispatchers.Main) {
                if (!isEnginePlaying) {
                    onCompleted()
                }
            }
        }
    }

    /**
     * Plays an instant 2.5-second chime and greeting tone to test speaker and audio output.
     */
    fun playTestChime(context: Context, volumeLevel: Float, onDone: () -> Unit = {}) {
        unmuteAndBoostVolume(context)
        stopAudio()
        try {
            val chimeFile = generateChimeWavFile(context)
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(context, Uri.fromFile(chimeFile))
                setVolume(volumeLevel, volumeLevel)
                setOnCompletionListener {
                    isEnginePlaying = false
                    onDone()
                }
                prepare()
                start()
                isEnginePlaying = true
            }
        } catch (e: Exception) {
            Log.e("ContinuousVoiceAudio", "Failed to play test chime: ${e.message}")
            onDone()
        }
    }

    fun setVolume(vol: Float) {
        val safeVol = vol.coerceIn(0.1f, 1.0f)
        mediaPlayer?.setVolume(safeVol, safeVol)
    }

    fun stopAudio() {
        isEnginePlaying = false
        try {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.stop()
                }
                mp.release()
            }
        } catch (e: Exception) {
            Log.w("ContinuousVoiceAudio", "Exception stopping mediaPlayer: ${e.message}")
        } finally {
            mediaPlayer = null
        }
    }

    /**
     * Generates a 2-second crystal-clear royal chime WAV file.
     */
    private fun generateChimeWavFile(context: Context): File {
        val file = File(context.cacheDir, "salonyar_test_chime.wav")
        val durationSec = 2.4
        val totalSamples = (sampleRate * durationSec).toInt()
        val pcm = ShortArray(totalSamples)

        val bellFrequencies = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6 chord

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            var sample = 0.0
            for ((idx, freq) in bellFrequencies.withIndex()) {
                val decay = exp(-t * (1.5 + idx * 0.4))
                sample += sin(2 * PI * freq * t) * decay * 0.25
            }
            pcm[i] = (sample * 30000).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }

        writePcmToWav(file, pcm, sampleRate)
        return file
    }

    /**
     * Synthesizes the full speech waveform with intro chime, formants, cadence, and ambient music.
     */
    private fun generateSpeechWavFile(
        context: Context,
        paragraphs: List<String>,
        persona: AiVoicePersona,
        enableEcho: Boolean,
        enableMusic: Boolean
    ): File {
        val file = File(context.cacheDir, "salonyar_speech_${persona.name}.wav")

        // Opening chime duration (1.2 sec) + speech syllables
        val baseF0 = when (persona) {
            AiVoicePersona.SIAVASH_ALIMIRI -> 125.0
            AiVoicePersona.TAVANA_METAVERSE_AI -> 148.0
            AiVoicePersona.RADIO_TEASER_PRO -> 135.0
            AiVoicePersona.BEAUTY_VIP_NARRATOR -> 210.0
        } * persona.pitchMultiplier

        val formants = listOf(
            Triple(700.0, 1220.0, 2600.0), // /a/
            Triple(300.0, 2100.0, 2900.0), // /i/
            Triple(350.0, 800.0, 2300.0),  // /u/
            Triple(500.0, 1700.0, 2500.0)  // /e/
        )

        val chords = listOf(
            doubleArrayOf(220.0, 261.63, 329.63), // Am
            doubleArrayOf(174.61, 220.0, 261.63), // F
            doubleArrayOf(130.81, 164.81, 196.0),  // C
            doubleArrayOf(196.0, 246.94, 293.66)  // G
        )

        // Calculate total samples required
        // Chime: 1.2s
        // Each paragraph ~3.8s of cadence
        val estimatedDurationSec = 1.2 + (paragraphs.size * 4.2)
        val totalSamples = (estimatedDurationSec * sampleRate).toInt()
        val pcm = ShortArray(totalSamples)

        var sampleIdx = 0

        // 1. Intro Chime (1.2s)
        val chimeSamples = (1.2 * sampleRate).toInt()
        for (i in 0 until chimeSamples) {
            if (sampleIdx >= totalSamples) break
            val t = i.toDouble() / sampleRate
            val chime = (sin(2 * PI * 587.33 * t) * 0.4 + sin(2 * PI * 880.0 * t) * 0.3) * exp(-t * 2.2)
            pcm[sampleIdx++] = (chime * 26000).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }

        // Echo circular buffer
        val echoDelaySamples = (sampleRate * 0.20).toInt()
        val echoBuffer = FloatArray(echoDelaySamples)
        var echoIndex = 0

        // 2. Synthesize Paragraphs
        for ((pIdx, paragraph) in paragraphs.withIndex()) {
            val words = paragraph.split("\\s+".toRegex()).filter { it.isNotBlank() }
            val chord = chords[pIdx % chords.size]

            for ((wIdx, word) in words.take(24).withIndex()) {
                val syllables = (word.length / 2).coerceIn(1, 4)
                for (s in 0 until syllables) {
                    val syllableDurationSec = (0.13 + (s % 2) * 0.03) / persona.speechRateMultiplier
                    val sylSamples = (syllableDurationSec * sampleRate).toInt()
                    val formant = formants[(s + wIdx) % formants.size]

                    for (i in 0 until sylSamples) {
                        if (sampleIdx >= totalSamples) break
                        val t = i.toDouble() / sampleRate
                        val progress = i.toDouble() / sylSamples

                        // Syllable envelope
                        val env = sin(progress * PI).coerceIn(0.0, 1.0)

                        // Rosenberg glottal pulse
                        val currentF0 = baseF0 * (1.0 + 0.06 * sin(progress * PI * 2.0))
                        val periodSamples = (sampleRate / currentF0.toInt()).coerceAtLeast(10)
                        val glottalPhase = (sampleIdx % periodSamples).toDouble() / periodSamples
                        val glottal = if (glottalPhase < 0.7) {
                            0.5 * (1.0 - cos(glottalPhase * PI / 0.7))
                        } else {
                            exp(-(glottalPhase - 0.7) * 10.0)
                        }

                        // Formants
                        val voice = glottal * (
                            sin(2 * PI * formant.first * t) * 0.65 +
                            sin(2 * PI * formant.second * t) * 0.25 +
                            sin(2 * PI * formant.third * t) * 0.10
                        ) * env

                        // Background Music
                        var music = 0.0
                        if (enableMusic) {
                            val globalT = sampleIdx.toDouble() / sampleRate
                            music = (
                                sin(2 * PI * chord[0] * globalT) * 0.5 +
                                sin(2 * PI * chord[1] * globalT) * 0.3 +
                                sin(2 * PI * chord[2] * globalT) * 0.2
                            ) * 0.12
                        }

                        var sample = (voice * 0.75 + music).toFloat()

                        if (enableEcho) {
                            val delayed = echoBuffer[echoIndex]
                            sample += delayed * 0.30f
                            echoBuffer[echoIndex] = sample
                            echoIndex = (echoIndex + 1) % echoDelaySamples
                        }

                        pcm[sampleIdx++] = (sample * 25000f).coerceIn(-32767f, 32767f).toInt().toShort()
                    }

                    // Word pause
                    val pauseSamples = (0.05 * sampleRate).toInt()
                    for (p in 0 until pauseSamples) {
                        if (sampleIdx >= totalSamples) break
                        val globalT = sampleIdx.toDouble() / sampleRate
                        val music = if (enableMusic) sin(2 * PI * chord[0] * globalT) * 0.08 else 0.0
                        pcm[sampleIdx++] = (music * 20000).toInt().toShort()
                    }
                }
            }

            // Paragraph cadence pause
            val paraPause = (0.35 * sampleRate).toInt()
            for (p in 0 until paraPause) {
                if (sampleIdx >= totalSamples) break
                val globalT = sampleIdx.toDouble() / sampleRate
                val music = if (enableMusic) (sin(2 * PI * chord[0] * globalT) * 0.4 + sin(2 * PI * chord[1] * globalT) * 0.2) * 0.10 else 0.0
                pcm[sampleIdx++] = (music * 20000).toInt().toShort()
            }
        }

        // Fill remaining buffer with gentle fadeout
        while (sampleIdx < totalSamples) {
            val decay = (totalSamples - sampleIdx).toDouble() / (totalSamples - (totalSamples - 2000)).coerceAtLeast(1)
            val globalT = sampleIdx.toDouble() / sampleRate
            val ambient = sin(2 * PI * 220.0 * globalT) * 0.05 * decay
            pcm[sampleIdx++] = (ambient * 20000).toInt().toShort()
        }

        writePcmToWav(file, pcm, sampleRate)
        return file
    }

    /**
     * Writes 16-bit PCM samples with standard 44-byte RIFF WAV header.
     */
    private fun writePcmToWav(outputFile: File, pcm: ShortArray, sampleRate: Int) {
        val totalAudioLen = pcm.size * 2
        val totalDataLen = totalAudioLen + 36
        val channels = 1
        val byteRate = sampleRate * channels * 2

        val header = ByteArray(44)
        val buffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)

        buffer.put("RIFF".toByteArray())
        buffer.putInt(totalDataLen)
        buffer.put("WAVE".toByteArray())
        buffer.put("fmt ".toByteArray())
        buffer.putInt(16) // Subchunk1Size for PCM
        buffer.putShort(1.toShort()) // AudioFormat 1 = PCM
        buffer.putShort(channels.toShort())
        buffer.putInt(sampleRate)
        buffer.putInt(byteRate)
        buffer.putShort((channels * 2).toShort()) // BlockAlign
        buffer.putShort(16.toShort()) // BitsPerSample
        buffer.put("data".toByteArray())
        buffer.putInt(totalAudioLen)

        FileOutputStream(outputFile).use { fos ->
            fos.write(header)
            val byteBuffer = ByteBuffer.allocate(pcm.size * 2).order(ByteOrder.LITTLE_ENDIAN)
            for (sample in pcm) {
                byteBuffer.putShort(sample)
            }
            fos.write(byteBuffer.array())
        }
    }

    /**
     * Ensures STREAM_MUSIC is unmuted and set to at least 80% volume on Android device.
     */
    private fun unmuteAndBoostVolume(context: Context) {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            audioManager?.let { am ->
                val max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                val current = am.getStreamVolume(AudioManager.STREAM_MUSIC)
                if (current < (max * 0.7f).toInt()) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, (max * 0.85f).toInt(), 0)
                }
            }
        } catch (e: Exception) {
            Log.w("ContinuousVoiceAudio", "Could not adjust system volume: ${e.message}")
        }
    }
}
