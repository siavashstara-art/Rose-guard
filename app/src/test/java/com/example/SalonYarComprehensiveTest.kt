package com.example

import com.example.model.AiVoicePersona
import com.example.model.BodyAnatomy
import com.example.model.BridalCrownCatalog
import com.example.model.DreamScenario
import com.example.model.FaceShape
import com.example.model.HesitantClientEngine
import com.example.model.LicenseCodeGenerator
import com.example.model.PrivacyMaskType
import com.example.model.SiavashAlimiriSpeech
import com.example.model.SkinTone
import com.example.model.StoryDraft
import com.example.model.StoryInteractiveSticker
import com.example.model.StoryTemplate
import com.example.model.WardrobeCatalog
import com.example.service.StoryExportService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Real automated functional test suite for SalonYar VIP.
 * Validates all 8 core features, wardrobe catalog counts, license codes, and story generation.
 */
class SalonYarComprehensiveTest {

    @Test
    fun test100LicenseCodesGenerationAndValidation() {
        val codes = LicenseCodeGenerator.ALL_100_LICENSE_CODES
        // Must contain 100 distinct codes
        assertEquals("باید دقیقا ۱۰۰ کد لایسنس موجود باشد", 100, codes.size)
        assertEquals("کدها باید یکتا باشند", 100, codes.distinct().size)

        // Validate prefix and format
        codes.forEach { code ->
            assertTrue("کد باید با SYAR شروع شود", code.startsWith("SYAR-"))
            assertTrue("کد باید معتبر شناخته شود", LicenseCodeGenerator.isValidCode(code))
        }

        // Test invalid code rejection
        assertFalse("کد نامعتبر باید رد شود", LicenseCodeGenerator.isValidCode("FAKE-CODE-1234"))
        assertEquals("ارزش هر لایسنس باید ۹ میلیون تومان باشد", 9_000_000L, LicenseCodeGenerator.LICENSE_PRICE_TOMAN)
        assertEquals("مدت لایسنس باید ۳۶۵ روز باشد", 365, LicenseCodeGenerator.LICENSE_DURATION_DAYS)
    }

    @Test
    fun testHesitantClientAnalysis() {
        // Test all face shapes and anatomy combinations
        FaceShape.values().forEach { face ->
            BodyAnatomy.values().forEach { body ->
                SkinTone.values().forEach { skin ->
                    val rec = HesitantClientEngine.analyzeAndRecommend(face, body, skin)
                    assertNotNull("شینیون پیشنهادی نباید خالی باشد", rec.recommendedHairstyle)
                    assertNotNull("میکاپ پیشنهادی نباید خالی باشد", rec.recommendedMakeup)
                    assertNotNull("برش لباس نباید خالی باشد", rec.recommendedDressCut)
                    assertNotNull("تاج پیشنهادی نباید خالی باشد", rec.recommendedCrownType)
                    assertTrue("درصد تطابق باید بالای ۹۰ باشد", rec.matchPercentage in 90..100)
                    assertTrue("فرمول بستن قرارداد باید موجود باشد", rec.psychologicalCloserTip.contains("قرارداد"))
                }
            }
        }
    }

    @Test
    fun testWardrobeCatalogCounts() {
        // CORE_FEATURE_5 requirement: Exactly 50 bridal gowns and 35 groom suits
        val gowns = WardrobeCatalog.BRIDAL_50_GOWNS
        assertEquals("تعداد لباس عروس ترند باید ۵۰ مدل باشد", 50, gowns.size)
        assertEquals("شناسه لباس‌ها باید یکتا باشد", 50, gowns.map { it.id }.distinct().size)

        val suits = WardrobeCatalog.GROOM_35_SUITS
        assertEquals("تعداد کت‌شلوار داماد ترند باید ۳۵ مدل باشد", 35, suits.size)
        assertEquals("شناسه کت‌شلوارها باید یکتا باشد", 35, suits.map { it.id }.distinct().size)
    }

    @Test
    fun testDreamScenarios() {
        // CORE_FEATURE_5 requirement: 3 dream scenarios
        val scenarios = DreamScenario.values()
        assertEquals("باید ۳ سناریوی رویایی وجود داشته باشد", 3, scenarios.size)
        assertTrue("سناریوی رقص وسط سن باید وجود داشته باشد", scenarios.any { it.id == "dance_stage" })
        assertTrue("سناریوی لحظه ورود باید وجود داشته باشد", scenarios.any { it.id == "grand_entrance" })
        assertTrue("سناریوی کنار ماشین عروس باید وجود داشته باشد", scenarios.any { it.id == "wedding_car" })
    }

    @Test
    fun testPrivacyMasksAndCrownCatalog() {
        // CORE_FEATURE_3: Gulf & Arabic niqab with rose, masquerade masks
        val masks = PrivacyMaskType.values()
        assertTrue("نقاب خلیجی با گل رز طلایی باید موجود باشد", masks.any { it.id == "gulf_rose_gold" })
        assertTrue("نقاب عربی با رز قرمز باید موجود باشد", masks.any { it.id == "arabic_black_rose" })
        assertTrue("ماسک بالماسکه ونیزی باید موجود باشد", masks.any { it.id == "venetian_masquerade" })
        assertTrue("روبنده توری فرانسوی باید موجود باشد", masks.any { it.id == "french_lace_veil" })

        // CORE_FEATURE_4: Bridal crown fitting catalog
        val crowns = BridalCrownCatalog.CROWNS
        assertTrue("تاج‌ها باید حداقل ۵ مدل باشند", crowns.size >= 5)
        crowns.forEach { crown ->
            assertTrue("ارتفاع و مقیاس باید معتبر باشد", crown.defaultScale > 0f)
        }
    }

    @Test
    fun testAiVoicePersonasAndSiavashAlimiriSpeech() {
        val personas = AiVoicePersona.values()
        assertEquals("باید ۴ پرسونا صوتی هوش مصنوعی وجود داشته باشد", 4, personas.size)
        assertTrue("صدای استاد سیاوش علیمیری باید موجود باشد", personas.any { it.id == "siavash_alimiri" })
        assertTrue("صدای هوش مصنوعی متابورس توانا باید موجود باشد", personas.any { it.id == "tavana_metaverse_ai" })

        val speech = SiavashAlimiriSpeech.FULL_SPEECH_TEXT
        assertTrue("سخنرانی باید شامل نام سیاوش علی‌میری باشد", speech.contains("سیاوش علی‌میری"))
        assertTrue("سخنرانی باید شامل شهر متابورسی توانا باشد", speech.contains("توانا"))
        assertTrue("سخنرانی باید شامل سالن‌یار باشد", speech.contains("سالن‌یار"))
        assertTrue("سخنرانی باید شامل لایسنس ۹ میلیون تومانی باشد", speech.contains("۹ میلیون"))
    }

    @Test
    fun testStoryMakerDraftAndStickers() {
        val draft = StoryDraft(
            template = StoryTemplate.BRIDAL_VIP,
            activeStickers = setOf(StoryInteractiveSticker.POLL_RATING, StoryInteractiveSticker.BOOKING_CTA)
        )
        assertEquals("قالب باید BRIDAL_VIP باشد", StoryTemplate.BRIDAL_VIP, draft.template)
        assertEquals("تعداد استیکرها باید ۲ باشد", 2, draft.activeStickers.size)

        // Verify all 4 story templates exist
        assertEquals("باید ۴ قالب استوری وجود داشته باشد", 4, StoryTemplate.values().size)
    }
}
