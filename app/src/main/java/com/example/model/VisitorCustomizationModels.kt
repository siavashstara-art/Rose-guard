package com.example.model

/**
 * Salon profile customized on-site by sales visitor.
 */
data class SalonProfile(
    val salonName: String = "سالن زیبایی و عروس گل‌آرا",
    val managerName: String = "خانم سارا ابراهیمی",
    val phoneNumber: String = "0912-345-6789",
    val landlinePhone: String = "021-22004488",
    val address: String = "تهران، سعادت‌آباد، میدان کاج، مجتمع تجاری سرو، طبقه ۳",
    val instagramId: String = "@golara_beauty_vip",
    val telegramId: String = "@golara_bridal",
    val websiteOrBookingLink: String = "https://golara-salon.ir/booking",
    val visitorName: String = "سیاوش علی‌میری (نماینده رسمی توانا)",
    val visitorCode: String = "VST-7089",
    val brandingSlogan: String = "درخشش رویایی شما در به‌یادماندنی‌ترین شب زندگی"
)
