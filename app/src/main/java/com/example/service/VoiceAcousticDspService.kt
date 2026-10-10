package com.example.service

import android.content.Context
import android.media.audiofx.BassBoost
import android.media.audiofx.EnvironmentalReverb
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import android.util.Log
import com.example.model.RoomSizePreset
import com.example.model.VoiceAcousticSettings

/**
 * Audio DSP engine that applies subtle acoustic room reverb, gentle echo, and vocal warmth
 * to the speech audio stream using Android's native AudioFX framework.
 */
class VoiceAcousticDspService(private val context: Context) {

    private val tag = "VoiceAcousticDsp"

    private var environmentalReverb: EnvironmentalReverb? = null
    private var presetReverb: PresetReverb? = null
    private var bassBoost: BassBoost? = null
    private var equalizer: Equalizer? = null
    private var currentSessionId: Int = 0

    /**
     * Attaches audio processing effects to a MediaPlayer's audio session.
     */
    fun attachToSession(sessionId: Int, settings: VoiceAcousticSettings) {
        if (sessionId == 0) return
        release()
        currentSessionId = sessionId

        try {
            // 1. Try Environmental Reverb for precise decay, reflection (echo) and room size
            try {
                environmentalReverb = EnvironmentalReverb(0, sessionId).apply {
                    applyEnvironmentalSettings(this, settings)
                    enabled = settings.isEnabled
                }
            } catch (e: Exception) {
                Log.w(tag, "EnvironmentalReverb not available, falling back to PresetReverb: ${e.message}")
                try {
                    presetReverb = PresetReverb(0, sessionId).apply {
                        preset = when (settings.roomSize) {
                            RoomSizePreset.STUDIO_WARM -> PresetReverb.PRESET_SMALLROOM
                            RoomSizePreset.ENGAGING_ROOM -> PresetReverb.PRESET_MEDIUMHALL
                            RoomSizePreset.ACOUSTIC_HALL -> PresetReverb.PRESET_LARGEHALL
                            RoomSizePreset.INTIMATE_BOOTH -> PresetReverb.PRESET_ROOM
                        }
                        enabled = settings.isEnabled
                    }
                } catch (pe: Exception) {
                    Log.w(tag, "PresetReverb also unavailable: ${pe.message}")
                }
            }

            // 2. BassBoost for vocal warmth and radio presence
            try {
                bassBoost = BassBoost(0, sessionId).apply {
                    if (strengthSupported) {
                        val strength = (settings.vocalWarmth.coerceIn(0f, 1f) * 600).toInt().toShort()
                        setStrength(strength)
                    }
                    enabled = settings.isEnabled && settings.vocalWarmth > 0.05f
                }
            } catch (be: Exception) {
                Log.w(tag, "BassBoost not supported: ${be.message}")
            }

            // 3. Equalizer for vocal clarity and crisp speech presence
            try {
                equalizer = Equalizer(0, sessionId).apply {
                    // Slight boost in mid-high for speech clarity (3kHz)
                    val numBands = numberOfBands
                    for (i in 0 until numBands) {
                        val centerFreq = getCenterFreq(i.toShort())
                        // Boost speech intelligibility band between 1500Hz and 4000Hz
                        if (centerFreq in 1500000..4500000) {
                            val boost = (150 * settings.roomSizeAmount).toInt().toShort() // gentle 1.5 dB boost
                            setBandLevel(i.toShort(), boost)
                        }
                    }
                    enabled = settings.isEnabled
                }
            } catch (ee: Exception) {
                Log.w(tag, "Equalizer not supported: ${ee.message}")
            }

        } catch (e: Exception) {
            Log.e(tag, "Error attaching AudioFX: ${e.message}")
        }
    }

    /**
     * Updates settings on active effects without reallocating session.
     */
    fun updateSettings(settings: VoiceAcousticSettings) {
        environmentalReverb?.let { reverb ->
            try {
                applyEnvironmentalSettings(reverb, settings)
                reverb.enabled = settings.isEnabled
            } catch (e: Exception) {
                Log.w(tag, "Error updating environmental reverb: ${e.message}")
            }
        }

        presetReverb?.let { pReverb ->
            try {
                pReverb.preset = when (settings.roomSize) {
                    RoomSizePreset.STUDIO_WARM -> PresetReverb.PRESET_SMALLROOM
                    RoomSizePreset.ENGAGING_ROOM -> PresetReverb.PRESET_MEDIUMHALL
                    RoomSizePreset.ACOUSTIC_HALL -> PresetReverb.PRESET_LARGEHALL
                    RoomSizePreset.INTIMATE_BOOTH -> PresetReverb.PRESET_ROOM
                }
                pReverb.enabled = settings.isEnabled
            } catch (e: Exception) {
                Log.w(tag, "Error updating preset reverb: ${e.message}")
            }
        }

        bassBoost?.let { bb ->
            try {
                if (bb.strengthSupported) {
                    val strength = (settings.vocalWarmth.coerceIn(0f, 1f) * 600).toInt().toShort()
                    bb.setStrength(strength)
                }
                bb.enabled = settings.isEnabled && settings.vocalWarmth > 0.05f
            } catch (e: Exception) {
                Log.w(tag, "Error updating bass boost: ${e.message}")
            }
        }

        equalizer?.let { eq ->
            try {
                eq.enabled = settings.isEnabled
            } catch (e: Exception) {
                Log.w(tag, "Error updating equalizer: ${e.message}")
            }
        }
    }

    private fun applyEnvironmentalSettings(reverb: EnvironmentalReverb, settings: VoiceAcousticSettings) {
        // Base values from RoomSizePreset
        val baseDecay = settings.roomSize.decayMs
        val baseRoomLevel = settings.roomSize.roomLevelMb

        // Scale decay based on roomSizeAmount slider (1000ms - 3000ms)
        val scaledDecay = ((baseDecay * (0.6f + settings.roomSizeAmount * 0.8f)).toInt()).coerceIn(500, 3500)
        reverb.decayTime = scaledDecay

        // Room Level in millibels (-3000 to 0)
        val scaledRoomLevel = (baseRoomLevel + (settings.roomSizeAmount * 400).toInt()).toShort().coerceIn(-2500, 0)
        reverb.roomLevel = scaledRoomLevel

        // Subtle Echo / Reflections: echoLevel slider controls reflections level and delay
        // -2000 mB is very subtle, -600 mB is clearly audible
        val reflectionsMb = (-1800 + (settings.echoLevel.coerceIn(0f, 1f) * 1200).toInt()).toShort()
        reverb.reflectionsLevel = reflectionsMb

        // Reverb delay between direct sound and early reflections (subtle echo timing: 15ms - 45ms)
        val delayMs = (15 + (settings.echoLevel * 30).toInt()).coerceIn(10, 60)
        reverb.reverbDelay = delayMs
    }

    /**
     * Safely releases all AudioFX instances.
     */
    fun release() {
        try {
            environmentalReverb?.release()
            presetReverb?.release()
            bassBoost?.release()
            equalizer?.release()
        } catch (e: Exception) {
            Log.w(tag, "Error releasing AudioFX: ${e.message}")
        } finally {
            environmentalReverb = null
            presetReverb = null
            bassBoost = null
            equalizer = null
            currentSessionId = 0
        }
    }
}
