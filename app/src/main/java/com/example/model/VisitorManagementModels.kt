package com.example.model

/**
 * Data models for Visitor Sales Management, Licensing & Ticket System.
 */
data class VisitorSaleRecord(
    val id: String,
    val visitorName: String,
    val licenseCode: String,
    val salonName: String,
    val salonManager: String,
    val salonAddress: String,
    val salonPhone: String,
    val startDateShamsi: String,
    val endDateShamsi: String,
    val daysRemaining: Int,
    val salonPaymentToman: Long,
    val visitorCommissionToman: Long,
    val paymentStatus: String,
    val ticketStatus: String
)

data class SupportTicket(
    val ticketId: String,
    val salonName: String,
    val visitorName: String,
    val subject: String,
    val message: String,
    val priority: String,
    val createdDateShamsi: String,
    val status: String
)

object VisitorManagementCatalog {
    val INITIAL_SALES_RECORDS = listOf(
        VisitorSaleRecord(
            id = "REC-101",
            visitorName = "مهندس سیاوش علی‌میری",
            licenseCode = "SYAR-9M01-A48F-72BC",
            salonName = "سالن زیبایی و عروس گل‌آرا",
            salonManager = "سرکار خانم موسوی",
            salonAddress = "تهران، سعادت‌آباد، میدان کاج، مجتمع سرو، طبقه ۳",
            salonPhone = "09123456789",
            startDateShamsi = "۱۴۰۵/۰۷/۰۱",
            endDateShamsi = "۱۴۰۶/۰۷/۰۱",
            daysRemaining = 352,
            salonPaymentToman = 9_000_000L,
            visitorCommissionToman = 2_700_000L,
            paymentStatus = "پرداخت کامل نقدی (کارت‌خوان)",
            ticketStatus = "فاکتور رسمی صادر شد - فعال"
        ),
        VisitorSaleRecord(
            id = "REC-102",
            visitorName = "مهندس راد (نماینده ارشد فروش)",
            licenseCode = "SYAR-9M02-B83C-91FE",
            salonName = "مجموعه زیبایی بیوتی رویال",
            salonManager = "خانم مهندس افشار",
            salonAddress = "تهران، فرمانیه، خیابان سنبل، پلاک ۱۲",
            salonPhone = "09129876543",
            startDateShamsi = "۱۴۰۵/۰۷/۱۰",
            endDateShamsi = "۱۴۰۶/۰۷/۱۰",
            daysRemaining = 361,
            salonPaymentToman = 9_000_000L,
            visitorCommissionToman = 2_700_000L,
            paymentStatus = "پرداخت آنلاین پایا",
            ticketStatus = "فعال و پشتیبانی VIP"
        ),
        VisitorSaleRecord(
            id = "REC-103",
            visitorName = "آقای صابری (تیم فروش توانا)",
            licenseCode = "SYAR-9M03-C27A-44D1",
            salonName = "خانه عروس شاین لایت",
            salonManager = "خانم کریمی",
            salonAddress = "کرج، مهرشهر، فاز ۱، خیابان ۱۰۰",
            salonPhone = "09351234567",
            startDateShamsi = "۱۴۰۵/۰۶/۲۰",
            endDateShamsi = "۱۴۰۶/۰۶/۲۰",
            daysRemaining = 341,
            salonPaymentToman = 9_000_000L,
            visitorCommissionToman = 2_700_000L,
            paymentStatus = "چک صیادی ۳۰ روزه",
            ticketStatus = "در انتظار پاس چک - نسخه آزمایشی فعال"
        )
    )

    val INITIAL_TICKETS = listOf(
        SupportTicket(
            ticketId = "TCK-401",
            salonName = "سالن زیبایی و عروس گل‌آرا",
            visitorName = "مهندس سیاوش علی‌میری",
            subject = "درخواست معرفی ۵ عروس اول پاییز از تالارهای همکار",
            message = "با سلام، فرم‌های ارجاع عروس‌های معرفی‌شده از تالار قصر سفید ثبت گردید و نوبت‌های میکاپ هماهنگ شد.",
            priority = "فوری",
            createdDateShamsi = "۱۴۰۵/۰۷/۱۲",
            status = "پاسخ داده شده"
        ),
        SupportTicket(
            ticketId = "TCK-402",
            salonName = "مجموعه زیبایی بیوتی رویال",
            visitorName = "مهندس راد",
            subject = "صدور فاکتور و لایسنس تمدید شعبه دوم",
            message = "فاکتور رسمی ۹ میلیون تومانی همراه با شناسه مالیاتی و کمیسیون ویزیتور به تایید رسید.",
            priority = "عادی",
            createdDateShamsi = "۱۴۰۵/۰۷/۱۳",
            status = "در حال بررسی"
        )
    )
}
