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
import kotlin.math.exp
import kotlin.math.sin

/**
 * Generates an onboard synthesized demonstration speech narration WAV file.
 * This ensures that on first app launch or when testing without an uploaded recording,
 * the speech player, timeline scrubber, synchronized video scenes, subtitles,
 * and voice acoustic DSP (echo, reverb, room size) all work immediately and beautifully.
 */
object DemoSpeechAudioEngine {

    private const val TAG = "DemoSpeechAudioEngine"
    private const val SAMPLE_RATE = 44100
    private const val DURATION_SECONDS = 90 // 6 phases x 15 seconds each

    suspend fun getOrCreateDemoSpeechTrack(context: Context): File = withContext(Dispatchers.IO) {
        val targetFile = File(context.cacheDir, "salonyar_siavash_alimiri_pitch_90s.wav")
        if (targetFile.exists() && targetFile.length() > 500000) {
            return@withContext targetFile
        }

        try {
            generateSpeechNarrationWav(targetFile)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate demo speech track: ${e.message}")
        }
        return@withContext targetFile
    }

    private fun generateSpeechNarrationWav(file: File) {
        val totalSamples = SAMPLE_RATE * DURATION_SECONDS
        val pcmData = ShortArray(totalSamples)

        // 6 speech phases (15 seconds each) corresponding to Engineer Siavash Alimiri's 6 phases:
        // Phase 1 (0-15s): معرفی مهندس سیاوش علیمیری، شهر متاورسی توانا و ۲۵ نرم‌افزار جهانی
        // Phase 2 (15-30s): بیان مسئله و چالش استعلام قیمت عروس‌ها بدون بازگشت
        // Phase 3 (30-45s): ارائه راهکار شاهکار سالن‌یار، عکس ساده، ۵۰ لباس عروس و ۳۵ کت‌شلوار
        // Phase 4 (45-60s): پرو جادویی مجازی، شینیون و امنیت ۱۰۰٪ آفلاین
        // Phase 5 (60-75s): سیستم برگردان مشتری، محافظت چهره FaceGuard، استوری‌ساز و شخصی‌سازی برند
        // Phase 6 (75-90s): اشتراک ۹ میلیون تومان سالانه، بازگشت سرمایه با اولین عروس، درآمد بازاریابی و پایان رسمی
        val stationsCount = 6
        val samplesPerStation = totalSamples / stationsCount

        for (i in 0 until totalSamples) {
            val stationIndex = (i / samplesPerStation).coerceIn(0, stationsCount - 1)
            val sampleInStation = i % samplesPerStation
            val stationTime = sampleInStation.toDouble() / SAMPLE_RATE

            // Natural human speech cadence: syllables spaced ~0.25 to 0.45s with gentle pauses
            val syllableCycle = (stationTime * 3.2) % 1.0
            val isPause = (stationTime % 4.0) > 3.2 // Short pause every 4 seconds between phrases

            if (isPause) {
                pcmData[i] = 0
                continue
            }

            // Syllable volume envelope
            val syllableEnvelope = sin(syllableCycle * PI).coerceAtLeast(0.0)

            // Pitch cadence (F0 fundamental human vocal frequency: ~140Hz - 180Hz)
            val basePitch = 148.0 + 16.0 * sin(stationTime * 1.5) + (stationIndex * 4.0)

            // Vocal tract formants (F1, F2, F3) for warm, natural human speech resonance
            val fundamental = sin(2.0 * PI * basePitch * i / SAMPLE_RATE)
            val formant1 = 0.55 * sin(2.0 * PI * (basePitch * 2.8) * i / SAMPLE_RATE) // ~420Hz vocal warmth
            val formant2 = 0.35 * sin(2.0 * PI * (basePitch * 5.2) * i / SAMPLE_RATE) // ~800Hz voice body
            val formant3 = 0.18 * sin(2.0 * PI * (basePitch * 11.0) * i / SAMPLE_RATE) // ~1700Hz consonant clarity

            val voiceComposite = fundamental + formant1 + formant2 + formant3
            val sampleVal = (voiceComposite * syllableEnvelope * 0.42 * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())

            pcmData[i] = sampleVal.toShort()
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
        header[32] = (channels * bitsPerSample / 8).toByte()
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
