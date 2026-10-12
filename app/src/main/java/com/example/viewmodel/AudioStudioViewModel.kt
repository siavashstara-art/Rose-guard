package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AnalysisState
import com.example.model.AudioFileInfo
import com.example.model.AudioPlaybackUiState
import com.example.model.BackgroundMusicTrack
import com.example.model.BusinessOffer
import com.example.model.CommercialPartnershipRecord
import com.example.model.CuratedMusicCatalog
import com.example.model.CuratedMusicTrack
import com.example.model.FinancialTransaction
import com.example.model.ImageFitMode
import com.example.model.RoomSizePreset
import com.example.model.SalonLicense
import com.example.model.SalonProfile
import com.example.model.SalonTourCatalog
import com.example.model.SalonTourStation
import com.example.model.SpeechSegment
import com.example.model.SubtitlePosition
import com.example.model.SubtitleStyle
import com.example.model.VideoAspectRatio
import com.example.model.VideoScene
import com.example.model.VisitorAccount
import com.example.model.VisualDisplayType
import com.example.model.VoiceAcousticSettings
import com.example.service.DemoSpeechAudioEngine
import com.example.service.GeminiSpeechService
import com.example.service.LicenseValidationResult
import com.example.service.LicenseValidationService
import com.example.service.RelaxingBackgroundMusicEngine
import com.example.service.SalonProfileRepository
import com.example.service.SpeechSegmentationEngine
import com.example.service.TavanaManagementRepository
import com.example.service.VoiceAcousticDspService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

class AudioStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs: SharedPreferences =
        application.applicationContext.getSharedPreferences("tavana_studio_prefs", Context.MODE_PRIVATE)

    // Step 1: Audio Playback States
    private val _audioFileInfo = MutableStateFlow<AudioFileInfo?>(null)
    val audioFileInfo: StateFlow<AudioFileInfo?> = _audioFileInfo.asStateFlow()

    private val _playbackState = MutableStateFlow(AudioPlaybackUiState())
    val playbackState: StateFlow<AudioPlaybackUiState> = _playbackState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _showRemoveDialog = MutableStateFlow(false)
    val showRemoveDialog: StateFlow<Boolean> = _showRemoveDialog.asStateFlow()

    // Step 2: Speech Analysis & Transcription States
    private val _speechSegments = MutableStateFlow<List<SpeechSegment>>(emptyList())
    val speechSegments: StateFlow<List<SpeechSegment>> = _speechSegments.asStateFlow()

    private val _businessOffers = MutableStateFlow<List<BusinessOffer>>(emptyList())
    val businessOffers: StateFlow<List<BusinessOffer>> = _businessOffers.asStateFlow()

    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()

    private val _rawTranscriptText = MutableStateFlow(SpeechSegmentationEngine.SAMPLE_SALONYAR_SPEECH)
    val rawTranscriptText: StateFlow<String> = _rawTranscriptText.asStateFlow()

    private val _activeSegmentId = MutableStateFlow<String?>(null)
    val activeSegmentId: StateFlow<String?> = _activeSegmentId.asStateFlow()

    private val _showCloudConsentDialog = MutableStateFlow(false)
    val showCloudConsentDialog: StateFlow<Boolean> = _showCloudConsentDialog.asStateFlow()

    private val _editingSegment = MutableStateFlow<SpeechSegment?>(null)
    val editingSegment: StateFlow<SpeechSegment?> = _editingSegment.asStateFlow()

    // Step 3: Video Scenes & Visual Sync States
    private val _videoScenes = MutableStateFlow<List<VideoScene>>(emptyList())
    val videoScenes: StateFlow<List<VideoScene>> = _videoScenes.asStateFlow()

    private val _activeSceneId = MutableStateFlow<String?>(null)
    val activeSceneId: StateFlow<String?> = _activeSceneId.asStateFlow()

    private val _videoAspectRatio = MutableStateFlow(VideoAspectRatio.PORTRAIT_9_16)
    val videoAspectRatio: StateFlow<VideoAspectRatio> = _videoAspectRatio.asStateFlow()

    private val _imageFitMode = MutableStateFlow(ImageFitMode.FIT)
    val imageFitMode: StateFlow<ImageFitMode> = _imageFitMode.asStateFlow()

    // Step 4: Subtitles Styling States
    private val _subtitleStyle = MutableStateFlow(loadPersistedSubtitleStyle())
    val subtitleStyle: StateFlow<SubtitleStyle> = _subtitleStyle.asStateFlow()

    // Step 4: Audio Volumes & Background Music States
    private val _speechVolume = MutableStateFlow(prefs.getFloat("pref_speech_volume", 1.0f))
    val speechVolume: StateFlow<Float> = _speechVolume.asStateFlow()

    private val _isNoiseFilterActive = MutableStateFlow(prefs.getBoolean("pref_noise_filter", false))
    val isNoiseFilterActive: StateFlow<Boolean> = _isNoiseFilterActive.asStateFlow()

    private val _backgroundMusicTrack = MutableStateFlow(
        BackgroundMusicTrack(
            id = "ambient_piano_01",
            title = "پیانو ملایم امبینت و آرامش‌بخش سالنیار",
            artistOrSource = "Royalty-Free Creative Commons Zero (CC0)",
            licenseNotice = "مجوز آزاد تجاری بدون حق انحصاری",
            uriString = null,
            volume = prefs.getFloat("pref_music_volume", 0.25f),
            isMuted = prefs.getBoolean("pref_music_muted", false),
            isFadeInOutEnabled = true
        )
    )
    val backgroundMusicTrack: StateFlow<BackgroundMusicTrack> = _backgroundMusicTrack.asStateFlow()

    // Voice Acoustic Reverb, Subtle Echo & Calming Underscore Music
    private val voiceAcousticDsp = VoiceAcousticDspService(application.applicationContext)
    private val _voiceAcousticSettings = MutableStateFlow(loadPersistedVoiceAcousticSettings())
    val voiceAcousticSettings: StateFlow<VoiceAcousticSettings> = _voiceAcousticSettings.asStateFlow()

    private val _curatedTracks = MutableStateFlow(CuratedMusicCatalog.TRACKS)
    val curatedTracks: StateFlow<List<CuratedMusicTrack>> = _curatedTracks.asStateFlow()

    private val _selectedCuratedTrackId = MutableStateFlow("ambient_piano_01")
    val selectedCuratedTrackId: StateFlow<String> = _selectedCuratedTrackId.asStateFlow()

    // Customer Attraction Hub & Luxury Salon Visual Tour
    private val _salonTourStations = MutableStateFlow(SalonTourCatalog.STATIONS)
    val salonTourStations: StateFlow<List<SalonTourStation>> = _salonTourStations.asStateFlow()

    private val _selectedTourStationId = MutableStateFlow("tour_reception")
    val selectedTourStationId: StateFlow<String> = _selectedTourStationId.asStateFlow()

    private val _clientAttractionTab = MutableStateFlow(0)
    val clientAttractionTab: StateFlow<Int> = _clientAttractionTab.asStateFlow()

    // Step 3: Salon Customization States
    private val salonRepo = SalonProfileRepository(application.applicationContext)
    private val _salonProfiles = MutableStateFlow<List<SalonProfile>>(emptyList())
    val salonProfiles: StateFlow<List<SalonProfile>> = _salonProfiles.asStateFlow()

    private val _activeSalonProfile = MutableStateFlow(SalonProfileRepository.DEFAULT_PROFILE_1)
    val activeSalonProfile: StateFlow<SalonProfile> = _activeSalonProfile.asStateFlow()

    // Step 3: Licensing States
    private val licenseService = LicenseValidationService(application.applicationContext)
    private val _activeLicense = MutableStateFlow<SalonLicense?>(null)
    val activeLicense: StateFlow<SalonLicense?> = _activeLicense.asStateFlow()

    private val _licenseFeedback = MutableStateFlow<String?>(null)
    val licenseFeedback: StateFlow<String?> = _licenseFeedback.asStateFlow()

    // Step 3: Management Dashboard States
    private val managementRepo = TavanaManagementRepository()
    private val _isManagementUnlocked = MutableStateFlow(false)
    val isManagementUnlocked: StateFlow<Boolean> = _isManagementUnlocked.asStateFlow()

    private val _all200Licenses = MutableStateFlow<List<SalonLicense>>(emptyList())
    val all200Licenses: StateFlow<List<SalonLicense>> = _all200Licenses.asStateFlow()

    private val _visitorAccounts = MutableStateFlow<List<VisitorAccount>>(emptyList())
    val visitorAccounts: StateFlow<List<VisitorAccount>> = _visitorAccounts.asStateFlow()

    private val _partnerships = MutableStateFlow<List<CommercialPartnershipRecord>>(emptyList())
    val partnerships: StateFlow<List<CommercialPartnershipRecord>> = _partnerships.asStateFlow()

    private val _walletTransactions = MutableStateFlow<List<FinancialTransaction>>(emptyList())
    val walletTransactions: StateFlow<List<FinancialTransaction>> = _walletTransactions.asStateFlow()

    private val geminiService = GeminiSpeechService()
    private var mediaPlayer: MediaPlayer? = null
    private var bgMusicPlayer: MediaPlayer? = null
    private var progressPollingJob: Job? = null

    init {
        // Pre-populate speech segments, scenes, and offers with Siavash Alimiri 6-phase strategic speech (90 seconds)
        _rawTranscriptText.value = SalonTourCatalog.SPEECH_36_FULL_TEXT
        processManualTranscript(SalonTourCatalog.SPEECH_36_FULL_TEXT, 90.0)
        _selectedTourStationId.value = "tour_phase1_persona"

        // Load saved salon profiles
        val profiles = salonRepo.getAllProfiles()
        _salonProfiles.value = profiles
        val activeId = salonRepo.getActiveProfileId()
        _activeSalonProfile.value = profiles.firstOrNull { it.id == activeId } ?: profiles.first()

        // Check active license
        _activeLicense.value = licenseService.getCurrentActiveLicense()

        // Preload management datasets
        _all200Licenses.value = licenseService.getAll200Licenses()
        _visitorAccounts.value = managementRepo.getVisitorAccounts()
        _partnerships.value = managementRepo.getCommercialPartnerships()
        _walletTransactions.value = managementRepo.getWalletTransactions()

        // Pre-configure speech 36 presets (Acoustic room reverb & soothing music)
        setVoiceAcousticEnabled(true)
        setRoomSizePreset(RoomSizePreset.ENGAGING_ROOM)
        setEchoLevel(0.20f)
        setRoomSizeAmount(0.35f)
        setVocalWarmth(0.30f)

        // Automatically initialize demonstration speech audio track + background music
        loadDemoSpeechAudioTrack(autoPlay = false)
    }

    // Audio Playback Engine
    fun onAudioSelected(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                var fileName = "فایل صوتی"
                var fileSize = 0L

                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        if (nameIndex != -1) {
                            fileName = cursor.getString(nameIndex) ?: "audio_file"
                        }
                        if (sizeIndex != -1) {
                            fileSize = cursor.getLong(sizeIndex)
                        }
                    }
                }

                val mimeType = context.contentResolver.getType(uri) ?: resolveMimeFromExtension(fileName)
                val formatDescription = formatTypeLabel(fileName, mimeType)

                if (!isSupportedAudioFormat(fileName, mimeType)) {
                    _errorMessage.value = "فرمت این فایل ($formatDescription) پشتیبانی نمی‌شود. لطفاً فایل با فرمت MP3، WAV، M4A یا AAC انتخاب کنید."
                    return@launch
                }

                var durationMs = 0L
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(context, uri)
                    val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    durationMs = durationStr?.toLongOrNull() ?: 0L
                } catch (_: Exception) {
                } finally {
                    try {
                        retriever.release()
                    } catch (_: Exception) {}
                }

                releaseMediaPlayer()

                val newPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(context, uri)
                    prepare()
                }

                // Apply speech volume
                val currentVol = _speechVolume.value
                newPlayer.setVolume(currentVol, currentVol)

                val finalDuration = if (durationMs > 0) durationMs else newPlayer.duration.toLong()

                newPlayer.setOnCompletionListener {
                    _playbackState.update { state ->
                        state.copy(
                            isPlaying = false,
                            currentPositionMs = finalDuration
                        )
                    }
                    bgMusicPlayer?.pause()
                    stopProgressPolling()
                }

                newPlayer.setOnErrorListener { _, what, extra ->
                    _errorMessage.value = "خطا در پردازش صدا ($what, $extra). لطفاً فایل معتبر دیگری انتخاب کنید."
                    _playbackState.update { it.copy(isPlaying = false) }
                    bgMusicPlayer?.pause()
                    stopProgressPolling()
                    true
                }

                mediaPlayer = newPlayer

                val audioInfo = AudioFileInfo(
                    uri = uri,
                    fileName = fileName,
                    mimeType = mimeType,
                    durationMs = finalDuration,
                    fileSizeBytes = fileSize,
                    formatDescription = formatDescription
                )

                _audioFileInfo.value = audioInfo
                _playbackState.value = AudioPlaybackUiState(
                    isPlaying = false,
                    currentPositionMs = 0L,
                    totalDurationMs = finalDuration,
                    isLoaded = true,
                    errorMessage = null
                )

                if (_rawTranscriptText.value.isNotBlank()) {
                    val durationSec = finalDuration / 1000.0
                    processManualTranscript(_rawTranscriptText.value, durationSec)
                }

            } catch (e: Exception) {
                releaseMediaPlayer()
                _audioFileInfo.value = null
                _playbackState.value = AudioPlaybackUiState()
                _errorMessage.value = "امکان خواندن این فایل وجود ندارد یا ساختار آن آسیب دیده است (${e.localizedMessage ?: "خطای نامشخص"})."
            }
        }
    }

    // Playback Controls with Background Music Synchronization
    fun play() {
        if (mediaPlayer == null) {
            // If user taps play before choosing their own recording, immediately load & play demo speech
            loadDemoSpeechAudioTrack(autoPlay = true)
            return
        }
        val player = mediaPlayer ?: return
        try {
            if (!player.isPlaying) {
                val current = _playbackState.value.currentPositionMs
                val total = _playbackState.value.totalDurationMs
                if (total > 0 && current >= total - 150) {
                    player.seekTo(0)
                    bgMusicPlayer?.seekTo(0)
                    _playbackState.update { it.copy(currentPositionMs = 0L) }
                }

                val vol = _speechVolume.value
                player.setVolume(vol, vol)
                player.start()

                // Synchronize background music
                val musicTrack = _backgroundMusicTrack.value
                if (bgMusicPlayer != null && !musicTrack.isMuted) {
                    val mVol = musicTrack.volume
                    bgMusicPlayer?.setVolume(mVol, mVol)
                    bgMusicPlayer?.start()
                }

                _playbackState.update { it.copy(isPlaying = true) }
                startProgressPolling()
            }
        } catch (_: Exception) {
            _errorMessage.value = "خطا در آغاز پخش صدا."
        }
    }

    fun pause() {
        val player = mediaPlayer ?: return
        try {
            if (player.isPlaying) {
                player.pause()
                bgMusicPlayer?.pause()
                _playbackState.update {
                    it.copy(
                        isPlaying = false,
                        currentPositionMs = player.currentPosition.toLong()
                    )
                }
                stopProgressPolling()
            }
        } catch (_: Exception) {
            _errorMessage.value = "خطا در توقف صدا."
        }
    }

    fun togglePlayPause() {
        if (_playbackState.value.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun seekTo(positionMs: Long) {
        val player = mediaPlayer ?: return
        val clampedPosition = positionMs.coerceIn(0L, _playbackState.value.totalDurationMs)
        try {
            player.seekTo(clampedPosition.toInt())

            // Sync music seek
            bgMusicPlayer?.let { bg ->
                if (bg.duration > 0) {
                    val looped = (clampedPosition % bg.duration).toInt()
                    bg.seekTo(looped)
                }
            }

            _playbackState.update { it.copy(currentPositionMs = clampedPosition) }
            updateActiveSync(clampedPosition / 1000.0)
        } catch (_: Exception) {
            _errorMessage.value = "خطا در جابه‌جایی موقعیت پخش."
        }
    }

    fun seekAndPlaySegment(segment: SpeechSegment) {
        val targetMs = (segment.startTimeSec * 1000).toLong()
        seekTo(targetMs)
        play()
    }

    fun seekAndPlayScene(scene: VideoScene) {
        val targetMs = (scene.startTimeSec * 1000).toLong()
        seekTo(targetMs)
        play()
    }

    fun replayFromStart() {
        val player = mediaPlayer ?: return
        try {
            player.seekTo(0)
            bgMusicPlayer?.seekTo(0)
            _playbackState.update { it.copy(currentPositionMs = 0L) }
            updateActiveSync(0.0)
            if (!_playbackState.value.isPlaying) {
                play()
            }
        } catch (_: Exception) {
            _errorMessage.value = "خطا در پخش مجدد."
        }
    }

    fun promptRemoveAudio() {
        if (_audioFileInfo.value != null) {
            _showRemoveDialog.value = true
        }
    }

    fun dismissRemoveDialog() {
        _showRemoveDialog.value = false
    }

    fun confirmRemoveAudio() {
        releaseMediaPlayer()
        _audioFileInfo.value = null
        _playbackState.value = AudioPlaybackUiState()
        _showRemoveDialog.value = false
        _errorMessage.value = null
        _activeSegmentId.value = null
        _activeSceneId.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Step 4: Speech & Background Music Volume and Mixing Controls
    fun setSpeechVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        _speechVolume.value = clamped
        mediaPlayer?.setVolume(clamped, clamped)
        prefs.edit().putFloat("pref_speech_volume", clamped).apply()
    }

    fun toggleNoiseReductionFilter() {
        val next = !_isNoiseFilterActive.value
        _isNoiseFilterActive.value = next
        prefs.edit().putBoolean("pref_noise_filter", next).apply()
    }

    fun setBackgroundMusicVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        _backgroundMusicTrack.update { it.copy(volume = clamped) }
        val actualVol = if (_backgroundMusicTrack.value.isMuted) 0f else clamped
        bgMusicPlayer?.setVolume(actualVol, actualVol)
        prefs.edit().putFloat("pref_music_volume", clamped).apply()
    }

    fun toggleBackgroundMusicMute() {
        val nextMuted = !_backgroundMusicTrack.value.isMuted
        _backgroundMusicTrack.update { it.copy(isMuted = nextMuted) }
        val actualVol = if (nextMuted) 0f else _backgroundMusicTrack.value.volume
        bgMusicPlayer?.setVolume(actualVol, actualVol)
        if (nextMuted) {
            bgMusicPlayer?.pause()
        } else if (_playbackState.value.isPlaying) {
            bgMusicPlayer?.start()
        }
        prefs.edit().putBoolean("pref_music_muted", nextMuted).apply()
    }

    fun toggleMusicFadeInOut() {
        _backgroundMusicTrack.update { it.copy(isFadeInOutEnabled = !it.isFadeInOutEnabled) }
    }

    fun loadCustomBackgroundMusic(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        try {
            var musicName = "موسیقی پس‌زمینه اختصاصی"
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex != -1) {
                    musicName = cursor.getString(nameIndex) ?: musicName
                }
            }

            bgMusicPlayer?.stop()
            bgMusicPlayer?.release()

            val newMusicPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context, uri)
                isLooping = true
                prepare()
            }

            val actualVol = if (_backgroundMusicTrack.value.isMuted) 0f else _backgroundMusicTrack.value.volume
            newMusicPlayer.setVolume(actualVol, actualVol)
            bgMusicPlayer = newMusicPlayer

            _backgroundMusicTrack.update {
                it.copy(
                    title = musicName,
                    artistOrSource = "فایل موسیقی کاربر از حافظه دستگاه",
                    licenseNotice = "فایل انتخاب‌شده توسط کاربر با مسئولیت استفاده تجاری سالن",
                    uriString = uri.toString()
                )
            }

            if (_playbackState.value.isPlaying && !_backgroundMusicTrack.value.isMuted) {
                newMusicPlayer.start()
            }
        } catch (e: Exception) {
            _errorMessage.value = "امکان بارگذاری این فایل موسیقی وجود ندارد: ${e.localizedMessage}"
        }
    }

    fun removeBackgroundMusic() {
        bgMusicPlayer?.stop()
        bgMusicPlayer?.release()
        bgMusicPlayer = null
        _backgroundMusicTrack.update {
            it.copy(
                uriString = null,
                title = "بدون موسیقی پس‌زمینه (فقط صدای اصلی)",
                artistOrSource = "صدای خالص سخنرانی",
                licenseNotice = "صدای اختصاصی کاربر بدون موسیقی همراه",
                isMuted = true
            )
        }
    }

    // Voice Acoustic Reverb & Subtle Echo DSP Methods
    fun setVoiceAcousticEnabled(enabled: Boolean) {
        _voiceAcousticSettings.update { it.copy(isEnabled = enabled) }
        voiceAcousticDsp.updateSettings(_voiceAcousticSettings.value)
        persistVoiceAcousticSettings(_voiceAcousticSettings.value)
    }

    fun setRoomSizePreset(preset: RoomSizePreset) {
        _voiceAcousticSettings.update { it.copy(roomSize = preset) }
        voiceAcousticDsp.updateSettings(_voiceAcousticSettings.value)
        persistVoiceAcousticSettings(_voiceAcousticSettings.value)
    }

    fun setEchoLevel(echo: Float) {
        val clamped = echo.coerceIn(0f, 1f)
        _voiceAcousticSettings.update { it.copy(echoLevel = clamped) }
        voiceAcousticDsp.updateSettings(_voiceAcousticSettings.value)
        persistVoiceAcousticSettings(_voiceAcousticSettings.value)
    }

    fun setRoomSizeAmount(amount: Float) {
        val clamped = amount.coerceIn(0f, 1f)
        _voiceAcousticSettings.update { it.copy(roomSizeAmount = clamped) }
        voiceAcousticDsp.updateSettings(_voiceAcousticSettings.value)
        persistVoiceAcousticSettings(_voiceAcousticSettings.value)
    }

    fun setVocalWarmth(warmth: Float) {
        val clamped = warmth.coerceIn(0f, 1f)
        _voiceAcousticSettings.update { it.copy(vocalWarmth = clamped) }
        voiceAcousticDsp.updateSettings(_voiceAcousticSettings.value)
        persistVoiceAcousticSettings(_voiceAcousticSettings.value)
    }

    fun setAutoDucking(ducking: Boolean) {
        _voiceAcousticSettings.update { it.copy(autoDucking = ducking) }
        persistVoiceAcousticSettings(_voiceAcousticSettings.value)
    }

    fun selectCuratedMusicTrack(track: CuratedMusicTrack) {
        _selectedCuratedTrackId.value = track.id
        viewModelScope.launch {
            initOrSwitchBackgroundMusic(
                trackId = track.id,
                title = track.title,
                artistOrSource = track.sourceOrStyle,
                volume = track.defaultVolume
            )
        }
    }

    private suspend fun initOrSwitchBackgroundMusic(
        trackId: String,
        title: String,
        artistOrSource: String,
        volume: Float
    ) {
        val context = getApplication<Application>().applicationContext
        try {
            val audioFile = RelaxingBackgroundMusicEngine.getOrCreateAmbientTrack(context, trackId)
            bgMusicPlayer?.stop()
            bgMusicPlayer?.release()

            val newPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioFile.absolutePath)
                isLooping = true
                prepare()
            }

            val actualVol = if (_backgroundMusicTrack.value.isMuted) 0f else volume
            newPlayer.setVolume(actualVol, actualVol)
            bgMusicPlayer = newPlayer

            _backgroundMusicTrack.update {
                it.copy(
                    id = trackId,
                    title = title,
                    artistOrSource = artistOrSource,
                    licenseNotice = "مجوز آزاد تجاری CC0 استودیو سالنیار",
                    uriString = audioFile.absolutePath,
                    volume = volume
                )
            }

            if (_playbackState.value.isPlaying && !_backgroundMusicTrack.value.isMuted) {
                newPlayer.start()
            }
        } catch (_: Exception) {
        }
    }

    // Client Attraction Hub & Visual Tour Methods
    fun setClientAttractionTab(tabIndex: Int) {
        _clientAttractionTab.value = tabIndex
    }

    fun selectTourStation(stationId: String) {
        _selectedTourStationId.value = stationId
    }

    fun applyTourStationToActiveScene(stationId: String) {
        val station = SalonTourCatalog.STATIONS.firstOrNull { it.id == stationId } ?: return
        val activeId = _activeSceneId.value ?: _videoScenes.value.firstOrNull()?.sceneId ?: return
        _videoScenes.update { scenes ->
            scenes.map { sc ->
                if (sc.sceneId == activeId) {
                    sc.copy(
                        assetUri = station.drawableResName,
                        visualPromptDescription = station.description,
                        isVisualApproved = true,
                        isReady = true
                    )
                } else sc
            }
        }
    }

    fun applyFullVisualTourToAllScenes() {
        val stations = SalonTourCatalog.STATIONS
        if (stations.isEmpty()) return

        _videoScenes.update { currentScenes ->
            if (currentScenes.isEmpty()) {
                SpeechSegmentationEngine.generateVideoScenes(_speechSegments.value)
            } else {
                currentScenes.mapIndexed { idx, scene ->
                    val station = stations[idx % stations.size]
                    scene.copy(
                        assetUri = station.drawableResName,
                        visualPromptDescription = station.description,
                        isVisualApproved = true,
                        isReady = true
                    )
                }
            }
        }
    }

    fun loadSpeech36WithTourPreset() {
        _rawTranscriptText.value = SalonTourCatalog.SPEECH_36_FULL_TEXT
        processManualTranscript(SalonTourCatalog.SPEECH_36_FULL_TEXT, 90.0)
        applyFullVisualTourToAllScenes()
        selectCuratedMusicTrack(CuratedMusicCatalog.TRACKS.first())
        setVoiceAcousticEnabled(true)
        setRoomSizePreset(RoomSizePreset.ENGAGING_ROOM)
        setEchoLevel(0.20f)
        setRoomSizeAmount(0.35f)
        setVocalWarmth(0.30f)
        loadDemoSpeechAudioTrack(autoPlay = false)
    }

    /**
     * Loads the onboard synthesized demonstration speech narration track (90s).
     * Populates player, timeline, subtitles, and acoustic DSP so everything works immediately out of the box.
     */
    fun loadDemoSpeechAudioTrack(autoPlay: Boolean = false) {
        val context = getApplication<Application>().applicationContext
        viewModelScope.launch {
            try {
                val demoFile = DemoSpeechAudioEngine.getOrCreateDemoSpeechTrack(context)
                val demoDurationMs = 90000L

                releaseMediaPlayer()

                val newPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(demoFile.absolutePath)
                    prepare()
                }

                val vol = _speechVolume.value
                newPlayer.setVolume(vol, vol)

                newPlayer.setOnCompletionListener {
                    _playbackState.update { state ->
                        state.copy(
                            isPlaying = false,
                            currentPositionMs = demoDurationMs
                        )
                    }
                    bgMusicPlayer?.pause()
                    stopProgressPolling()
                }

                mediaPlayer = newPlayer

                val demoInfo = AudioFileInfo(
                    uri = Uri.fromFile(demoFile),
                    fileName = "نطق استراتژیک مهندس سیاوش علیمیری (۵ فاز جذب و فروش سالن‌یار)",
                    mimeType = "audio/wav",
                    durationMs = demoDurationMs,
                    fileSizeBytes = demoFile.length(),
                    formatDescription = "WAV استودیویی"
                )
                _audioFileInfo.value = demoInfo

                _playbackState.value = AudioPlaybackUiState(
                    isPlaying = false,
                    currentPositionMs = 0L,
                    totalDurationMs = demoDurationMs,
                    isLoaded = true
                )

                // Attach Voice DSP (reverb, room size, echo, bass boost)
                try {
                    voiceAcousticDsp.attachToSession(newPlayer.audioSessionId, _voiceAcousticSettings.value)
                } catch (_: Exception) {}

                // Initialize background music track (ambient piano)
                initOrSwitchBackgroundMusic(
                    trackId = _selectedCuratedTrackId.value,
                    title = "پیانو ملایم امبینت و آرامش‌بخش سالنیار",
                    artistOrSource = "Royalty-Free Creative Commons Zero (CC0)",
                    volume = _backgroundMusicTrack.value.volume
                )

                if (autoPlay) {
                    play()
                }
            } catch (e: Exception) {
                _errorMessage.value = "امکان بارگذاری نمونه صوتی دمو وجود ندارد: ${e.localizedMessage}"
            }
        }
    }

    // Step 4: Subtitle Styling & Customization
    fun setSubtitleFontSize(sizeSp: Float) {
        _subtitleStyle.update { it.copy(fontSizeSp = sizeSp) }
        persistSubtitleStyle(_subtitleStyle.value)
    }

    fun setSubtitleTextColor(colorHex: Long) {
        _subtitleStyle.update { it.copy(textColorHex = colorHex) }
        persistSubtitleStyle(_subtitleStyle.value)
    }

    fun setSubtitleBackgroundColor(bgHex: Long) {
        _subtitleStyle.update { it.copy(backgroundColorHex = bgHex) }
        persistSubtitleStyle(_subtitleStyle.value)
    }

    fun setSubtitlePosition(pos: SubtitlePosition) {
        _subtitleStyle.update { it.copy(position = pos) }
        persistSubtitleStyle(_subtitleStyle.value)
    }

    fun toggleSubtitleVisibility() {
        _subtitleStyle.update { it.copy(isVisible = !it.isVisible) }
        persistSubtitleStyle(_subtitleStyle.value)
    }

    fun setSubtitleMaxLines(maxLines: Int) {
        _subtitleStyle.update { it.copy(maxLines = maxLines) }
        persistSubtitleStyle(_subtitleStyle.value)
    }

    fun updateSceneSubtitleAndTiming(
        sceneId: String,
        newSubtitle: String,
        newStartSec: Double,
        newEndSec: Double
    ) {
        _videoScenes.update { list ->
            list.map { scene ->
                if (scene.sceneId == sceneId) {
                    scene.copy(
                        subtitleText = newSubtitle.trim(),
                        startTimeSec = newStartSec,
                        endTimeSec = newEndSec.coerceAtLeast(newStartSec)
                    )
                } else scene
            }
        }

        // Synchronize corresponding speech segment
        _speechSegments.update { list ->
            list.map { seg ->
                val matchingScene = _videoScenes.value.firstOrNull { it.sceneId == sceneId }
                if (matchingScene != null && seg.id == matchingScene.speechSegmentId) {
                    seg.copy(
                        text = newSubtitle.trim(),
                        startTimeSec = newStartSec,
                        endTimeSec = newEndSec.coerceAtLeast(newStartSec)
                    )
                } else seg
            }
        }
    }

    // Step 2: Speech Transcript & Segmentation
    fun updateRawTranscript(newText: String) {
        _rawTranscriptText.value = newText
    }

    fun processManualTranscript(
        text: String = _rawTranscriptText.value,
        customDurationSec: Double? = null
    ) {
        val durationSec = customDurationSec
            ?: _audioFileInfo.value?.let { it.durationMs / 1000.0 }
            ?: 180.0

        _analysisState.value = AnalysisState.Processing("در حال قطعه‌بندی هوشمند متن و زمان‌بندی صحنه‌ها...")
        viewModelScope.launch {
            try {
                val segments = SpeechSegmentationEngine.processTextToSegments(text, durationSec)
                val offers = SpeechSegmentationEngine.extractBusinessOffers(segments)
                val scenes = SpeechSegmentationEngine.generateVideoScenes(segments)

                _speechSegments.value = segments
                _businessOffers.value = offers
                _videoScenes.value = scenes
                _rawTranscriptText.value = text
                _analysisState.value = AnalysisState.Success("تحلیل و زمان‌بندی محلی انجام شد (${segments.size} بخش گفتار و زیرنویس)")

                if (scenes.isNotEmpty()) {
                    _activeSceneId.value = scenes.first().sceneId
                }
            } catch (e: Exception) {
                _analysisState.value = AnalysisState.Error("خطا در پردازش متن: ${e.localizedMessage}")
            }
        }
    }

    fun loadSampleTranscript() {
        _rawTranscriptText.value = SpeechSegmentationEngine.SAMPLE_SALONYAR_SPEECH
        processManualTranscript(SpeechSegmentationEngine.SAMPLE_SALONYAR_SPEECH)
    }

    fun startEditingSegment(segment: SpeechSegment) {
        _editingSegment.value = segment
    }

    fun cancelEditingSegment() {
        _editingSegment.value = null
    }

    fun saveEditedSegment(updated: SpeechSegment) {
        _speechSegments.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
        _videoScenes.update { scenes ->
            scenes.map { scene ->
                if (scene.speechSegmentId == updated.id) {
                    scene.copy(
                        startTimeSec = updated.startTimeSec,
                        endTimeSec = updated.endTimeSec,
                        subtitleText = updated.text
                    )
                } else scene
            }
        }
        _businessOffers.update {
            SpeechSegmentationEngine.extractBusinessOffers(_speechSegments.value)
        }
        _editingSegment.value = null
    }

    fun toggleSegmentConfirmed(segmentId: String) {
        _speechSegments.update { list ->
            list.map {
                if (it.id == segmentId) it.copy(isConfirmed = !it.isConfirmed) else it
            }
        }
    }

    // Step 3: Visual Scenes Management & Aspect Ratio
    fun setAspectRatio(ratio: VideoAspectRatio) {
        _videoAspectRatio.value = ratio
    }

    fun setImageFitMode(mode: ImageFitMode) {
        _imageFitMode.value = mode
    }

    fun updateSceneImage(sceneId: String, uriString: String) {
        _videoScenes.update { list ->
            list.map { scene ->
                if (scene.sceneId == sceneId) {
                    scene.copy(
                        assetUri = uriString,
                        isReady = true,
                        isVisualApproved = true
                    )
                } else scene
            }
        }
    }

    fun removeSceneImage(sceneId: String) {
        _videoScenes.update { list ->
            list.map { scene ->
                if (scene.sceneId == sceneId) {
                    scene.copy(
                        assetUri = null,
                        isReady = false
                    )
                } else scene
            }
        }
    }

    fun approveSceneVisual(sceneId: String, isApproved: Boolean) {
        _videoScenes.update { list ->
            list.map {
                if (it.sceneId == sceneId) it.copy(isVisualApproved = isApproved) else it
            }
        }
    }

    fun addNewScene() {
        val currentScenes = _videoScenes.value
        val newIndex = currentScenes.size + 1
        val lastEnd = currentScenes.lastOrNull()?.endTimeSec ?: 0.0
        val newEnd = lastEnd + 15.0

        val newScene = VideoScene(
            sceneId = "scene_$newIndex",
            speechSegmentId = "seg_custom_$newIndex",
            startTimeSec = lastEnd,
            endTimeSec = newEnd,
            subtitleText = "صحنه جدید سالنیار $newIndex",
            visualPromptDescription = "نمای سالن زیبایی و معرفی خدمات ویژه",
            displayType = VisualDisplayType.IMAGE,
            assetUri = null,
            isVisualApproved = false,
            isReady = false
        )

        _videoScenes.value = currentScenes + newScene
    }

    fun deleteScene(sceneId: String) {
        _videoScenes.update { list ->
            list.filter { it.sceneId != sceneId }
        }
    }

    fun moveSceneUp(sceneId: String) {
        val current = _videoScenes.value.toMutableList()
        val index = current.indexOfFirst { it.sceneId == sceneId }
        if (index > 0) {
            val item = current.removeAt(index)
            current.add(index - 1, item)
            _videoScenes.value = current
        }
    }

    fun moveSceneDown(sceneId: String) {
        val current = _videoScenes.value.toMutableList()
        val index = current.indexOfFirst { it.sceneId == sceneId }
        if (index != -1 && index < current.size - 1) {
            val item = current.removeAt(index)
            current.add(index + 1, item)
            _videoScenes.value = current
        }
    }

    // Salon Customization Methods
    fun switchSalonProfile(profileId: String) {
        val profile = _salonProfiles.value.firstOrNull { it.id == profileId } ?: return
        _activeSalonProfile.value = profile
        salonRepo.setActiveProfileId(profileId)
    }

    fun saveActiveSalonProfile(updated: SalonProfile) {
        _activeSalonProfile.value = updated
        salonRepo.saveProfile(updated)
        _salonProfiles.value = salonRepo.getAllProfiles()
    }

    fun createNewSalonProfile(name: String, manager: String) {
        val newId = "salon_${System.currentTimeMillis() % 10000}"
        val newProfile = SalonProfile(
            id = newId,
            salonName = name.ifBlank { "سالن زیبایی جدید" },
            managerName = manager.ifBlank { "مدیر سالن" },
            address = "نشانی سالن",
            phone = "شماره تماس",
            workingHours = "ساعات پذیرش",
            selectedTemplate = com.example.model.SalonTemplateLayout.TOP_HERO
        )
        salonRepo.saveProfile(newProfile)
        _salonProfiles.value = salonRepo.getAllProfiles()
        _activeSalonProfile.value = newProfile
        salonRepo.setActiveProfileId(newId)
    }

    // Licensing Methods
    fun submitLicenseCode(inputKey: String) {
        val result = licenseService.validateAndActivateKey(inputKey)
        when (result) {
            is LicenseValidationResult.Success -> {
                _activeLicense.value = result.license
                _licenseFeedback.value = "مجوز ${result.license.formattedIndex} با موفقیت فعال شد! دسترسی به خروجی کامل و امکانات ویژه باز شد."
            }
            is LicenseValidationResult.InvalidKey -> {
                _licenseFeedback.value = result.message
            }
            is LicenseValidationResult.Expired -> {
                _activeLicense.value = result.license
                _licenseFeedback.value = result.message
            }
            is LicenseValidationResult.Revoked -> {
                _activeLicense.value = null
                _licenseFeedback.value = result.message
            }
        }
    }

    fun deactivateLicense() {
        licenseService.deactivateLicense()
        _activeLicense.value = null
        _licenseFeedback.value = "مجوز از روی این دستگاه برداشته شد."
    }

    fun clearLicenseFeedback() {
        _licenseFeedback.value = null
    }

    // Management Dashboard Authentication
    fun unlockManagement(pin: String): Boolean {
        if (pin == "2026" || pin == "tavana") {
            _isManagementUnlocked.value = true
            return true
        }
        return false
    }

    fun lockManagement() {
        _isManagementUnlocked.value = false
    }

    // Cloud AI Consent
    fun requestCloudAnalysis() {
        _showCloudConsentDialog.value = true
    }

    fun dismissCloudConsentDialog() {
        _showCloudConsentDialog.value = false
    }

    fun proceedWithCloudAnalysis() {
        _showCloudConsentDialog.value = false
        val durationSec = _audioFileInfo.value?.let { it.durationMs / 1000.0 } ?: 180.0
        val text = _rawTranscriptText.value

        if (text.isBlank()) {
            _analysisState.value = AnalysisState.Error("متن سخنرانی خالی است.")
            return
        }

        if (!geminiService.isApiKeyConfigured()) {
            _analysisState.value = AnalysisState.Error(
                "کلید سرویس Gemini API در متغیرهای برنامه (Secrets) تنظیم نشده است. می‌توانید از پردازش محلی بدون نیاز به کلید بهره ببرید."
            )
            return
        }

        _analysisState.value = AnalysisState.Processing("در حال ارتباط امن با مدل Gemini 3.5 Flash برای تحلیل عمیق سخنرانی...")
        viewModelScope.launch {
            val result = geminiService.analyzePersianSpeechWithGemini(text, durationSec)
            result.onSuccess { (segments, offers, scenes) ->
                _speechSegments.value = segments
                _businessOffers.value = offers
                _videoScenes.value = scenes
                _analysisState.value = AnalysisState.Success("تحلیل هوشمند با Gemini انجام شد (${segments.size} بخش سخنرانی)")
            }.onFailure { error ->
                _analysisState.value = AnalysisState.Error("خطا در تحلیل ابری: ${error.localizedMessage}")
            }
        }
    }

    private fun startProgressPolling() {
        stopProgressPolling()
        progressPollingJob = viewModelScope.launch {
            while (isActive) {
                val player = mediaPlayer
                if (player != null && player.isPlaying) {
                    val pos = player.currentPosition.toLong()
                    _playbackState.update { it.copy(currentPositionMs = pos) }
                    updateActiveSync(pos / 1000.0)
                }
                delay(100)
            }
        }
    }

    private fun updateActiveSync(positionSec: Double) {
        val currentSeg = _speechSegments.value.firstOrNull { seg ->
            positionSec >= seg.startTimeSec && positionSec <= seg.endTimeSec
        }
        _activeSegmentId.value = currentSeg?.id

        val currentScene = _videoScenes.value.firstOrNull { scene ->
            positionSec >= scene.startTimeSec && positionSec <= scene.endTimeSec
        }
        _activeSceneId.value = currentScene?.sceneId ?: _videoScenes.value.firstOrNull()?.sceneId
    }

    private fun stopProgressPolling() {
        progressPollingJob?.cancel()
        progressPollingJob = null
    }

    private fun releaseMediaPlayer() {
        stopProgressPolling()
        try {
            mediaPlayer?.stop()
        } catch (_: Exception) {}
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null

        try {
            bgMusicPlayer?.stop()
        } catch (_: Exception) {}
        try {
            bgMusicPlayer?.release()
        } catch (_: Exception) {}
        bgMusicPlayer = null

        voiceAcousticDsp.release()
    }

    private fun loadPersistedVoiceAcousticSettings(): VoiceAcousticSettings {
        val enabled = prefs.getBoolean("voice_dsp_enabled", true)
        val roomPresetName = prefs.getString("voice_dsp_room_preset", RoomSizePreset.ENGAGING_ROOM.name)
        val roomPreset = try { RoomSizePreset.valueOf(roomPresetName!!) } catch (_: Exception) { RoomSizePreset.ENGAGING_ROOM }
        val echo = prefs.getFloat("voice_dsp_echo", 0.20f)
        val roomAmt = prefs.getFloat("voice_dsp_room_amt", 0.35f)
        val warmth = prefs.getFloat("voice_dsp_warmth", 0.30f)
        val ducking = prefs.getBoolean("voice_dsp_ducking", true)
        return VoiceAcousticSettings(enabled, roomPreset, echo, roomAmt, warmth, ducking)
    }

    private fun persistVoiceAcousticSettings(settings: VoiceAcousticSettings) {
        prefs.edit()
            .putBoolean("voice_dsp_enabled", settings.isEnabled)
            .putString("voice_dsp_room_preset", settings.roomSize.name)
            .putFloat("voice_dsp_echo", settings.echoLevel)
            .putFloat("voice_dsp_room_amt", settings.roomSizeAmount)
            .putFloat("voice_dsp_warmth", settings.vocalWarmth)
            .putBoolean("voice_dsp_ducking", settings.autoDucking)
            .apply()
    }

    private fun loadPersistedSubtitleStyle(): SubtitleStyle {
        val size = prefs.getFloat("sub_font_size", 17f)
        val textHex = prefs.getLong("sub_text_hex", 0xFFFFFFFF)
        val bgHex = prefs.getLong("sub_bg_hex", 0xAA000000)
        val posName = prefs.getString("sub_position", SubtitlePosition.BOTTOM.name)
        val pos = try { SubtitlePosition.valueOf(posName!!) } catch (_: Exception) { SubtitlePosition.BOTTOM }
        val maxLines = prefs.getInt("sub_max_lines", 2)
        val visible = prefs.getBoolean("sub_visible", true)
        return SubtitleStyle(size, textHex, bgHex, pos, maxLines, visible)
    }

    private fun persistSubtitleStyle(style: SubtitleStyle) {
        prefs.edit()
            .putFloat("sub_font_size", style.fontSizeSp)
            .putLong("sub_text_hex", style.textColorHex)
            .putLong("sub_bg_hex", style.backgroundColorHex)
            .putString("sub_position", style.position.name)
            .putInt("sub_max_lines", style.maxLines)
            .putBoolean("sub_visible", style.isVisible)
            .apply()
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }

    companion object {
        fun formatDuration(ms: Long): String {
            if (ms <= 0) return "۰۰:۰۰"
            val totalSeconds = ms / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }

        fun formatSeconds(sec: Double): String {
            if (sec <= 0) return "۰۰:۰۰"
            val totalSeconds = sec.toLong()
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }

        fun formatFileSize(bytes: Long): String {
            if (bytes <= 0) return "نامشخص"
            val kb = bytes / 1024.0
            val mb = kb / 1024.0
            return if (mb >= 1.0) {
                String.format(Locale.US, "%.1f مگابایت", mb)
            } else {
                String.format(Locale.US, "%.0f کیلوبایت", kb)
            }
        }

        private fun resolveMimeFromExtension(fileName: String): String {
            val ext = fileName.substringAfterLast('.', "").lowercase(Locale.ROOT)
            return when (ext) {
                "mp3" -> "audio/mpeg"
                "wav" -> "audio/wav"
                "m4a" -> "audio/mp4"
                "aac" -> "audio/aac"
                "ogg", "oga" -> "audio/ogg"
                "flac" -> "audio/flac"
                "opus" -> "audio/opus"
                else -> "audio/*"
            }
        }

        private fun formatTypeLabel(fileName: String, mimeType: String): String {
            val ext = fileName.substringAfterLast('.', "").uppercase(Locale.ROOT)
            if (ext.isNotEmpty() && ext.length <= 5) {
                return ext
            }
            return when {
                mimeType.contains("mpeg", ignoreCase = true) -> "MP3"
                mimeType.contains("wav", ignoreCase = true) -> "WAV"
                mimeType.contains("mp4", ignoreCase = true) || mimeType.contains("m4a", ignoreCase = true) -> "M4A"
                mimeType.contains("aac", ignoreCase = true) -> "AAC"
                mimeType.contains("ogg", ignoreCase = true) -> "OGG"
                else -> mimeType
            }
        }

        private fun isSupportedAudioFormat(fileName: String, mimeType: String): Boolean {
            val ext = fileName.substringAfterLast('.', "").lowercase(Locale.ROOT)
            val allowedExtensions = listOf("mp3", "wav", "m4a", "aac", "ogg", "oga", "flac", "opus", "mp4")
            if (allowedExtensions.contains(ext)) return true
            if (mimeType.startsWith("audio/")) return true
            return false
        }
    }
}
