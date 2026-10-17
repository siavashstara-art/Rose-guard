package com.example.model

/**
 * Data models for the Recharts-inspired Financial & Commission Analytical Dashboard.
 * Models comparative figures between:
 * 1. هزینه‌های سالن‌های زیبایی (Salon Operating & Marketing Costs)
 * 2. پورسانت‌های ویزیتورها (Field Visitor Sales Commissions)
 * 3. درآمدهای حاصل از ارجاع تالارها و باغ‌عمارت‌ها (Ballroom Revenues)
 * 4. درآمدهای حاصل از ارجاع مزون‌های لباس عروس و داماد (Bridal Boutique Revenues)
 */
data class MonthlyFinancialPoint(
    val monthName: String,
    val shortName: String,
    val salonCostsMillionToman: Double,
    val visitorCommissionsMillionToman: Double,
    val ballroomRevenuesMillionToman: Double,
    val maisonRevenuesMillionToman: Double
) {
    val totalRevenueMillionToman: Double
        get() = ballroomRevenuesMillionToman + maisonRevenuesMillionToman

    val netProfitMillionToman: Double
        get() = totalRevenueMillionToman - (salonCostsMillionToman + visitorCommissionsMillionToman)
}

data class FinancialKpiSummary(
    val totalBallroomRevenueMillion: Double,
    val totalMaisonRevenueMillion: Double,
    val totalVisitorCommissionMillion: Double,
    val totalSalonCostsMillion: Double,
    val totalNetProfitMillion: Double,
    val roiPercentage: Int,
    val totalReferredBridesCount: Int
)

enum class AnalyticsTimeframe(val titlePersian: String) {
    MONTHLY("ماهانه (۶ ماهه اخیر)"),
    QUARTERLY("فصلی (چهار فصل)"),
    ANNUAL("روند سالانه")
}

data class RevenueDistributionSlice(
    val title: String,
    val percentage: Float,
    val amountMillionToman: Double,
    val colorHex: Long
)

object FinancialAnalyticsCatalog {

    val MONTHLY_DATA = listOf(
        MonthlyFinancialPoint(
            monthName = "فروردین",
            shortName = "فروردین",
            salonCostsMillionToman = 18.5,
            visitorCommissionsMillionToman = 12.0,
            ballroomRevenuesMillionToman = 45.0,
            maisonRevenuesMillionToman = 28.0
        ),
        MonthlyFinancialPoint(
            monthName = "اردیبهشت",
            shortName = "اردیبهشت",
            salonCostsMillionToman = 22.0,
            visitorCommissionsMillionToman = 16.5,
            ballroomRevenuesMillionToman = 62.0,
            maisonRevenuesMillionToman = 38.0
        ),
        MonthlyFinancialPoint(
            monthName = "خرداد",
            shortName = "خرداد",
            salonCostsMillionToman = 25.0,
            visitorCommissionsMillionToman = 19.0,
            ballroomRevenuesMillionToman = 78.0,
            maisonRevenuesMillionToman = 46.0
        ),
        MonthlyFinancialPoint(
            monthName = "تیر (پیک عروسی)",
            shortName = "تیر",
            salonCostsMillionToman = 28.0,
            visitorCommissionsMillionToman = 24.5,
            ballroomRevenuesMillionToman = 98.0,
            maisonRevenuesMillionToman = 58.0
        ),
        MonthlyFinancialPoint(
            monthName = "مرداد",
            shortName = "مرداد",
            salonCostsMillionToman = 26.5,
            visitorCommissionsMillionToman = 21.0,
            ballroomRevenuesMillionToman = 86.0,
            maisonRevenuesMillionToman = 52.0
        ),
        MonthlyFinancialPoint(
            monthName = "شهریور",
            shortName = "شهریور",
            salonCostsMillionToman = 24.0,
            visitorCommissionsMillionToman = 18.5,
            ballroomRevenuesMillionToman = 82.0,
            maisonRevenuesMillionToman = 48.0
        )
    )

