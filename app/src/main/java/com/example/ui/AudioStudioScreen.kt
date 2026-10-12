package com.example.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.ui.components.AudioMixerAndMusicSection
import com.example.ui.components.AudioPlayerCard
import com.example.ui.components.BusinessOffersSection
import com.example.ui.components.LicenseStatusCard
import com.example.ui.components.ManagementDashboardSection
import com.example.ui.components.PipelineRoadmapSection
import com.example.ui.components.SalonClientAttractionHub
import com.example.ui.components.SalonCustomizationSection
import com.example.ui.components.SceneEditorSection
import com.example.ui.components.SegmentEditDialog
import com.example.ui.components.SpeechTranscriptSection
import com.example.ui.components.SubtitleStyleEditorSection
import com.example.ui.components.VideoCanvasPreview
import com.example.ui.components.VoiceAcousticDspSection
import com.example.ui.theme.IvoryBackground
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.OnPeachContainer
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.StateError
import com.example.ui.theme.StateErrorContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmPeachLight
import com.example.viewmodel.AudioStudioViewModel

@Composable
fun AudioStudioScreen(
    viewModel: AudioStudioViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Step 1 States
    val audioInfo by viewModel.audioFileInfo.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val showRemoveDialog by viewModel.showRemoveDialog.collectAsState()

    // Step 2 States
    val speechSegments by viewModel.speechSegments.collectAsState()
    val businessOffers by viewModel.businessOffers.collectAsState()
    val analysisState by viewModel.analysisState.collectAsState()
    val rawTranscript by viewModel.rawTranscriptText.collectAsState()
    val activeSegmentId by viewModel.activeSegmentId.collectAsState()
    val showCloudConsentDialog by viewModel.showCloudConsentDialog.collectAsState()
    val editingSegment by viewModel.editingSegment.collectAsState()

    // Step 3 States
    val videoScenes by viewModel.videoScenes.collectAsState()
    val activeSceneId by viewModel.activeSceneId.collectAsState()
    val videoAspectRatio by viewModel.videoAspectRatio.collectAsState()
    val imageFitMode by viewModel.imageFitMode.collectAsState()

    val salonProfiles by viewModel.salonProfiles.collectAsState()
    val activeSalonProfile by viewModel.activeSalonProfile.collectAsState()

    val activeLicense by viewModel.activeLicense.collectAsState()
    val licenseFeedback by viewModel.licenseFeedback.collectAsState()

    val isManagementUnlocked by viewModel.isManagementUnlocked.collectAsState()
    val all200Licenses by viewModel.all200Licenses.collectAsState()
    val visitorAccounts by viewModel.visitorAccounts.collectAsState()
    val partnerships by viewModel.partnerships.collectAsState()
    val walletTransactions by viewModel.walletTransactions.collectAsState()

    // Step 4 States: Subtitles & Music Mixer
    val subtitleStyle by viewModel.subtitleStyle.collectAsState()
    val speechVolume by viewModel.speechVolume.collectAsState()
    val isNoiseFilterActive by viewModel.isNoiseFilterActive.collectAsState()
    val backgroundMusicTrack by viewModel.backgroundMusicTrack.collectAsState()

    // Voice Acoustic Reverb, Subtle Echo & Underscore Music
    val voiceAcousticSettings by viewModel.voiceAcousticSettings.collectAsState()
    val curatedTracks by viewModel.curatedTracks.collectAsState()
    val selectedCuratedTrackId by viewModel.selectedCuratedTrackId.collectAsState()

    // Client Attraction Hub & Visual Tour
    val salonTourStations by viewModel.salonTourStations.collectAsState()
    val selectedTourStationId by viewModel.selectedTourStationId.collectAsState()
    val clientAttractionTab by viewModel.clientAttractionTab.collectAsState()

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onAudioSelected(uri)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(IvoryBackground),
            containerColor = IvoryBackground
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                // 1. Studio Header & Branding
                item {
                    StudioBrandingHeader()
                }

                // 2. Error Banner (if error occurred)
                if (errorMessage != null) {
                    item {
                        ErrorBanner(
                            message = errorMessage ?: "",
                            onDismiss = { viewModel.clearErrorMessage() }
                        )
                    }
                }

                // 3. Audio Selection / Audio Player Card (Step 1 Preserved)
                item {
                    if (audioInfo == null) {
                        AudioPickerCard(
                            onSelectAudio = {
                                audioPickerLauncher.launch("audio/*")
                            },
                            onLoadDemoAudio = {
                                viewModel.loadDemoSpeechAudioTrack(autoPlay = false)
                            }
                        )
                    } else {
                        AudioPlayerCard(
                            audioInfo = audioInfo!!,
                            playbackState = playbackState,
                            onTogglePlayPause = { viewModel.togglePlayPause() },
                            onSeekTo = { viewModel.seekTo(it) },
                            onReplayFromStart = { viewModel.replayFromStart() },
                            onChangeAudio = { audioPickerLauncher.launch("audio/*") },
                            onRemoveAudio = { viewModel.promptRemoveAudio() }
                        )
                    }
                }

                // 4. Synchronized Video Canvas Preview (Step 3 & 4)
                item {
                    VideoCanvasPreview(
                        scenes = videoScenes,
                        activeSceneId = activeSceneId,
                        playbackState = playbackState,
                        aspectRatio = videoAspectRatio,
                        imageFitMode = imageFitMode,
                        subtitleStyle = subtitleStyle,
                        onSetAspectRatio = { viewModel.setAspectRatio(it) },
                        onSetImageFitMode = { viewModel.setImageFitMode(it) },
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onPickImageForActiveScene = { sceneId, uriStr ->
                            viewModel.updateSceneImage(sceneId, uriStr)
                        }
                    )
                }

                // 5. CORE CLIENT ATTRACTION & COMMERCIAL PROPOSALS HUB (سخنی با مدیران و تور سالن زیبایی)
                item {
                    val activeScene = videoScenes.firstOrNull { it.sceneId == activeSceneId } ?: videoScenes.firstOrNull()
                    SalonClientAttractionHub(
                        tourStations = salonTourStations,
                        selectedStationId = selectedTourStationId,
                        activeScene = activeScene,
                        businessOffers = businessOffers,
                        activeTab = clientAttractionTab,
                        onTabSelected = { viewModel.setClientAttractionTab(it) },
                        onSelectStation = { viewModel.selectTourStation(it) },
                        onApplyStationToActiveScene = { viewModel.applyTourStationToActiveScene(it) },
                        onApplyFullTourToAllScenes = { viewModel.applyFullVisualTourToAllScenes() },
                        onLoadSpeech36TourPreset = { viewModel.loadSpeech36WithTourPreset() },
                        onPlayScene = { viewModel.play() }
                    )
                }

                // 6. Scene Management & Editor Section (Step 3)
                item {
                    SceneEditorSection(
                        scenes = videoScenes,
                        activeSceneId = activeSceneId,
                        onAddScene = { viewModel.addNewScene() },
                        onDeleteScene = { viewModel.deleteScene(it) },
                        onMoveSceneUp = { viewModel.moveSceneUp(it) },
                        onMoveSceneDown = { viewModel.moveSceneDown(it) },
                        onUpdateSceneImage = { sceneId, uriStr -> viewModel.updateSceneImage(sceneId, uriStr) },
                        onRemoveSceneImage = { viewModel.removeSceneImage(it) },
                        onSeekAndPlayScene = { viewModel.seekAndPlayScene(it) },
                        onToggleApproval = { sceneId, approved -> viewModel.approveSceneVisual(sceneId, approved) }
                    )
                }

                // 7. Subtitle Styling & Persian Display Settings (Step 4)
                item {
                    SubtitleStyleEditorSection(
                        subtitleStyle = subtitleStyle,
                        onSetFontSize = { viewModel.setSubtitleFontSize(it) },
                        onSetTextColor = { viewModel.setSubtitleTextColor(it) },
                        onSetBackgroundColor = { viewModel.setSubtitleBackgroundColor(it) },
                        onSetPosition = { viewModel.setSubtitlePosition(it) },
                        onToggleVisibility = { viewModel.toggleSubtitleVisibility() },
                        onSetMaxLines = { viewModel.setSubtitleMaxLines(it) }
                    )
                }

                // 8. VOCAL IMPACT DSP, ACOUSTIC ROOM REVERB & PEACEFUL UNDERSCORE MUSIC
                item {
                    VoiceAcousticDspSection(
                        acousticSettings = voiceAcousticSettings,
                        curatedTracks = curatedTracks,
                        selectedTrackId = selectedCuratedTrackId,
                        musicTrack = backgroundMusicTrack,
                        speechVolume = speechVolume,
                        isNoiseFilterActive = isNoiseFilterActive,
                        onSetVoiceAcousticEnabled = { viewModel.setVoiceAcousticEnabled(it) },
                        onSetRoomSizePreset = { viewModel.setRoomSizePreset(it) },
                        onSetEchoLevel = { viewModel.setEchoLevel(it) },
                        onSetRoomSizeAmount = { viewModel.setRoomSizeAmount(it) },
                        onSetVocalWarmth = { viewModel.setVocalWarmth(it) },
                        onSetAutoDucking = { viewModel.setAutoDucking(it) },
                        onSelectCuratedTrack = { viewModel.selectCuratedMusicTrack(it) },
                        onSetSpeechVolume = { viewModel.setSpeechVolume(it) },
                        onToggleNoiseFilter = { viewModel.toggleNoiseReductionFilter() },
                        onSetMusicVolume = { viewModel.setBackgroundMusicVolume(it) },
                        onToggleMusicMute = { viewModel.toggleBackgroundMusicMute() },
                        onToggleFadeInOut = { viewModel.toggleMusicFadeInOut() },
                        onPickCustomMusic = { viewModel.loadCustomBackgroundMusic(it) },
                        onRemoveCustomMusic = { viewModel.removeBackgroundMusic() }
                    )
                }

                // 6. Salon Customization Studio (Step 3: Free Instant Visitor Demo)
                item {
                    SalonCustomizationSection(
                        profiles = salonProfiles,
                        activeProfile = activeSalonProfile,
                        onSwitchProfile = { viewModel.switchSalonProfile(it) },
                        onSaveProfile = { viewModel.saveActiveSalonProfile(it) },
                        onCreateNewProfile = { name, manager -> viewModel.createNewSalonProfile(name, manager) }
                    )
                }

                // 7. Licensing & 200 Customer Codes (Step 3)
                item {
                    LicenseStatusCard(
                        activeLicense = activeLicense,
                        licenseFeedback = licenseFeedback,
                        onSubmitLicenseCode = { viewModel.submitLicenseCode(it) },
                        onDeactivateLicense = { viewModel.deactivateLicense() },
                        onClearFeedback = { viewModel.clearLicenseFeedback() }
                    )
                }

                // 8. Speech Transcript & Persian Segmentation (Step 2 Preserved)
                item {
                    SpeechTranscriptSection(
                        segments = speechSegments,
                        analysisState = analysisState,
                        rawTranscript = rawTranscript,
                        activeSegmentId = activeSegmentId,
                        onUpdateRawTranscript = { viewModel.updateRawTranscript(it) },
                        onProcessTranscript = { viewModel.processManualTranscript(it) },
                        onLoadSampleTranscript = { viewModel.loadSampleTranscript() },
                        onRequestCloudAnalysis = { viewModel.requestCloudAnalysis() },
                        onSeekAndPlay = { viewModel.seekAndPlaySegment(it) },
                        onEditSegment = { viewModel.startEditingSegment(it) },
                        onToggleConfirm = { viewModel.toggleSegmentConfirmed(it) }
                    )
                }

                // 9. Business Offers Section (SalonYar & TAVANA - Step 2 Preserved)
                item {
                    BusinessOffersSection(
                        offers = businessOffers,
                        onSeekToTimestamp = { sec ->
                            val ms = (sec * 1000).toLong()
                            viewModel.seekTo(ms)
                            viewModel.play()
                        }
                    )
                }

                // 10. TAVANA Management Dashboard (Step 3: Central Admin Portal)
                item {
                    ManagementDashboardSection(
                        isUnlocked = isManagementUnlocked,
                        allLicenses = all200Licenses,
                        visitorAccounts = visitorAccounts,
                        partnerships = partnerships,
                        transactions = walletTransactions,
                        onUnlock = { viewModel.unlockManagement(it) },
                        onLock = { viewModel.lockManagement() }
                    )
                }

                // 11. Studio Pipeline Roadmap (Phase 3 Active)
                item {
                    PipelineRoadmapSection(
                        hasAudioLoaded = audioInfo != null
                    )
                }

                // 12. Privacy & Device Memory Notice Footer
                item {
                    DeviceSafetyCard()
                }
            }
        }

        // Confirmation Dialog for Removing File (Step 1)
        if (showRemoveDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissRemoveDialog() },
                modifier = Modifier.testTag("remove_confirm_dialog"),
                shape = RoundedCornerShape(22.dp),
                containerColor = IvorySurface,
                title = {
                    Text(
                        text = "حذف فایل از پروژه",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "آیا مطمئن هستید که می‌خواهید این فایل صوتی را از پروژه جاری خارج کنید؟\n\nنکته: فایل اصلی در حافظه دستگاه شما محفوظ است و به هیچ عنوان حذف یا تغییر داده نخواهد شد.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.confirmRemoveAudio() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StateError,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("confirm_remove_button")
                    ) {
                        Text(text = "حذف از پروژه", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.dismissRemoveDialog() },
                        modifier = Modifier.testTag("cancel_remove_button")
                    ) {
                        Text(text = "انصراف", color = TextSecondary)
                    }
                }
            )
        }

        // Explicit Cloud AI Consent Dialog (Step 2)
        if (showCloudConsentDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissCloudConsentDialog() },
                modifier = Modifier.testTag("cloud_consent_dialog"),
                shape = RoundedCornerShape(22.dp),
                containerColor = IvorySurface,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = PeachPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تحلیل با هوش مصنوعی ابری",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                },
                text = {
                    Text(
                        text = "توجه: در صورت تأیید، متن سخنرانی جهت تحلیل معنایی، زمان‌بندی و دسته‌بندی پیشنهادهای تجاری به سرویس امن Gemini ارسال خواهد شد.\n\nاین قابلیت نیازمند تنظیم GEMINI_API_KEY در تنظیمات برنامه (Secrets) است.\n\nآیا مایل به ارسال و ادامه هستید؟",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.proceedWithCloudAnalysis() },
                        colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("confirm_cloud_analysis_btn")
                    ) {
                        Text(text = "موافقم، تحلیل کن", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.dismissCloudConsentDialog() },
                        modifier = Modifier.testTag("cancel_cloud_analysis_btn")
                    ) {
                        Text(text = "انصراف (استفاده از تحلیل محلی)", color = TextSecondary)
                    }
                }
            )
        }

        // Segment Edit Dialog (Step 2)
        editingSegment?.let { segment ->
            SegmentEditDialog(
                segment = segment,
                onDismiss = { viewModel.cancelEditingSegment() },
                onSave = { updated -> viewModel.saveEditedSegment(updated) }
            )
        }
    }
}

