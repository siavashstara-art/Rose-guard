package com.example.model

enum class ReferralDirection(val titlePersian: String, val badgeColorHex: Long) {
    PARTNER_TO_SALON("از تالار / مزون به آرایشگاه (ورودی سالن)", 0xFF4ADE80),
    SALON_TO_PARTNER("از آرایشگاه به تالار / مزون (خروجی سالن)", 0xFF38BDF8)
}

enum class ReferralPartnerType(val titlePersian: String) {
    WEDDING_HALL("تالار و باغ‌عمارت مجلل"),
    BRIDAL_MAISON("مزون لباس عروس"),
    GROOM_SUIT_MAISON("مزون کت‌شلوار داماد"),
    PHOTO_STUDIO("عمارت و آتلیه عکاسی")
}

enum class ReferralStage(val titlePersian: String, val colorHex: Long) {
    SUBMITTED("معرفی ثبت شد", 0xFF93C5FD),
    CONTACTED("تماس و ارسال بن تخفیف", 0xFFFDE047),
    CONTRACT_FINALIZED("عقد قرارداد قطعی (پرداخت بیعانه)", 0xFF4ADE80),
    COMMISSION_SETTLED("تسویه نقدی سهم ۵۰٪ طرفین", 0xFF10B981)
}

data class TwoWayBridalReferral(
    val id: String,
    val direction: ReferralDirection,
    val brideGroomName: String,
    val contactPhone: String,
    val weddingDateShamsi: String,
    val partnerType: ReferralPartnerType,
    val partnerName: String,
    val salonName: String,
    val totalContractBudgetToman: Long,
    val exclusiveBrideDiscountPercent: Int, // درصد تخفیف ویژه جهت ترغیب عروس و داماد
    val brideDiscountAmountToman: Long,      // مبلغ تخفیف ریالی هدیه به عروس
    val totalCommissionToman: Long,
    val platformShareToman: Long,            // سهم ۵۰٪ پلتفرم
    val partnerOrSalonShareToman: Long,      // سهم ۵۰٪ نقدی معرف
    val voucherCode: String,                 // کد ووچر اختصاصی تخفیف
    val stage: ReferralStage,
    val description: String
)

object TwoWayReferralCatalog {
    val INITIAL_TWO_WAY_REFERRALS = listOf(
        TwoWayBridalReferral(
            id = "REF-2WAY-01",
            direction = ReferralDirection.PARTNER_TO_SALON,
            brideGroomName = "سرکار خانم یگانه سهرابی (عروس)",
            contactPhone = "09121112233",
            weddingDateShamsi = "۱۴۰۵/۰۸/۱۵",
            partnerType = ReferralPartnerType.WEDDING_HALL,
            partnerName = "باغ‌عمارت قصر فیروزه (گرمدره)",
            salonName = "سالن زیبایی و عروس گل‌آرا",
            totalContractBudgetToman = 26_000_000L,
            exclusiveBrideDiscountPercent = 15,
            brideDiscountAmountToman = 3_900_000L,
            totalCommissionToman = 3_000_000L,
            platformShareToman = 1_500_000L,
            partnerOrSalonShareToman = 1_500_000L,
            voucherCode = "VIP-GLR-9921",
            stage = ReferralStage.CONTRACT_FINALIZED,
            description = "معرفی از طرف باغ‌عمارت قصر فیروزه. بن تخفیف ۱۵٪ (۳.۹ میلیون تومان) به عروس تعلق گرفت که منجر به عقد آنی قرارداد شد. سهم ۵۰٪ تالاردار واریز شد."
        ),
        TwoWayBridalReferral(
            id = "REF-2WAY-02",
            direction = ReferralDirection.SALON_TO_PARTNER,
            brideGroomName = "دوشیزه پریا کریمی و آقای رادمنش (عروس و داماد)",
            contactPhone = "09358889900",
            weddingDateShamsi = "۱۴۰۵/۰۹/۱۰",
            partnerType = ReferralPartnerType.BRIDAL_MAISON,
            partnerName = "مزون تخصصی لباس عروس و داماد پرنسس رویال",
            salonName = "سالن زیبایی و عروس گل‌آرا",
            totalContractBudgetToman = 34_000_000L,
            exclusiveBrideDiscountPercent = 12,
            brideDiscountAmountToman = 4_080_000L,
            totalCommissionToman = 4_000_000L,
            platformShareToman = 2_000_000L,
            partnerOrSalonShareToman = 2_000_000L,
            voucherCode = "VIP-MSN-4412",
            stage = ReferralStage.COMMISSION_SETTLED,
            description = "عروس سالن تالار و لباس نگرفته بود؛ با دریافت بن تخفیف ۱۲٪ به مزون مراجعه کرد و قرارداد بست. مبلغ ۲ میلیون تومان نقداً به سالن زیبایی پرداخت شد."
        ),
        TwoWayBridalReferral(
            id = "REF-2WAY-03",
            direction = ReferralDirection.SALON_TO_PARTNER,
            brideGroomName = "خانم مهندس بیات (عروس)",
            contactPhone = "09123334455",
            weddingDateShamsi = "۱۴۰۵/۰۹/۲۵",
            partnerType = ReferralPartnerType.WEDDING_HALL,
            partnerName = "تالار و تشریفات دیپلمات (فرمانیه)",
            salonName = "مجموعه زیبایی بیوتی رویال",
            totalContractBudgetToman = 80_000_000L,
            exclusiveBrideDiscountPercent = 10,
            brideDiscountAmountToman = 8_000_000L,
            totalCommissionToman = 8_000_000L,
            platformShareToman = 4_000_000L,
            partnerOrSalonShareToman = 4_000_000L,
            voucherCode = "VIP-HAL-8830",
            stage = ReferralStage.CONTACTED,
            description = "ووچر ۸ میلیون تومانی تخفیف منو تالار برای عروس پیامک شد. پس از تسویه قرارداد، ۴ میلیون تومان سهم سالن واریز می‌گردد."
        ),
        TwoWayBridalReferral(
            id = "REF-2WAY-04",
            direction = ReferralDirection.PARTNER_TO_SALON,
            brideGroomName = "سرکار خانم راد (عروس)",
            contactPhone = "09127778899",
            weddingDateShamsi = "۱۴۰۵/۱۰/۰۵",
            partnerType = ReferralPartnerType.GROOM_SUIT_MAISON,
            partnerName = "مزون تخصصی کت‌شلوار داماد الگانس",
            salonName = "سالن زیبایی و عروس گل‌آرا",
            totalContractBudgetToman = 22_000_000L,
            exclusiveBrideDiscountPercent = 15,
            brideDiscountAmountToman = 3_300_000L,
            totalCommissionToman = 2_400_000L,
            platformShareToman = 1_200_000L,
            partnerOrSalonShareToman = 1_200_000L,
            voucherCode = "VIP-GLR-1025",
            stage = ReferralStage.SUBMITTED,
            description = "بن تخفیف ویژه سالن گل‌آرا در مزون کت‌شلوار به داماد تحویل شد تا عروس برای جلسه تست و عقد قرارداد مراجعه کند."
        )
    )
}
