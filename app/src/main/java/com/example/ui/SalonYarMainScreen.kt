package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BeforeAfterSection
import com.example.ui.components.DreamScenarioStudioSection
import com.example.ui.components.HesitantClientSection
import com.example.ui.components.LicenseLockedContainer
import com.example.ui.components.StoryMakerSection
import com.example.ui.components.VisitorLicensePanelSection
import com.example.ui.components.VoiceoverStudioSection
import com.example.viewmodel.SalonYarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonYarMainScreen(
    viewModel: SalonYarViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.activeTab.collectAsState()
    val licenseState by viewModel.licenseState.collectAsState()
    val salonProfile by viewModel.salonProfile.collectAsState()

    val selectedFace by viewModel.selectedFaceShape.collectAsState()
    val selectedBody by viewModel.selectedBodyAnatomy.collectAsState()
    val selectedSkin by viewModel.selectedSkinTone.collectAsState()
    val hesitantRec by viewModel.hesitantRecommendation.collectAsState()

    val selectedTransformation by viewModel.selectedTransformation.collectAsState()
    val activePrivacyMask by viewModel.activePrivacyMask.collectAsState()
    val splitRatio by viewModel.beforeAfterSplitRatio.collectAsState()

    val selectedScenario by viewModel.selectedScenario.collectAsState()
    val selectedGown by viewModel.selectedBridalGown.collectAsState()
    val selectedSuit by viewModel.selectedGroomSuit.collectAsState()
    val selectedCrown by viewModel.selectedCrown.collectAsState()
    val crownScale by viewModel.crownScale.collectAsState()
    val crownOffsetY by viewModel.crownOffsetY.collectAsState()

    val aiVoiceState by viewModel.aiVoiceState.collectAsState()
    val storyDraft by viewModel.storyDraft.collectAsState()

    // Persian Right-To-Left layout provider
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "سالن‌یار VIP",
                                        color = Color(0xFFECC34C),
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• ${salonProfile.salonName}",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = "مدیریت: ${salonProfile.managerName} | اکوسیستم توانا",
                                    color = Color(0xFFC4B5FD),
                                    fontSize = 11.sp
                                )
                            }

                            // License Status Chip
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (licenseState.isActivated) Color(0xFF14532D) else Color(0xFF7F1D1D),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.setActiveTab(5) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("topbar_license_badge")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (licenseState.isActivated) Icons.Default.Verified else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (licenseState.isActivated) Color(0xFF4ADE80) else Color(0xFFFCA5A5),
                                        modifier = Modifier.width(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (licenseState.isActivated) "۱ ساله فعال" else "دمو (قفل)",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF160924)
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF12071C),
                    contentColor = Color(0xFFD4AF37)
                ) {
                    val items = listOf(
                        Triple(0, "هوش صوتی", Icons.Default.RecordVoiceOver),
                        Triple(1, "مشتری مردد", Icons.Default.AutoAwesome),
                        Triple(2, "پرو و سناریو", Icons.Default.Celebration),
                        Triple(3, "قبل و بعد", Icons.Default.Compare),
                        Triple(4, "استوریساز", Icons.Default.CameraAlt),
                        Triple(5, "ویزیتور", Icons.Default.Business)
                    )

                    items.forEach { (index, label, icon) ->
                        val isSelected = activeTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setActiveTab(index) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) Color(0xFFECC34C) else Color(0xFF8B7A9F)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFFECC34C) else Color(0xFF8B7A9F)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFF381A56)
                            ),
                            modifier = Modifier.testTag("nav_tab_$index")
                        )
                    }
                }
            },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F0816), Color(0xFF190C25), Color(0xFF0B0610))
                        )
                    )
            ) {
                when (activeTab) {
                    0 -> {
                        // Voiceover & Pitch Studio
                        VoiceoverStudioSection(
                            playState = aiVoiceState,
                            onTogglePlay = { viewModel.toggleSpeechPlayback() },
                            onSelectPersona = { viewModel.selectAiPersona(it) },
                            onToggleEcho = { viewModel.toggleEcho() },
                            onToggleMusic = { viewModel.toggleMusic() }
                        )
                    }
                    1 -> {
                        // Hesitant Client Engine (Locked until license)
                        LicenseLockedContainer(
                            isLocked = licenseState.isFeatureLocked,
                            featureTitle = "موتور استایل‌شناسی جذب مشتریان مردد",
                            onActivateCode = { viewModel.activateLicense(it) },
                            onNavigateToLicenseTab = { viewModel.setActiveTab(5) }
                        ) {
                            HesitantClientSection(
                                selectedFaceShape = selectedFace,
                                selectedBodyAnatomy = selectedBody,
                                selectedSkinTone = selectedSkin,
                                recommendation = hesitantRec,
                                onSelectFace = { viewModel.selectFaceShape(it) },
                                onSelectBody = { viewModel.selectBodyAnatomy(it) },
                                onSelectSkin = { viewModel.selectSkinTone(it) }
                            )
                        }
                    }
                    2 -> {
                        // Dream Scenario & Wardrobe Studio (Locked until license)
                        LicenseLockedContainer(
                            isLocked = licenseState.isFeatureLocked,
                            featureTitle = "موتور سناریوساز رویایی و ۵۰ لباس عروس + ۳۵ کت‌شلوار",
                            onActivateCode = { viewModel.activateLicense(it) },
                            onNavigateToLicenseTab = { viewModel.setActiveTab(5) }
                        ) {
                            DreamScenarioStudioSection(
                                selectedScenario = selectedScenario,
                                selectedGown = selectedGown,
                                selectedSuit = selectedSuit,
                                selectedCrown = selectedCrown,
                                crownScale = crownScale,
                                crownOffsetY = crownOffsetY,
                                onSelectScenario = { viewModel.selectScenario(it) },
                                onSelectGown = { viewModel.selectBridalGown(it) },
                                onSelectSuit = { viewModel.selectGroomSuit(it) },
                                onSelectCrown = { viewModel.selectCrown(it) },
                                onScaleCrown = { viewModel.adjustCrownScale(it) },
                                onOffsetYCrown = { viewModel.adjustCrownOffsetY(it) }
                            )
                        }
                    }
                    3 -> {
                        // Before & After and Privacy Masks
                        BeforeAfterSection(
                            selectedRecord = selectedTransformation,
                            activeMask = activePrivacyMask,
                            splitRatio = splitRatio,
                            onSplitChange = { viewModel.setBeforeAfterSplit(it) },
                            onSelectMask = { viewModel.selectPrivacyMask(it) }
                        )
                    }
                    4 -> {
                        // Story Maker for Instagram & Telegram (Locked until license)
                        LicenseLockedContainer(
                            isLocked = licenseState.isFeatureLocked,
                            featureTitle = "استوریساز ۹:۱۶ اینستاگرام و تلگرام",
                            onActivateCode = { viewModel.activateLicense(it) },
                            onNavigateToLicenseTab = { viewModel.setActiveTab(5) }
                        ) {
                            StoryMakerSection(
                                draft = storyDraft,
                                onSelectTemplate = { viewModel.setStoryTemplate(it) },
                                onUpdateTexts = { h, s -> viewModel.updateStoryTexts(h, s) },
                                onToggleSticker = { viewModel.toggleStorySticker(it) },
                                onShareInstagram = { viewModel.shareToInstagram() },
                                onShareTelegram = { viewModel.shareToTelegram() },
                                onShareGeneral = { viewModel.shareStoryUniversal() }
                            )
                        }
                    }
                    5 -> {
                        // Visitor & License Customization Panel
                        VisitorLicensePanelSection(
                            licenseState = licenseState,
                            salonProfile = salonProfile,
                            onActivateLicense = { viewModel.activateLicense(it) },
                            onCopyLicenseCode = { viewModel.copyLicenseCode(it) },
                            onSaveProfile = { viewModel.updateSalonProfile(it) }
                        )
                    }
                }
            }
        }
    }
}
