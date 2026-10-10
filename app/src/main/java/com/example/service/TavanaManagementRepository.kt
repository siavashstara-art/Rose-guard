package com.example.service

import com.example.model.CommercialPartnershipRecord
import com.example.model.FinancialTransaction
import com.example.model.VisitorAccount

data class SalonDossier(
    val salonId: String,
    val salonName: String,
    val contactPerson: String,
    val phone: String,
    val address: String,
    val contractStartDate: String,
    val contractEndDate: String,
    val contractStatus: String, // "فعال", "در انتظار تمدید", "لغوشده"
    val assignedVisitorName: String,
    val visitorCommissionPercent: Double,
    val renewalHistory: List<String>
)

class TavanaManagementRepository {

    fun getSalonDossiers(): List<SalonDossier> {
        return listOf(
            SalonDossier(
                salonId = "salon_001",
                salonName = "سالن زیبایی نگین رویال",
                contactPerson = "سرکار خانم الهام کریمی",
                phone = "۰۹۱۲۳۴۵۶۷۸۹",
                address = "تهران، سعادت‌آباد، میدان کاج",
                contractStartDate = "۱۴۰۳/۰۱/۱۵",
                contractEndDate = "۱۴۰۴/۰۱/۱۵",
                contractStatus = "فعال",
                assignedVisitorName = "علیرضا رادمنش (کد ویزیتور ۱۰۱)",
                visitorCommissionPercent = 20.0,
                renewalHistory = listOf("ثبت قرارداد اولیه سالانه در ۱۴۰۳/۰۱/۱۵", "ارتقا به پکیج استودیوی هوشمند در ۱۴۰۳/۰۴/۲۰")
            ),
            SalonDossier(
                salonId = "salon_002",
                salonName = "سالن تخصصی عروس گل سرخ",
                contactPerson = "سرکار خانم مریم صادقی",
                phone = "۰۹۱۹۸۷۶۵۴۳۲",
                address = "تهران، فرمانیه، خیابان جهانبخش‌نژاد",
                contractStartDate = "۱۴۰۲/۰۶/۱۰",
                contractEndDate = "۱۴۰۳/۰۶/۱۰",
                contractStatus = "در انتظار تمدید",
                assignedVisitorName = "سارا احمدی (کد ویزیتور ۱۰۲)",
                visitorCommissionPercent = 25.0,
                renewalHistory = listOf("قرارداد اولیه در ۱۴۰۲/۰۶/۱۰", "ثبت الحاقیه همکاری با مزون عروس در ۱۴۰۲/۱۱/۰۱")
            ),
            SalonDossier(
                salonId = "salon_003",
                salonName = "سالن زیبایی و عروس پرنسس",
                contactPerson = "خانم نیلوفر فرهمند",
                phone = "۰۹۱۲۹۹۸۸۷۷۶",
                address = "تهران، شهرک غرب، بلوار دادمان",
                contractStartDate = "۱۴۰۳/۰۵/۰۱",
                contractEndDate = "۱۴۰۴/۰۵/۰۱",
                contractStatus = "فعال",
                assignedVisitorName = "علیرضا رادمنش (کد ویزیتور ۱۰۱)",
                visitorCommissionPercent = 20.0,
                renewalHistory = listOf("ثبت قرارداد با تخفیف افتتاحیه سالنیار")
            )
        )
    }

    fun getVisitorAccounts(): List<VisitorAccount> {
        return listOf(
            VisitorAccount(
                id = "vis_101",
                name = "علیرضا رادمنش",
                phone = "۰۹۱۲۱۱۱۱۱۱۱",
                assignedSalonsCount = 12,
                closedContractsCount = 9,
                commissionRatePercent = 20.0,
                totalEarnedCommissionToman = 18_000_000L,
                paidCommissionToman = 14_000_000L,
                remainingCommissionToman = 4_000_000L
            ),
            VisitorAccount(
                id = "vis_102",
                name = "سارا احمدی",
                phone = "۰۹۱۲۲۲۲۲۲۲۲",
                assignedSalonsCount = 15,
                closedContractsCount = 11,
                commissionRatePercent = 25.0,
                totalEarnedCommissionToman = 24_500_000L,
                paidCommissionToman = 20_000_000L,
                remainingCommissionToman = 4_500_000L
            )
        )
    }

