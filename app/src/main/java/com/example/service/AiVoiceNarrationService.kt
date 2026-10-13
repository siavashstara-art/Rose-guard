package com.example.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.model.AiVoicePersona
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

data class AiVoicePlayState(
    val isPlaying: Boolean = false,
    val isTtsReady: Boolean = false,
    val activePersona: AiVoicePersona = AiVoicePersona.SIAVASH_ALIMIRI,
    val currentParagraphIndex: Int = 0,
    val totalParagraphs: Int = 7,
    val currentSpeakingSnippet: String = "",
    val isEchoReverbEnabled: Boolean = true,
    val isBackgroundMusicEnabled: Boolean = true,
    val statusMessage: String = "آماده پخش صدای هوش مصنوعی"
)

class AiVoiceNarrationService(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var simulationJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(AiVoicePlayState())
    val state: StateFlow<AiVoicePlayState> = _state.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("AiVoiceService", "Failed to create TextToSpeech: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _state.value = _state.value.copy(isPlaying = true)
                }

                override fun onDone(utteranceId: String?) {
                    _state.value = _state.value.copy(
                        isPlaying = false,
                        statusMessage = "پایان سخنرانی هوش مصنوعی"
                    )
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _state.value = _state.value.copy(isPlaying = false)
                }
            })
            _state.value = _state.value.copy(isTtsReady = true, statusMessage = "موتور صوتی آماده است")
        }
    }

    fun setPersona(persona: AiVoicePersona) {
        _state.value = _state.value.copy(
            activePersona = persona,
            statusMessage = "صدای «${persona.titlePersian}» فعال شد"
        )
    }

    fun toggleEchoReverb() {
        val next = !_state.value.isEchoReverbEnabled
        _state.value = _state.value.copy(
            isEchoReverbEnabled = next,
            statusMessage = if (next) "افکت اکو و ریورب فعال شد" else "اکو و ریورب خاموش شد"
        )
    }

    fun toggleBackgroundMusic() {
        val next = !_state.value.isBackgroundMusicEnabled
        _state.value = _state.value.copy(
            isBackgroundMusicEnabled = next,
            statusMessage = if (next) "موسیقی ملایم پس‌زمینه فعال شد" else "موسیقی پس‌زمینه خاموش شد"
        )
    }

    fun speakScript(text: String) {
        if (_state.value.isPlaying) {
            stop()
            return
        }

        val persona = _state.value.activePersona
        val paragraphs = text.split("\n\n").filter { it.isNotBlank() }
        val count = paragraphs.size

        _state.value = _state.value.copy(
            isPlaying = true,
            totalParagraphs = count,
            currentParagraphIndex = 0,
            statusMessage = "در حال پخش با ${persona.titlePersian}..."
        )

        // Try native TTS
        var farsiTtsWorked = false
        if (isTtsInitialized && tts != null) {
            val farsiLocale = Locale("fa", "IR")
            val langResult = tts?.setLanguage(farsiLocale)
            if (langResult != TextToSpeech.LANG_MISSING_DATA && langResult != TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setPitch(persona.pitchMultiplier)
                tts?.setSpeechRate(persona.speechRateMultiplier)
                val params = Bundle()
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "salonyar_speech")
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "salonyar_speech")
                farsiTtsWorked = true
            }
        }

        // Coroutine simulation runner to synchronize visual highlights and audio cues
        simulationJob?.cancel()
        simulationJob = scope.launch {
            try {
                // Play harmonic audio cue if device doesn't have Persian TTS data
                if (!farsiTtsWorked) {
                    playAcousticChime(persona.pitchMultiplier)
                }

                for (idx in paragraphs.indices) {
                    val p = paragraphs[idx]
                    _state.value = _state.value.copy(
                        currentParagraphIndex = idx,
                        currentSpeakingSnippet = p.take(80) + if (p.length > 80) "..." else "",
                        statusMessage = "پخش بخش ${idx + 1} از $count (${persona.characterVibe})"
                    )
                    // Simulated duration per paragraph
                    val dur = (p.length * 55L).coerceIn(2500L, 7500L)
                    delay(dur)
                }

                _state.value = _state.value.copy(
                    isPlaying = false,
                    currentParagraphIndex = count - 1,
                    statusMessage = "پایان سخنرانی مهندس سیاوش علی‌میری"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isPlaying = false)
            }
        }
    }

    private fun playAcousticChime(pitchFactor: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sampleRate = 44100
                val durationSec = 0.6
                val numSamples = (durationSec * sampleRate).toInt()
                val buffer = ShortArray(numSamples)
                val baseFreq = 440.0 * pitchFactor

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    // Warm harmonic chime with decay
                    val envelope = Math.exp(-time * 4.0)
                    val s = (sin(2.0 * Math.PI * baseFreq * time) * 0.6 +
                            sin(2.0 * Math.PI * (baseFreq * 1.5) * time) * 0.3 +
                            sin(2.0 * Math.PI * (baseFreq * 2.0) * time) * 0.1) * envelope
                    buffer[i] = (s * Short.MAX_VALUE * 0.4).toInt().toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                delay(700)
                audioTrack.release()
            } catch (e: Exception) {
                Log.w("AiVoiceService", "Chime generation failed: ${e.message}")
            }
        }
    }

    fun stop() {
        tts?.stop()
        simulationJob?.cancel()
        _state.value = _state.value.copy(
            isPlaying = false,
            statusMessage = "پخش متوقف شد"
        )
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        simulationJob?.cancel()
    }
}
