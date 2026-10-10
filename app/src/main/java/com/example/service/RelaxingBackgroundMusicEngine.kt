package com.example.service

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.sin

/**
 * Generates an ultra-soothing, royalty-free ambient music WAV track for salon video underscoring.
 * Synthesizes peaceful pentatonic piano & pad harmony (Fmaj7, Cmaj9, Dm7) with silky envelopes.
 */
object RelaxingBackgroundMusicEngine {

    private const val TAG = "RelaxingMusicEngine"
    private const val SAMPLE_RATE = 44100
    private const val DURATION_SECONDS = 16

    suspend fun getOrCreateAmbientTrack(context: Context, trackId: String): File = withContext(Dispatchers.IO) {
        val targetFile = File(context.cacheDir, "ambient_relaxing_${trackId}.wav")
        if (targetFile.exists() && targetFile.length() > 100000) {
            return@withContext targetFile
        }

        try {
            generateSoothingWav(targetFile, trackId)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate synthesized ambient audio: ${e.message}")
        }
        return@withContext targetFile
    }

    private fun generateSoothingWav(file: File, trackId: String) {
        val totalSamples = SAMPLE_RATE * DURATION_SECONDS
        val pcmData = ShortArray(totalSamples)

        // Musical frequencies (gentle peaceful chords: F3, A3, C4, E4, G4, D4)
        val chordProgressions = when (trackId) {
            "strings_dreamy_02" -> listOf(
                doubleArrayOf(220.0, 277.18, 329.63, 440.0), // A maj
                doubleArrayOf(196.0, 246.94, 293.66, 392.0), // G maj
                doubleArrayOf(174.61, 220.0, 261.63, 349.23), // F maj
                doubleArrayOf(164.81, 207.65, 246.94, 329.63)  // E min
            )
            "spa_meditation_03" -> listOf(
                doubleArrayOf(174.61, 261.63, 349.23, 528.0), // 528Hz Love/Spa tone
                doubleArrayOf(174.61, 261.63, 349.23, 432.0), // 432Hz Calm tone
                doubleArrayOf(196.0, 293.66, 392.0, 528.0),
                doubleArrayOf(164.81, 246.94, 329.63, 432.0)
            )
            "acoustic_warmth_04" -> listOf(
                doubleArrayOf(130.81, 196.0, 261.63, 329.63), // C maj
                doubleArrayOf(110.0, 164.81, 220.0, 261.63),  // A min
                doubleArrayOf(87.31, 130.81, 174.61, 220.0),   // F maj
                doubleArrayOf(98.0, 146.83, 196.0, 246.94)     // G maj
            )
            else -> listOf(
                // ambient_piano_01 (default)
                doubleArrayOf(174.61, 220.00, 261.63, 329.63), // Fmaj7
                doubleArrayOf(130.81, 164.81, 196.00, 246.94), // Cmaj7
                doubleArrayOf(146.83, 174.61, 220.00, 261.63), // Dm7
                doubleArrayOf(164.81, 196.00, 246.94, 293.66)  // Em7
            )
        }

        val segmentSamples = totalSamples / chordProgressions.size

        for (i in 0 until totalSamples) {
            val chordIndex = (i / segmentSamples).coerceIn(0, chordProgressions.size - 1)
            val chord = chordProgressions[chordIndex]
            val localSample = i % segmentSamples
            val localTime = localSample.toDouble() / SAMPLE_RATE
            val chordDurationSec = segmentSamples.toDouble() / SAMPLE_RATE

            // Smooth envelope: 1.2s attack, sustained body, 1.2s release
            val attackTime = 1.2
            val releaseTime = 1.2
            val envelope = when {
                localTime < attackTime -> (localTime / attackTime)
                localTime > (chordDurationSec - releaseTime) -> ((chordDurationSec - localTime) / releaseTime).coerceAtLeast(0.0)
                else -> 1.0
            }

            var sumSample = 0.0
            for ((noteIndex, freq) in chord.withIndex()) {
                // Fundamental tone + subtle warm overtone (soft octave)
                val fundamental = sin(2.0 * PI * freq * i / SAMPLE_RATE)
                val overtone = 0.25 * sin(2.0 * PI * (freq * 2.0) * i / SAMPLE_RATE)
                val noteWeight = 1.0 / (noteIndex + 1.2)
                sumSample += (fundamental + overtone) * noteWeight
            }

            // Normalization and gentle master gain
            val sampleValue = (sumSample * envelope * 0.28 * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
            pcmData[i] = sampleValue.toShort()
        }

        FileOutputStream(file).use { fos ->
            writeWavHeader(fos, totalSamples * 2, SAMPLE_RATE, 1, 16)
            val buffer = ByteBuffer.allocate(pcmData.size * 2).order(ByteOrder.LITTLE_ENDIAN)
            for (s in pcmData) {
                buffer.putShort(s)
            }
            fos.write(buffer.array())
        }
    }

    private fun writeWavHeader(
        out: FileOutputStream,
        pcmDataLength: Int,
        sampleRate: Int,
        channels: Int,
        bitsPerSample: Int
    ) {
        val totalDataLen = pcmDataLength + 36
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val header = ByteArray(44)

        header[0] = 'R'.code.toByte() // RIFF
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()
        header[8] = 'W'.code.toByte() // WAVE
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte() // 'fmt ' chunk
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16 // 16 for PCM
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // Audio format 1 = PCM
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = ((sampleRate shr 8) and 0xff).toByte()
        header[26] = ((sampleRate shr 16) and 0xff).toByte()
        header[27] = ((sampleRate shr 24) and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()
        header[32] = (channels * bitsPerSample / 8).toByte() // block align
        header[33] = 0
        header[34] = bitsPerSample.toByte()
        header[35] = 0
        header[36] = 'd'.code.toByte() // 'data' chunk
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (pcmDataLength and 0xff).toByte()
        header[41] = ((pcmDataLength shr 8) and 0xff).toByte()
        header[42] = ((pcmDataLength shr 16) and 0xff).toByte()
        header[43] = ((pcmDataLength shr 24) and 0xff).toByte()

        out.write(header, 0, 44)
    }
}