@Composable
private fun StudioBrandingHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("studio_branding_card"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = IvorySurface
        ),
        border = BorderStroke(1.dp, IvoryCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.tavana_icon),
                        contentDescription = "نشان تجاری توانا",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "TAVANA STUDIO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PeachPrimary,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "استودیوی هوشمند صدا به ویدیو",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "پلتفرم تخصصی تبدیل سخنرانی‌های صوتی فارسی به ویدیوهای غنی همراه با تصویرسازی صحنه‌ها، شخصی‌سازی سالنیار، سیستم لایسنس و رندر MP4.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = WarmPeachLight
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(PeachPrimary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "گام ۳ از ۵: تصاویر هوشمند، شخصی‌سازی سالنیار، مجوزها و آماده‌سازی انتشار",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnPeachContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AudioPickerCard(
    onSelectAudio: () -> Unit,
    onLoadDemoAudio: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("audio_picker_card"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        border = BorderStroke(1.5.dp, PeachPrimary.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = PeachContainer,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "آپلود فایل صوتی",
                        tint = PeachPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "انتخاب فایل سخنرانی یا صوت",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "فایل صوتی سخنرانی خود را از حافظه دستگاه انتخاب کنید تا مدت زمان، کیفیت و پخش آن ارزیابی شود، یا نمونه صوتی دمو را بارگذاری فرمایید.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onSelectAudio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("select_audio_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PeachPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Audiotrack,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "انتخاب فایل صدای شخصی از گوشی",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onLoadDemoAudio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("load_demo_audio_btn"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.2.dp, PeachPrimary.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PeachPrimary)
            ) {
                Icon(imageVector = Icons.Default.Audiotrack, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "بارگذاری نمونه صوتی دمو (سخنرانی ۳۶)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = IvorySurfaceVariant
            ) {
                Text(
                    text = "پشتیبانی از فرمت‌های صوتی: MP3 • WAV • M4A • AAC • OGG",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("error_banner"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = StateErrorContainer),
        border = BorderStroke(1.dp, StateError.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "خطا",
                tint = StateError,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = StateError,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "بستن پیام خطا",
                    tint = StateError,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun DeviceSafetyCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("device_safety_card"),
        shape = RoundedCornerShape(18.dp),
        color = IvorySurfaceVariant.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, IvoryCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = SoftPink,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "امنیت فایل و حریم خصوصی",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "فایل‌های صوتی و تصاویر به صورت کاملاً محلی روی دستگاه بارگذاری می‌شوند. فایل اصلی شما در حافظه گوشی دست‌نخورده باقی می‌ماند و اطلاعات تجاری سالن‌ها بدون اجازه منتشر نخواهند شد.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