    fun getCommercialPartnerships(): List<CommercialPartnershipRecord> {
        return listOf(
            CommercialPartnershipRecord(
                id = "cp_001",
                proposalType = "معرفی عروس به مزون همکار",
                sourcePartner = "سالن زیبایی نگین رویال",
                targetPartner = "مزون عروس رویال کوئین",
                contractType = "کمیسیون متقابل معرفی مشتری",
                commissionPercent = 15.0,
                commissionAmountToman = 3_750_000L,
                receivableToman = 3_750_000L,
                payableToman = 0L,
                settledAmountToman = 3_750_000L,
                balanceDueToman = 0L,
                isSettled = true,
                date = "۱۴۰۳/۰۷/۰۲",
                notes = "معرفی پکیج کامل عروس مهرماه؛ تسویه کامل انجام شد."
            ),
            CommercialPartnershipRecord(
                id = "cp_002",
                proposalType = "ساخت تیزر تبلیغاتی و استوری‌سازی",
                sourcePartner = "استودیوی توانا (TAVANA)",
                targetPartner = "سالن تخصصی گل سرخ",
                contractType = "خدمات تولید محتوای ویدیویی",
                commissionPercent = 20.0,
                commissionAmountToman = 2_400_000L,
                receivableToman = 12_000_000L,
                payableToman = 2_400_000L,
                settledAmountToman = 6_000_000L,
                balanceDueToman = 6_000_000L,
                isSettled = false,
                date = "۱۴۰۳/۰۷/۰۸",
                notes = "قسط اول واریز شد؛ قسط دوم پس از تدوین و تایید صحنه‌ها."
            ),
            CommercialPartnershipRecord(
                id = "cp_003",
                proposalType = "اشتراک سالانه ویترین و نوبت‌دهی",
                sourcePartner = "پلتفرم سالنیار",
                targetPartner = "سالن زیبایی و عروس پرنسس",
                contractType = "فروش لایسنس تجاری",
                commissionPercent = 20.0,
                commissionAmountToman = 1_800_000L,
                receivableToman = 9_000_000L,
                payableToman = 1_800_000L,
                settledAmountToman = 9_000_000L,
                balanceDueToman = 0L,
                isSettled = true,
                date = "۱۴۰۳/۰۷/۰۵",
                notes = "تسویه نقدی همراه با تحویل کد فعال‌سازی ۰۰۳."
            ),
            CommercialPartnershipRecord(
                id = "cp_004",
                proposalType = "همکاری با تالار پذیرایی مجلل",
                sourcePartner = "تالار مجلل نیاوران",
                targetPartner = "سالن تخصصی گل سرخ",
                contractType = "تخفیف گروهی و پورسانت معرفی",
                commissionPercent = 10.0,
                commissionAmountToman = 4_200_000L,
                receivableToman = 4_200_000L,
                payableToman = 0L,
                settledAmountToman = 0L,
                balanceDueToman = 4_200_000L,
                isSettled = false,
                date = "۱۴۰۳/۰۷/۱۰",
                notes = "پیشنهاد ثبت‌شده در انتظار برگزاری مراسم آبان‌ماه (مبلغ پیشنهادی)."
            )
        )
    }

    fun getWalletTransactions(): List<FinancialTransaction> {
        return listOf(
            FinancialTransaction(
                id = "tx_101",
                entityType = "SALON",
                entityName = "سالن نگین رویال",
                amountToman = 9_000_000L,
                date = "۱۴۰۳/۰۷/۰۱",
                description = "دریافت وجه بابت تمدید اشتراک سالنیار",
                isSettled = true,
                category = "فروش لایسنس"
            ),
            FinancialTransaction(
                id = "tx_102",
                entityType = "VISITOR",
                entityName = "علیرضا رادمنش",
                amountToman = 1_800_000L,
                date = "۱۴۰۳/۰۷/۰۲",
                description = "پرداخت پورسانت فروش اشتراک سالن نگین رویال",
                isSettled = true,
                category = "تسویه پورسانت"
            ),
            FinancialTransaction(
                id = "tx_103",
                entityType = "PARTNER",
                entityName = "مزون عروس رویال کوئین",
                amountToman = 3_750_000L,
                date = "۱۴۰۳/۰۷/۰۴",
                description = "سهم مشارکت معرفی مشتری عروس سالن نگین رویال",
                isSettled = true,
                category = "کمیسیون شرکا"
            ),
            FinancialTransaction(
                id = "tx_104",
                entityType = "SALON",
                entityName = "سالن گل سرخ",
                amountToman = 6_000_000L,
                date = "۱۴۰۳/۰۷/۰۸",
                description = "قسط اول تولید تیزر و استوری ویدیویی",
                isSettled = true,
                category = "تولید تیزر"
            ),
            FinancialTransaction(
                id = "tx_105",
                entityType = "SALON",
                entityName = "سالن گل سرخ",
                amountToman = 6_000_000L,
                date = "۱۴۰۳/۰۷/۲۵ (پیش‌بینی)",
                description = "قسط دوم تولید تیزر پس از تایید نهایی",
                isSettled = false,
                category = "در انتظار تسویه"
            )
        )
    }
}