    val QUARTERLY_DATA = listOf(
        MonthlyFinancialPoint(
            monthName = "بهار ۱۴۰۵",
            shortName = "بهار",
            salonCostsMillionToman = 65.5,
            visitorCommissionsMillionToman = 47.5,
            ballroomRevenuesMillionToman = 185.0,
            maisonRevenuesMillionToman = 112.0
        ),
        MonthlyFinancialPoint(
            monthName = "تابستان ۱۴۰۵ (فصل طلایی)",
            shortName = "تابستان",
            salonCostsMillionToman = 78.5,
            visitorCommissionsMillionToman = 64.0,
            ballroomRevenuesMillionToman = 266.0,
            maisonRevenuesMillionToman = 158.0
        ),
        MonthlyFinancialPoint(
            monthName = "پاییز ۱۴۰۵",
            shortName = "پاییز",
            salonCostsMillionToman = 58.0,
            visitorCommissionsMillionToman = 42.0,
            ballroomRevenuesMillionToman = 172.0,
            maisonRevenuesMillionToman = 98.0
        ),
        MonthlyFinancialPoint(
            monthName = "زمستان ۱۴۰۵",
            shortName = "زمستان",
            salonCostsMillionToman = 62.0,
            visitorCommissionsMillionToman = 46.0,
            ballroomRevenuesMillionToman = 188.0,
            maisonRevenuesMillionToman = 106.0
        )
    )

    val ANNUAL_DATA = listOf(
        MonthlyFinancialPoint(
            monthName = "سال ۱۴۰۲ (راه‌اندازی)",
            shortName = "۱۴۰۲",
            salonCostsMillionToman = 120.0,
            visitorCommissionsMillionToman = 75.0,
            ballroomRevenuesMillionToman = 280.0,
            maisonRevenuesMillionToman = 160.0
        ),
        MonthlyFinancialPoint(
            monthName = "سال ۱۴۰۳ (توسعه شبکه)",
            shortName = "۱۴۰۳",
            salonCostsMillionToman = 180.0,
            visitorCommissionsMillionToman = 135.0,
            ballroomRevenuesMillionToman = 490.0,
            maisonRevenuesMillionToman = 310.0
        ),
        MonthlyFinancialPoint(
            monthName = "سال ۱۴۰۴ (جهش سالن‌یار)",
            shortName = "۱۴۰۴",
            salonCostsMillionToman = 230.0,
            visitorCommissionsMillionToman = 185.0,
            ballroomRevenuesMillionToman = 710.0,
            maisonRevenuesMillionToman = 440.0
        ),
        MonthlyFinancialPoint(
            monthName = "سال ۱۴۰۵ (پیش‌بینی هوشمند)",
            shortName = "۱۴۰۵",
            salonCostsMillionToman = 264.0,
            visitorCommissionsMillionToman = 199.5,
            ballroomRevenuesMillionToman = 811.0,
            maisonRevenuesMillionToman = 474.0
        )
    )

    val REVENUE_SLICES = listOf(
        RevenueDistributionSlice("تالارها و باغ‌عمارت‌های عروسی", 48f, 451.0, 0xFF4ADE80),
        RevenueDistributionSlice("مزون‌های لباس عروس و کت‌شلوار", 34f, 270.0, 0xFFECC34C),
        RevenueDistributionSlice("پورسانت و خدمات VIP سالن", 18f, 144.0, 0xFFA855F7)
    )

    fun calculateKpiSummary(data: List<MonthlyFinancialPoint>): FinancialKpiSummary {
        val totalBallroom = data.sumOf { it.ballroomRevenuesMillionToman }
        val totalMaison = data.sumOf { it.maisonRevenuesMillionToman }
        val totalVisitor = data.sumOf { it.visitorCommissionsMillionToman }
        val totalCosts = data.sumOf { it.salonCostsMillionToman }
        val totalRevenue = totalBallroom + totalMaison
        val totalNet = totalRevenue - (totalCosts + totalVisitor)
        val roi = if (totalCosts + totalVisitor > 0) {
            ((totalNet / (totalCosts + totalVisitor)) * 100).toInt()
        } else 0

        return FinancialKpiSummary(
            totalBallroomRevenueMillion = totalBallroom,
            totalMaisonRevenueMillion = totalMaison,
            totalVisitorCommissionMillion = totalVisitor,
            totalSalonCostsMillion = totalCosts,
            totalNetProfitMillion = totalNet,
            roiPercentage = roi,
            totalReferredBridesCount = (totalRevenue / 18.0).toInt()
        )
    }
}
