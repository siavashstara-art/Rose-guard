package com.example

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.example.model.AudioFileInfo
import com.example.model.AudioPlaybackUiState
import com.example.model.BusinessOfferCategory
import com.example.model.LicenseStatus
import com.example.model.SalonProfile
import com.example.model.SalonTemplateLayout
import com.example.model.SpeechSegment
import com.example.model.VideoAspectRatio
import com.example.model.VideoScene
import com.example.model.VisualDisplayType
import com.example.service.LicenseValidationResult
import com.example.service.LicenseValidationService
import com.example.service.SalonProfileRepository
import com.example.service.SpeechSegmentationEngine
import com.example.viewmodel.AudioStudioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {

    @Test
    fun testAudioDurationFormatting() {
        assertEquals("۰۰:۰۰", AudioStudioViewModel.formatDuration(0L))
        assertEquals("۰۰:۰۰", AudioStudioViewModel.formatDuration(-100L))
        assertEquals("01:15", AudioStudioViewModel.formatDuration(75_000L))
        assertEquals("03:05", AudioStudioViewModel.formatDuration(185_000L))
        assertEquals("65:00", AudioStudioViewModel.formatDuration(3_900_000L))
    }

    @Test
    fun testAudioFileSizeFormatting() {
        assertEquals("نامشخص", AudioStudioViewModel.formatFileSize(0L))
        val sizeKb = AudioStudioViewModel.formatFileSize(512 * 1024L)
        assertTrue(sizeKb.contains("کیلوبایت"))
        val sizeMb = AudioStudioViewModel.formatFileSize(5 * 1024 * 1024L)
        assertTrue(sizeMb.contains("مگابایت"))
    }

    @Test
    fun testAudioPlaybackUiStateDefaults() {
        val defaultState = AudioPlaybackUiState()
        assertFalse(defaultState.isPlaying)
        assertFalse(defaultState.isLoaded)
        assertEquals(0L, defaultState.currentPositionMs)
        assertEquals(0L, defaultState.totalDurationMs)
        assertNull(defaultState.errorMessage)
    }

    @Test
    fun testSpeechSegmentationFromPersianText() {
        val text = """
            سلام به همه همکاران گرامی.
            امروز سالنیار را معرفی می‌کنیم.
            ویترین اختصاصی برای هر سالن زیبایی ساخته می‌شود.
        """.trimIndent()

        val segments = SpeechSegmentationEngine.processTextToSegments(text, 60.0)
        assertEquals(3, segments.size)

        assertEquals("seg_1", segments[0].id)
        assertTrue(segments[0].text.contains("سلام"))
        assertEquals(0.0, segments[0].startTimeSec, 0.1)

        assertTrue(segments[1].startTimeSec >= segments[0].startTimeSec)
        assertTrue(segments[2].endTimeSec <= 60.0)
    }

    @Test
    fun testBusinessOfferExtractionAndCategorization() {
        val segments = listOf(
            SpeechSegment(
                id = "s1",
                text = "سالنیار برای هر سالن زیبایی یک ویترین اختصاصی و شیک ایجاد می‌کند.",
                startTimeSec = 0.0,
                endTimeSec = 10.0
            ),
            SpeechSegment(
                id = "s2",
                text = "در این مدل همکاری با مزون‌ها، درصد کمیسیون توافقی به طرفین تعلق می‌گیرد.",
                startTimeSec = 10.0,
                endTimeSec = 25.0
            ),
            SpeechSegment(
                id = "s3",
                text = "در فازهای توسعه آینده، سیستم هوش مصنوعی به سالنیار افزوده خواهد شد.",
                startTimeSec = 25.0,
                endTimeSec = 40.0
            )
        )

        val offers = SpeechSegmentationEngine.extractBusinessOffers(segments)
        assertTrue(offers.isNotEmpty())

        val featureOffer = offers.firstOrNull { it.category == BusinessOfferCategory.CONFIRMED_FEATURE }
        assertNotNull("Should have confirmed feature offer", featureOffer)
        assertTrue(featureOffer!!.title.contains("ویترین"))
        assertNotNull(featureOffer.sourceQuote)

        val partnershipOffer = offers.firstOrNull { it.category == BusinessOfferCategory.SPEECH_OFFER }
        assertNotNull("Should have speech partnership offer", partnershipOffer)

        val futureOffer = offers.firstOrNull { it.category == BusinessOfferCategory.FUTURE_IDEA }
        assertNotNull("Should have future idea offer", futureOffer)
    }

    @Test
    fun test200LicensesGenerationAndCryptographicUniqueness() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val licenseService = LicenseValidationService(context)
        val all200 = licenseService.getAll200Licenses()

        assertEquals(200, all200.size)

        // Check index formatting 001 to 200
        assertEquals("001", all200.first().formattedIndex)
        assertEquals("200", all200.last().formattedIndex)

        // Verify keys are unique and non-guessable
        val keysSet = all200.map { it.clientSecretKey }.toSet()
        assertEquals(200, keysSet.size)

        all200.forEach { lic ->
            assertTrue(lic.clientSecretKey.startsWith("TAV-SALON-${lic.formattedIndex}-"))
            assertNotNull(lic.status)
        }
    }

    @Test
    fun testLicenseValidationScenarios() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val licenseService = LicenseValidationService(context)

        // 1. Valid key activation (e.g. index 1)
        val validKey001 = LicenseValidationService.generateSecretKeyForIndex(1)
        val resultValid = licenseService.validateAndActivateKey(validKey001)
        assertTrue("Valid key should succeed", resultValid is LicenseValidationResult.Success)
        val activeLic = licenseService.getCurrentActiveLicense()
        assertNotNull(activeLic)
        assertEquals(1, activeLic!!.licenseIndex)

        // 2. Invalid formatted or forged key
        val resultInvalid = licenseService.validateAndActivateKey("INVALID-KEY-12345")
        assertTrue("Invalid key should fail", resultInvalid is LicenseValidationResult.InvalidKey)

        // 3. Expired key (index 42 is flagged expired in simulated lifecycle)
        val expiredKey042 = LicenseValidationService.generateSecretKeyForIndex(42)
        val resultExpired = licenseService.validateAndActivateKey(expiredKey042)
        assertTrue("Key 042 should be expired", resultExpired is LicenseValidationResult.Expired)

        // 4. Revoked key (index 13 is flagged revoked by central management)
        val revokedKey013 = LicenseValidationService.generateSecretKeyForIndex(13)
        val resultRevoked = licenseService.validateAndActivateKey(revokedKey013)
        assertTrue("Key 013 should be revoked", resultRevoked is LicenseValidationResult.Revoked)
    }

    @Test
    fun testSalonProfilesIsolationAndPersistence() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = SalonProfileRepository(context)

        val profiles = repo.getAllProfiles()
        assertTrue("Should contain at least 2 default test salons", profiles.size >= 2)

        val salon1 = profiles.first { it.id == "salon_001" }
        val salon2 = profiles.first { it.id == "salon_002" }

        // Modify salon 1 name and template
        val updatedSalon1 = salon1.copy(
            salonName = "نام جدید سالن شماره یک",
            selectedTemplate = SalonTemplateLayout.SIDE_BY_SIDE
        )
        repo.saveProfile(updatedSalon1)

        // Verify salon 2 remained completely unchanged
        val reloadedProfiles = repo.getAllProfiles()
        val reloadedSalon1 = reloadedProfiles.first { it.id == "salon_001" }
        val reloadedSalon2 = reloadedProfiles.first { it.id == "salon_002" }

        assertEquals("نام جدید سالن شماره یک", reloadedSalon1.salonName)
        assertEquals(SalonTemplateLayout.SIDE_BY_SIDE, reloadedSalon1.selectedTemplate)
        assertEquals(salon2.salonName, reloadedSalon2.salonName)
        assertNotEquals(reloadedSalon1.salonName, reloadedSalon2.salonName)
    }

    @Test
    fun testVideoAspectRatioRatios() {
        assertEquals(9f / 16f, VideoAspectRatio.PORTRAIT_9_16.ratioFloat, 0.001f)
        assertEquals(16f / 9f, VideoAspectRatio.LANDSCAPE_16_9.ratioFloat, 0.001f)
    }

    @Test
    fun testSubtitleStylePresetsAndDefaults() {
        val defaultStyle = com.example.model.SubtitleStyle()
        assertEquals(16f, defaultStyle.fontSizeSp, 0.1f)
        assertEquals(0xFFFFFFFF, defaultStyle.textColorHex)
        assertEquals(0xAA000000, defaultStyle.backgroundColorHex)
        assertEquals(com.example.model.SubtitlePosition.BOTTOM, defaultStyle.position)
        assertEquals(2, defaultStyle.maxLines)
        assertTrue(defaultStyle.isVisible)

        // Test Color Presets
        val whitePreset = com.example.model.SubtitleColorPreset.WHITE
        assertEquals(0xFFFFFFFF, whitePreset.colorHex)
        val goldPreset = com.example.model.SubtitleColorPreset.GOLD
        assertEquals(0xFFFFD54F, goldPreset.colorHex)

        // Test Background Presets
        val blackPreset = com.example.model.SubtitleBackgroundPreset.TRANSLUCENT_BLACK
        assertEquals(0xAA000000, blackPreset.colorHex)
        val darkCard = com.example.model.SubtitleBackgroundPreset.DARK_CARD
        assertEquals(0xDD1F1B1A, darkCard.colorHex)
        val clearPreset = com.example.model.SubtitleBackgroundPreset.TRANSPARENT
        assertEquals(0x00000000, clearPreset.colorHex)
    }

    @Test
    fun testBackgroundMusicTrackDefaults() {
        val defaultTrack = com.example.model.BackgroundMusicTrack(
            id = "test_track",
            title = "موسیقی پیانو آرامش‌بخش",
            artistOrSource = "CC0 License",
            licenseNotice = "مجوز آزاد تجاری بدون حق انحصاری",
            uriString = null,
            volume = 0.25f,
            isMuted = false,
            isFadeInOutEnabled = true
        )
        assertEquals("test_track", defaultTrack.id)
        assertEquals(0.25f, defaultTrack.volume, 0.01f)
        assertFalse(defaultTrack.isMuted)
        assertTrue(defaultTrack.isFadeInOutEnabled)
        assertNull(defaultTrack.uriString)
    }

    @Test
    fun testSalonTourCatalogStations() {
        val stations = com.example.model.SalonTourCatalog.STATIONS
        assertEquals(5, stations.size)

        val stationIds = stations.map { it.id }
        assertTrue(stationIds.contains("tour_reception"))
        assertTrue(stationIds.contains("tour_bridal"))
        assertTrue(stationIds.contains("tour_hair"))
        assertTrue(stationIds.contains("tour_spa"))
        assertTrue(stationIds.contains("tour_nail"))

        stations.forEach { station ->
            assertTrue(station.title.isNotBlank())
            assertTrue(station.subtitle.isNotBlank())
            assertTrue(station.attractionBadge.isNotBlank())
            assertTrue(station.drawableResName.startsWith("img_tour_"))
            assertTrue(station.keyHighlights.isNotEmpty())
        }

        assertTrue(com.example.model.SalonTourCatalog.SPEECH_36_FULL_TEXT.contains("سخنان با مدیران"))
    }

    @Test
    fun testCuratedMusicCatalogAndVoiceAcousticSettings() {
        val tracks = com.example.model.CuratedMusicCatalog.TRACKS
        assertEquals(4, tracks.size)

        val defaultTrack = tracks.first()
        assertEquals("ambient_piano_01", defaultTrack.id)
        assertTrue(defaultTrack.defaultVolume <= 0.25f) // Ensures gentle underscore under voice

        val voiceSettings = com.example.model.VoiceAcousticSettings()
        assertTrue(voiceSettings.isEnabled)
        assertEquals(com.example.model.RoomSizePreset.ENGAGING_ROOM, voiceSettings.roomSize)
        assertEquals(0.20f, voiceSettings.echoLevel, 0.01f)
        assertEquals(0.35f, voiceSettings.roomSizeAmount, 0.01f)
        assertEquals(0.30f, voiceSettings.vocalWarmth, 0.01f)
        assertTrue(voiceSettings.autoDucking)
    }

    @Test
    fun testSalonTourImageResolverResolvesDirectDrawables() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val receptionModel = com.example.model.SalonTourImageResolver.resolveImageModel(context, "img_tour_reception")
        assertEquals(R.drawable.img_tour_reception, receptionModel)

        val bridalModel = com.example.model.SalonTourImageResolver.resolveImageModel(context, "img_tour_bridal")
        assertEquals(R.drawable.img_tour_bridal, bridalModel)

        val hairModel = com.example.model.SalonTourImageResolver.resolveImageModel(context, "img_tour_hair")
        assertEquals(R.drawable.img_tour_hair, hairModel)

        val spaModel = com.example.model.SalonTourImageResolver.resolveImageModel(context, "img_tour_spa")
        assertEquals(R.drawable.img_tour_spa, spaModel)

        val nailModel = com.example.model.SalonTourImageResolver.resolveImageModel(context, "img_tour_nail")
        assertEquals(R.drawable.img_tour_nail, nailModel)

        val nullModel = com.example.model.SalonTourImageResolver.resolveImageModel(context, null)
        assertNull(nullModel)

        val blankModel = com.example.model.SalonTourImageResolver.resolveImageModel(context, "   ")
        assertNull(blankModel)
    }

    @Test
    fun testDemoSpeechAudioEngineWavGeneration() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        kotlinx.coroutines.runBlocking {
            val file = com.example.service.DemoSpeechAudioEngine.getOrCreateDemoSpeechTrack(context)
            assertTrue(file.exists())
            assertTrue(file.length() > 500000L)
        }
    }
}

