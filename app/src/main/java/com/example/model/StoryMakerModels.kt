package com.example.model

/**
 * Models and templates for Instagram & Telegram Story Maker (9:16).
 */
enum class StoryTemplate(
    val id: String,
    val titlePersian: String,
    val defaultHeadline: String,
    val defaultSubheadline: String,
    val badgeText: String,
    val primaryColorHex: Long = 0xFFD4AF37,
    val secondaryColorHex: Long = 0xFF1E1029
) {
    BRIDAL_VIP(
        "bridal_vip",
        "استوری VIP عروس و مزون",
        "درخشش بی‌مانند در شب رویایی شما",
        "پکیج فوق‌تخصصی میکاپ عروس، شینیون ژورنالی و پرو تاج سلطنتی",
        "پکیج ویژه عروس ۲۰۲۶"
    ),
    BEFORE_AFTER(
        "before_after",
        "استوری قبل و بعد شگفت‌انگیز",
        "معجزه کانتورینگ و گریم هالیوودی",
        "تغییر چهره فوق‌العاده با حفظ وقار و زیبایی طبیعی بدون روتوش اغراق‌آمیز",
        "تحول قبل و بعد"
    ),
    DISCOUNT_OFFER(
        "discount_offer",
        "استوری رزرو نوبت با آفر ویژه",
        "تخفیف استثنایی قراردادهای این هفته",
        "۱۰٪ تخفیف ویژه برای ۲۰ عروس اول همراه با تست میکاپ و آلبوم رایگان",
        "فرصت محدود رزرو"
    ),
    NEW_SERVICE(
        "new_service",
        "استوری رونمایی از خدمات جدید",
        "جدیدترین تکنیک‌های شاین و خلیجی",
        "اجرای انحصاری تکنیک‌های میکاپ ضدآب ۲۴ ساعته با ماندگاری تضمینی",
        "خدمت انحصاری سالن"
    )
}

enum class StoryInteractiveSticker(
    val id: String,
    val titlePersian: String,
    val visualEmoji: String
) {
    POLL_RATING("poll", "نظرسنجی استوری (عالی بود / فوق‌العاده)", "📊"),
    PRICE_TAG("price", "تگ قیمت و پکیج اقتصادی VIP", "🏷️"),
    BRIDE_SATISFACTION("satisfaction", "نشان رضایت ۱۰۰٪ عروس", "👑"),
    LOCATION_BADGE("location", "لوکیشن سالن زیبایی", "📍"),
    BOOKING_CTA("cta", "برای رزرو نوبت دایرکت دهید", "💬")
}

data class StoryDraft(
    val template: StoryTemplate = StoryTemplate.BRIDAL_VIP,
    val headline: String = StoryTemplate.BRIDAL_VIP.defaultHeadline,
    val subheadline: String = StoryTemplate.BRIDAL_VIP.defaultSubheadline,
    val salonBrandName: String = "سالن زیبایی و عروس گل‌آرا",
    val instagramHandle: String = "@golara_beauty_vip",
    val phoneContact: String = "0912-345-6789",
    val activeStickers: Set<StoryInteractiveSticker> = setOf(
        StoryInteractiveSticker.BRIDE_SATISFACTION,
        StoryInteractiveSticker.BOOKING_CTA
    ),
    val privacyMaskApplied: PrivacyMaskType = PrivacyMaskType.GULF_ROSE_GOLD,
    val filterWarmth: Float = 1.0f
)
