package com.example.model

/**
 * Before and After photo comparison and portfolio archive.
 */
data class TransformationRecord(
    val id: String,
    val clientName: String,
    val serviceCategory: String,
    val notes: String,
    val beforeSampleType: String,
    val afterSampleType: String,
    val isPrivacyShieldApplied: Boolean = false,
    val datePersian: String = "۱۴۰۳/۰۷/۲۱"
)

object TransformationArchive {
    val SAMPLE_RECORDS = listOf(
        TransformationRecord(
            id = "TR-101",
            clientName = "عروس VIP (مهسا ک.)",
            serviceCategory = "پکیج کامل عروس و شینیون خطی",
            notes = "کانتورینگ فرم صورت گرد و میکاپ اسموکی اروپایی با درخشش طلایی",
            beforeSampleType = "چهره نچرال قبل از کار و زیرسازی پوست",
            afterSampleType = "نتیجه سوپرلوکس با تاج فرحی و فیکس ۱۸ ساعته",
            isPrivacyShieldApplied = false
        ),
        TransformationRecord(
            id = "TR-102",
            clientName = "میکاپ نامزدی (نگین ص.)",
            serviceCategory = "میکاپ لایت نود و براشینگ مواج",
            notes = "پوشش لک و زیرسازی شیشه‌ای با هایلایتر نچرال و رژ کالباسی",
            beforeSampleType = "پوست طبیعی بدون زیرساز",
            afterSampleType = "جلوه مخملی با جلوه مات و چشمان گربه‌ای",
            isPrivacyShieldApplied = true
        ),
        TransformationRecord(
            id = "TR-103",
            clientName = "عروس مزون (پریناز ر.)",
            serviceCategory = "میکاپ شاین خلیجی و نقاب گل رز",
            notes = "آرایش اشرافی با استفاده از نقاب حفاظتی عربی و مژه‌های ۳D",
            beforeSampleType = "حالت اولیه بدون آرایش",
            afterSampleType = "میکاپ فوق‌حرفه‌ای با روبنده اشرافی رزگلد",
            isPrivacyShieldApplied = true
        )
    )
}
