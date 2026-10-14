package com.example.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.model.AiVoicePersona
import com.example.model.BodyAnatomy
import com.example.model.BridalCrownCatalog
import com.example.model.BridalCrown
import com.example.model.BridalGownModel
import com.example.model.DreamScenario
import com.example.model.FaceShape
import com.example.model.GroomSuitModel
import com.example.model.HesitantClientEngine
import com.example.model.HesitantRecommendation
import com.example.model.LicenseCodeGenerator
import com.example.model.LicenseState
import com.example.model.PrivacyMaskType
import com.example.model.SalonProfile
import com.example.model.SiavashAlimiriSpeech
import com.example.model.SkinTone
import com.example.model.StoryDraft
import com.example.model.StoryInteractiveSticker
import com.example.model.StoryTemplate
import com.example.model.TransformationArchive
import com.example.model.TransformationRecord
import com.example.model.WardrobeCatalog
import com.example.service.AiVoiceNarrationService
import com.example.service.AiVoicePlayState
import com.example.service.StoryExportService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SalonYarViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication<Application>().applicationContext
    private val prefs = context.getSharedPreferences("salonyar_vip_prefs", Context.MODE_PRIVATE)

    // 1. License State
    private val _licenseState = MutableStateFlow(loadSavedLicense())
    val licenseState: StateFlow<LicenseState> = _licenseState.asStateFlow()

    // 2. Salon Visitor Profile
    private val _salonProfile = MutableStateFlow(loadSavedProfile())
    val salonProfile: StateFlow<SalonProfile> = _salonProfile.asStateFlow()

    // 3. Hesitant Client Styling Engine
    private val _selectedFaceShape = MutableStateFlow(FaceShape.OVAL)
    val selectedFaceShape: StateFlow<FaceShape> = _selectedFaceShape.asStateFlow()

    private val _selectedBodyAnatomy = MutableStateFlow(BodyAnatomy.HOURGLASS)
    val selectedBodyAnatomy: StateFlow<BodyAnatomy> = _selectedBodyAnatomy.asStateFlow()

    private val _selectedSkinTone = MutableStateFlow(SkinTone.FAIR)
    val selectedSkinTone: StateFlow<SkinTone> = _selectedSkinTone.asStateFlow()

    private val _hesitantRecommendation = MutableStateFlow(
        HesitantClientEngine.analyzeAndRecommend(FaceShape.OVAL, BodyAnatomy.HOURGLASS, SkinTone.FAIR)
    )
    val hesitantRecommendation: StateFlow<HesitantRecommendation> = _hesitantRecommendation.asStateFlow()

    // 4. Before & After and Privacy
    private val _selectedTransformation = MutableStateFlow(TransformationArchive.SAMPLE_RECORDS.first())
    val selectedTransformation: StateFlow<TransformationRecord> = _selectedTransformation.asStateFlow()

    private val _activePrivacyMask = MutableStateFlow(PrivacyMaskType.GULF_ROSE_GOLD)
    val activePrivacyMask: StateFlow<PrivacyMaskType> = _activePrivacyMask.asStateFlow()

    private val _beforeAfterSplitRatio = MutableStateFlow(0.5f)
    val beforeAfterSplitRatio: StateFlow<Float> = _beforeAfterSplitRatio.asStateFlow()

    // 5. Wardrobe & Dream Scenario Studio
    private val _selectedScenario = MutableStateFlow(DreamScenario.DANCE_STAGE)
    val selectedScenario: StateFlow<DreamScenario> = _selectedScenario.asStateFlow()

    private val _selectedBridalGown = MutableStateFlow(WardrobeCatalog.BRIDAL_50_GOWNS.first())
    val selectedBridalGown: StateFlow<BridalGownModel> = _selectedBridalGown.asStateFlow()

    private val _selectedGroomSuit = MutableStateFlow(WardrobeCatalog.GROOM_35_SUITS.first())
    val selectedGroomSuit: StateFlow<GroomSuitModel> = _selectedGroomSuit.asStateFlow()

    private val _selectedCrown = MutableStateFlow(BridalCrownCatalog.CROWNS.first())
    val selectedCrown: StateFlow<BridalCrown> = _selectedCrown.asStateFlow()

    private val _crownScale = MutableStateFlow(1.0f)
    val crownScale: StateFlow<Float> = _crownScale.asStateFlow()

    private val _crownOffsetY = MutableStateFlow(0f)
    val crownOffsetY: StateFlow<Float> = _crownOffsetY.asStateFlow()

    // 6. Voice Pitch & AI Speech
    val aiVoiceService = AiVoiceNarrationService(context)
    val aiVoiceState: StateFlow<AiVoicePlayState> = aiVoiceService.state

    // 7. Story Maker
    private val _storyDraft = MutableStateFlow(
        StoryDraft(
            salonBrandName = _salonProfile.value.salonName,
            instagramHandle = _salonProfile.value.instagramId,
            phoneContact = _salonProfile.value.phoneNumber
        )
    )
    val storyDraft: StateFlow<StoryDraft> = _storyDraft.asStateFlow()

    // Active Tab in navigation
    private val _activeTab = MutableStateFlow(0)
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    fun setActiveTab(tab: Int) {
        _activeTab.value = tab
    }

    // --- License Actions ---
    private fun loadSavedLicense(): LicenseState {
        val activated = prefs.getBoolean("is_activated", false)
        val code = prefs.getString("license_code", "") ?: ""
        return LicenseState(
            isActivated = activated,
            activeCode = code,
            validDaysRemaining = if (activated) 365 else 0,
            isDemoMode = !activated
        )
    }

    fun activateLicense(code: String): Boolean {
        if (LicenseCodeGenerator.isValidCode(code)) {
            prefs.edit()
                .putBoolean("is_activated", true)
                .putString("license_code", code.trim())
                .apply()
            _licenseState.value = LicenseState(
                isActivated = true,
                activeCode = code.trim(),
                validDaysRemaining = 365,
                isDemoMode = false
            )
            Toast.makeText(context, "✅ لایسنس یک‌ساله ۹ میلیون تومانی سالن‌یار با موفقیت فعال شد!", Toast.LENGTH_LONG).show()
            return true
        } else {
            Toast.makeText(context, "❌ کد لایسنس وارد شده نامعتبر است. از کدهای ویزیتور استفاده کنید.", Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun copyLicenseCode(code: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("SalonYar License", code))
        Toast.makeText(context, "کد لایسنس کپی شد: $code", Toast.LENGTH_SHORT).show()
    }

    // --- Visitor Customization Actions ---
    private fun loadSavedProfile(): SalonProfile {
        return SalonProfile(
            salonName = prefs.getString("salon_name", "سالن زیبایی و عروس گل‌آرا") ?: "سالن زیبایی و عروس گل‌آرا",
            managerName = prefs.getString("manager_name", "خانم سارا ابراهیمی") ?: "خانم سارا ابراهیمی",
            phoneNumber = prefs.getString("phone_number", "0912-345-6789") ?: "0912-345-6789",
            landlinePhone = prefs.getString("landline_phone", "021-22004488") ?: "021-22004488",
            address = prefs.getString("address", "تهران، سعادت‌آباد، میدان کاج، مجتمع تجاری سرو، طبقه ۳") ?: "تهران، سعادت‌آباد، میدان کاج، مجتمع تجاری سرو، طبقه ۳",
            instagramId = prefs.getString("instagram_id", "@golara_beauty_vip") ?: "@golara_beauty_vip",
            telegramId = prefs.getString("telegram_id", "@golara_bridal") ?: "@golara_bridal",
            websiteOrBookingLink = prefs.getString("booking_link", "https://golara-salon.ir/booking") ?: "https://golara-salon.ir/booking"
        )
    }

    fun updateSalonProfile(newProfile: SalonProfile) {
        _salonProfile.value = newProfile
        prefs.edit()
            .putString("salon_name", newProfile.salonName)
            .putString("manager_name", newProfile.managerName)
            .putString("phone_number", newProfile.phoneNumber)
            .putString("landline_phone", newProfile.landlinePhone)
            .putString("address", newProfile.address)
            .putString("instagram_id", newProfile.instagramId)
            .putString("telegram_id", newProfile.telegramId)
            .putString("booking_link", newProfile.websiteOrBookingLink)
            .apply()

        // Sync to story draft
        _storyDraft.value = _storyDraft.value.copy(
            salonBrandName = newProfile.salonName,
            instagramHandle = newProfile.instagramId,
            phoneContact = newProfile.phoneNumber
        )
        Toast.makeText(context, "اطلاعات سالن با موفقیت شخصی‌سازی شد.", Toast.LENGTH_SHORT).show()
    }

    // --- Hesitant Styling Actions ---
    fun selectFaceShape(face: FaceShape) {
        _selectedFaceShape.value = face
        refreshRecommendation()
    }

    fun selectBodyAnatomy(body: BodyAnatomy) {
        _selectedBodyAnatomy.value = body
        refreshRecommendation()
    }

    fun selectSkinTone(tone: SkinTone) {
        _selectedSkinTone.value = tone
        refreshRecommendation()
    }

    private fun refreshRecommendation() {
        _hesitantRecommendation.value = HesitantClientEngine.analyzeAndRecommend(
            _selectedFaceShape.value,
            _selectedBodyAnatomy.value,
            _selectedSkinTone.value
        )
    }

    // --- Before/After & Privacy Actions ---
    fun setBeforeAfterSplit(ratio: Float) {
        _beforeAfterSplitRatio.value = ratio.coerceIn(0.05f, 0.95f)
    }

    fun selectPrivacyMask(mask: PrivacyMaskType) {
        _activePrivacyMask.value = mask
        _storyDraft.value = _storyDraft.value.copy(privacyMaskApplied = mask)
    }

    // --- Wardrobe & Scenario Actions ---
    fun selectScenario(scenario: DreamScenario) {
        _selectedScenario.value = scenario
    }

    fun selectBridalGown(gown: BridalGownModel) {
        _selectedBridalGown.value = gown
    }

    fun selectGroomSuit(suit: GroomSuitModel) {
        _selectedGroomSuit.value = suit
    }

    fun selectCrown(crown: BridalCrown) {
        _selectedCrown.value = crown
        _crownScale.value = crown.defaultScale
        _crownOffsetY.value = crown.defaultOffsetY
    }

    fun adjustCrownScale(delta: Float) {
        _crownScale.value = (_crownScale.value + delta).coerceIn(0.6f, 1.8f)
    }

    fun adjustCrownOffsetY(delta: Float) {
        _crownOffsetY.value = (_crownOffsetY.value + delta).coerceIn(-60f, 60f)
    }

    // --- Voiceover & Speech Actions ---
    fun toggleSpeechPlayback() {
        aiVoiceService.speakScript(SiavashAlimiriSpeech.FULL_SPEECH_TEXT)
    }

    fun selectAiPersona(persona: AiVoicePersona) {
        aiVoiceService.setPersona(persona)
    }

    fun toggleEcho() {
        aiVoiceService.toggleEchoReverb()
    }

    fun toggleMusic() {
        aiVoiceService.toggleBackgroundMusic()
    }

    fun playCustomAudio(uri: android.net.Uri, displayName: String) {
        aiVoiceService.playCustomAudioUri(uri, displayName)
    }

    fun setAudioVolume(vol: Float) {
        aiVoiceService.setVolume(vol)
    }

    fun playTestChime() {
        aiVoiceService.playTestChime()
    }

    // --- Story Maker Actions ---
    fun setStoryTemplate(template: StoryTemplate) {
        _storyDraft.value = _storyDraft.value.copy(
            template = template,
            headline = template.defaultHeadline,
            subheadline = template.defaultSubheadline
        )
    }

    fun updateStoryTexts(headline: String, subheadline: String) {
        _storyDraft.value = _storyDraft.value.copy(
            headline = headline,
            subheadline = subheadline
        )
    }

    fun toggleStorySticker(sticker: StoryInteractiveSticker) {
        val current = _storyDraft.value.activeStickers.toMutableSet()
        if (current.contains(sticker)) {
            current.remove(sticker)
        } else {
            current.add(sticker)
        }
        _storyDraft.value = _storyDraft.value.copy(activeStickers = current)
    }

    fun shareToInstagram() {
        StoryExportService.shareStory(context, _storyDraft.value, "com.instagram.android")
    }

    fun shareToTelegram() {
        StoryExportService.shareStory(context, _storyDraft.value, "org.telegram.messenger")
    }

    fun shareStoryUniversal() {
        StoryExportService.shareStory(context, _storyDraft.value, null)
    }

    override fun onCleared() {
        super.onCleared()
        aiVoiceService.release()
    }
}
