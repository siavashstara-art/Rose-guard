package com.example.service

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.model.AiVoicePersona
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class AiVoicePlayState(
    val isPlaying: Boolean = false,
    val isTtsReady: Boolean = false,
    val activePersona: AiVoicePersona = AiVoicePersona.SIAVASH_ALIMIRI,
    val currentParagraphIndex: Int = 0,
    val totalParagraphs: Int = 7,
    val currentSpeakingSnippet: String = "",
    val isEchoReverbEnabled: Boolean = true,
    val isBackgroundMusicEnabled: Boolean = true,
    val statusMessage: String = "آماده پخش سخنرانی مهندس سیاوش علی‌میری",
    val customAudioFileName: String? = null,
    val volumeLevel: Float = 0.95f
)

class AiVoiceNarrationService(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private val scope = CoroutineScope(Dispatchers.Main)
    private var streamingJob: Job? = null

    private val continuousEngine = ContinuousVoiceAudioEngine()
    private var mediaPlayer: MediaPlayer? = null

    private val _state = MutableStateFlow(AiVoicePlayState())
    val state: StateFlow<AiVoicePlayState> = _state.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("AiVoiceService", "Failed to init TextToSpeech: ${e.message}")
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
                    // done
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    // error
                }
            })
            _state.value = _state.value.copy(isTtsReady = true)
        }
    }

    fun setPersona(persona: AiVoicePersona) {
        _state.value = _state.value.copy(
            activePersona = persona,
            statusMessage = "صدای «${persona.titlePersian}» انتخاب شد"
        )
    }

    fun toggleEchoReverb() {
        val next = !_state.value.isEchoReverbEnabled
        _state.value = _state.value.copy(
            isEchoReverbEnabled = next,
            statusMessage = if (next) "افکت اکو و ریورب طنین‌انداز فعال شد" else "اکو و ریورب خاموش شد"
        )
    }

    fun toggleBackgroundMusic() {
        val next = !_state.value.isBackgroundMusicEnabled
        _state.value = _state.value.copy(
            isBackgroundMusicEnabled = next,
            statusMessage = if (next) "موسیقی ملایم و شیک پس‌زمینه فعال شد" else "موسیقی پس‌زمینه خاموش شد"
        )
    }

    fun setVolume(volume: Float) {
        val vol = volume.coerceIn(0.1f, 1.0f)
        _state.value = _state.value.copy(volumeLevel = vol)
        mediaPlayer?.setVolume(vol, vol)
    }

    fun playCustomAudioUri(uri: Uri, displayName: String) {
        stop()
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, uri)
                setVolume(_state.value.volumeLevel, _state.value.volumeLevel)
                setOnPreparedListener { mp ->
                    mp.start()
                    _state.value = _state.value.copy(
                        isPlaying = true,
                        customAudioFileName = displayName,
                        statusMessage = "در حال پخش فایل صوتی اختصاصی شما: $displayName"
                    )
                }
                setOnCompletionListener {
                    _state.value = _state.value.copy(
                        isPlaying = false,
                        statusMessage = "پایان پخش فایل صوتی اختصاصی"
                    )
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                statusMessage = "خطا در پخش فایل صوتی: ${e.message}"
            )
        }
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
            statusMessage = "در حال پخش سخنرانی با ${persona.titlePersian}..."
        )

        // If native TTS is ready, attempt speaking in parallel with fallbacks
        if (isTtsInitialized && tts != null) {
            try {
                val farsiLocale = Locale("fa", "IR")
                val langAvailable = tts?.isLanguageAvailable(farsiLocale)
                if (langAvailable != TextToSpeech.LANG_NOT_SUPPORTED && langAvailable != TextToSpeech.LANG_MISSING_DATA) {
                    tts?.language = farsiLocale
                    tts?.setPitch(persona.pitchMultiplier)
                    tts?.setSpeechRate(persona.speechRateMultiplier)
                    val params = Bundle()
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "salonyar_speech")
                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "salonyar_speech")
                }
            } catch (e: Exception) {
                Log.w("AiVoiceService", "Native TTS call skipped: ${e.message}")
            }
        }

        // Start continuous acoustic audio synthesizer with formants, cadence, echo and ambient music
        streamingJob?.cancel()
        streamingJob = scope.launch {
            continuousEngine.streamSpeechAudio(
                text = text,
                persona = persona,
                enableEcho = _state.value.isEchoReverbEnabled,
                enableMusic = _state.value.isBackgroundMusicEnabled,
                onParagraphChanged = { idx, snippet ->
                    _state.value = _state.value.copy(
                        currentParagraphIndex = idx,
                        currentSpeakingSnippet = snippet,
                        statusMessage = "پخش بخش ${idx + 1} از $count (${persona.characterVibe})"
                    )
                },
                onCompleted = {
                    _state.value = _state.value.copy(
                        isPlaying = false,
                        currentParagraphIndex = count - 1,
                        statusMessage = "پایان سخنرانی مهندس سیاوش علی‌میری"
                    )
                }
            )
        }
    }

    fun stop() {
        tts?.stop()
        streamingJob?.cancel()
        continuousEngine.stopAudio()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            // ignore
        } finally {
            mediaPlayer = null
        }
        _state.value = _state.value.copy(
            isPlaying = false,
            statusMessage = "پخش سخنرانی متوقف شد"
        )
    }

    fun release() {
        stop()
        tts?.shutdown()
    }
}
