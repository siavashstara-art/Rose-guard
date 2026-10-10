package com.example.model

/**
 * Display aspect ratio for video preview.
 */
enum class VideoAspectRatio(val labelPersian: String, val ratioFloat: Float) {
    PORTRAIT_9_16("عمودی ۹:۱۶ (استوری / ریلز)", 9f / 16f),
    LANDSCAPE_16_9("افقی ۱۶:۹ (یوتیوب / واید)", 16f / 9f)
}

/**
 * Image scale type on video canvas.
 */
enum class ImageFitMode(val labelPersian: String) {
    FIT("نمایش کامل (Fit)"),
    FILL("پر کردن کادر (Fill / Crop)")
}

/**
 * Salon Front Page Template Layouts.
 */
enum class SalonTemplateLayout(val titlePersian: String, val descriptionPersian: String) {
    TOP_HERO("تصویر بزرگ در بالای صفحه", "عکس باشکوه سالن در سربرگ همراه با کارت مشخصات در پایین"),
    HEADER_CARD("لوگو و اطلاعات در سربرگ", "لوگوی رسمی و معرفی مدیریت در بالا و گالری در مرکز"),
    SIDE_BY_SIDE("نمای دو ستونه لوکس", "عکس شاخص سالن در کنار اطلاعات تماس و پذیرش")
}

/**
 * Customizable Salon Profile model for visitor instant showcase.
 */
data class SalonProfile(
    val id: String,
    val salonName: String,
    val managerName: String,
    val address: String,
    val phone: String,
    val workingHours: String,
    val mainPhotoUri: String? = null,
    val bannerPhotoUri: String? = null,
    val logoPhotoUri: String? = null,
    val brandPrimaryColorHex: Long = 0xFFE26A50,
    val selectedTemplate: SalonTemplateLayout = SalonTemplateLayout.TOP_HERO,
    val isCustomApprovedByOwner: Boolean = true
)

/**
 * License status for customer APK allocation.
 */
enum class LicenseStatus(val titlePersian: String, val colorHex: Long) {
    UNISSUED("صادرنشده", 0xFF9E9E9E),
    ALLOCATED("تخصیص‌یافته به سالن", 0xFF0288D1),
    ACTIVE("فعال و معتبر", 0xFF2E7D32),
    EXPIRED("منقضی‌شده", 0xFFE65100),
    REVOKED("لغوشده توسط مدیریت", 0xFFC62828)
}

/**
 * License Feature Tier.
 */
enum class LicenseTier(val titlePersian: String) {
    FREE_PREVIEW("نسخه پیش‌نمایش رایگان ویزیتور"),
    COMMERCIAL_STANDARD("مجوز تجاری استاندارد سالنیار"),
    ENTERPRISE_PRO("مجوز پیشرفته توانا پرو (هوش مصنوعی و خروجی کامل)")
}

/**
 * Commercial Customer License (1 to 200 allocation).
 */
data class SalonLicense(
    val licenseIndex: Int, // 1 to 200 (e.g. 001 to 200)
    val clientSecretKey: String, // un-guessable cryptographic key e.g. TAV-SALON-042-X7K2-9P8Q
    val allocatedSalonId: String? = null,
    val allocatedSalonName: String? = null,
    val status: LicenseStatus = LicenseStatus.UNISSUED,
    val activationDate: String? = null,
    val expirationDate: String? = null,
    val tier: LicenseTier = LicenseTier.FREE_PREVIEW,
    val allowedFeatures: List<String> = listOf("PREVIEW_CUSTOMIZATION")
) {
    val formattedIndex: String
        get() = String.format(java.util.Locale.US, "%03d", licenseIndex)
}

/**
 * Visitor (Sales Rep) Account dossier.
 */
data class VisitorAccount(
    val id: String,
    val name: String,
    val phone: String,
    val assignedSalonsCount: Int,
    val closedContractsCount: Int,
    val commissionRatePercent: Double,
    val totalEarnedCommissionToman: Long,
    val paidCommissionToman: Long,
    val remainingCommissionToman: Long
)

/**
 * Commercial Partnership Tracking record for Salon & TAVANA.
 */
data class CommercialPartnershipRecord(
    val id: String,
    val proposalType: String,
    val sourcePartner: String,
    val targetPartner: String,
    val contractType: String,
    val commissionPercent: Double,
    val commissionAmountToman: Long,
    val receivableToman: Long,
    val payableToman: Long,
    val settledAmountToman: Long,
    val balanceDueToman: Long,
    val isSettled: Boolean,
    val date: String,
    val notes: String
)

/**
 * Internal accounting transaction.
 */
data class FinancialTransaction(
    val id: String,
    val entityType: String, // "VISITOR", "SALON", "PARTNER"
    val entityName: String,
    val amountToman: Long,
    val date: String,
    val description: String,
    val isSettled: Boolean,
    val category: String
)
