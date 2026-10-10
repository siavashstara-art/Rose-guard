package com.example.model

/**
 * Data model for a customer-attracting salon visual tour station.
 * Designed to align with the speech for beauty salon managers (سخنی با مدیران).
 */
data class SalonTourStation(
    val id: String,
    val title: String,
    val subtitle: String,
    val attractionBadge: String,
    val badgeColorHex: Long = 0xFFE26A50,
    val drawableResName: String,
    val recommendedSpeechTopic: String,
    val clientAttractionStat: String,
    val description: String,
    val keyHighlights: List<String>
)

object SalonTourCatalog {

    val STATIONS: List<SalonTourStation> = listOf(
        SalonTourStation(
            id = "tour_reception",
            title = "لابی ورودی و پذیرش مجلل",
            subtitle = "نخستین نقطه تماس و ثبت حس اعتماد و لوکس‌بودن VIP",
            attractionBadge = "۴۲٪ افزایش تمایل به رزرو حضوری",
            badgeColorHex = 0xFFD4AF37, // Gold
            drawableResName = "img_tour_reception",
            recommendedSpeechTopic = "مقدمه، خوش‌آمدگویی و معرفی هویت برند سالن",
            clientAttractionStat = "۹۴٪ رضایت از دیزاین و پذیرایی در ورود",
            description = "کانتر مرمرین قوسی، نورپردازی گرم، مبلمان مخمل و گل‌آرایی طبیعی برای ثبت ماندگارترین تأثیر اولیه بر مشتری جدید.",
            keyHighlights = listOf(
                "کانتر لوکس مرمرین با نورپردازی خطی مخفی",
                "مبلمان پذیرایی مخمل برای مشاوره اولیه",
                "ویترین شیک نمایش محصولات برتر سالن"
            )
        ),
        SalonTourStation(
            id = "tour_bridal",
            title = "سوئیت شاهانه عروس VIP",
            subtitle = "بخش ویژه گریم تخصصی، شینیون و خدمات تشریفات عروس",
            attractionBadge = "پربازده‌ترین لاین درآمدی سالن زیبایی",
            badgeColorHex = 0xFFE26A50, // Peach/Coral
            drawableResName = "img_tour_bridal",
            recommendedSpeechTopic = "خدمات اختصاصی عروس، تشریفات و همکاری با مزون‌ها",
            clientAttractionStat = "۳.۸ برابر میانگین سبد خرید خدمات عادی",
            description = "آینه‌های بزرگ هالیوودی با نور کلوین روز، مانکن لباس عروس، گل‌های ارکیده سفید و فضای خصوصی آرامش‌بخش.",
            keyHighlights = listOf(
                "آینه‌های میکاپ هالیوودی بدون سایه با نور استاندارد",
                "صندلی ارگونومیک VIP با پشتی و جک هیدرولیک",
                "فضای تعویض لباس و استیج عکاسی اختصاصی عروس"
            )
        ),
        SalonTourStation(
            id = "tour_hair",
            title = "لاین تخصصی استایل و خدمات مو",
            subtitle = "استیشن‌های مدرن رنگ، لایت، بالیاژ، کراتین و احیا",
            attractionBadge = "بیشترین نرخ تکرار مراجعات ماهانه (۷۸٪)",
            badgeColorHex = 0xFF8E24AA, // Purple
            drawableResName = "img_tour_hair",
            recommendedSpeechTopic = "خدمات تخصصی مو، نمونه‌کارها و مشاوره تراپی",
            clientAttractionStat = "افزایش ۶۵٪ ماندگاری مشتری با لاین احیا",
            description = "ردیف صندلی‌های چرم راحت با آینه‌های قوسی بک‌لایت و متریال درجه یک برای به تصویر کشیدن حرفه‌ای‌گری پرسنل.",
            keyHighlights = listOf(
                "آینه‌های قوسی مدرن با نور پس‌زمینه بدون خیرگی",
                "بار تخصصی رنگ و محصولات احیای ارگانیک مو",
                "سرشورهای ارگونومیک تخت‌شو همراه با ماساژ"
            )
        ),
        SalonTourStation(
            id = "tour_spa",
            title = "اتاق اسپا، فیشیال و مراقبت پوست",
            subtitle = "واحه آرامش، پاکسازی پوست و ریلکسیشن اختصاصی",
            attractionBadge = "ایجاد وفاداری پایدار و معرفی دهان‌به‌دهان",
            badgeColorHex = 0xFF2E7D32, // Emerald
            drawableResName = "img_tour_spa",
            recommendedSpeechTopic = "مراقبت‌های پوستی، رفع خستگی و ریلکسیشن",
            clientAttractionStat = "۸۸٪ معرفی به دوستان پس از تجربه اسپا",
            description = "محیطی دنج با نور شمع‌های معطر، رایحه اسطوخودوس، موسیقی ملایم و تخت اسپا لوکس برای رهایی کامل از استرس.",
            keyHighlights = listOf(
                "تخت اسپا لوکس و ارگانیک با گرم‌کن هوشمند",
                "دستگاه‌های پیشرفته پاکسازی و آبرسانی عمیق",
                "رایحه‌درمانی (آروماتراپی) و نورپردازی آرامش‌بخش"
            )
        ),
        SalonTourStation(
            id = "tour_nail",
            title = "بار مدرن مانیکور و پدیکور",
            subtitle = "طراحی جذاب ناخن، ژلیش و مراقبت دست و پا با ترندهای روز",
            attractionBadge = "نقطه ورود ۷۰٪ مشتریان جوان به سالن",
            badgeColorHex = 0xFFD81B60, // Rose
            drawableResName = "img_tour_nail",
            recommendedSpeechTopic = "جذب مشتریان جدید، باشگاه مشتریان و ترندهای فصلی",
            clientAttractionStat = "بالاترین اشتراک‌گذاری در استوری اینستاگرام",
            description = "میزهای مرمرین بار ناخن با استند درخشان صدها رنگ لاک، لامپ‌های مدرن برنجی و صندلی‌های پدیکور راحتی.",
            keyHighlights = listOf(
                "استند دکوراتیو دیواری صدها رنگ لاک متنوع",
                "صندلی‌های پدیکور با وان آب‌گرم ماساژور و نور ملایم",
                "نورپردازی دقیق متمرکز برای طراحی ظریف ناخن"
            )
        )
    )

    /**
     * Complete sample speech for "سخنی با مدیران ۳۶: تور سالن زیبایی و جذب مشتریان"
     */
    val SPEECH_36_FULL_TEXT = """
سلام و درود به همه مدیران توانمند سالن‌های زیبایی، مزون‌ها و فعالان کارآفرین این حوزه.
امروز در قسمت ۳۶ از سلسله سخنان با مدیران، می‌خواهیم به مهم‌ترین راز جذب و حفظ مشتری بپردازیم: تور تصویری جذاب و بازاریابی حسی در سالن زیبایی.
اولین ثانیه‌های ورود مشتری تعیین‌کننده است؛ لابی و پذیرش لوکس سالن با نورپردازی گرم، لبخند پرسنل و عطر دلپذیر، اعتماد اولیه را در همان بدو ورود ایجاد می‌کند.
در بخش استایل مو و رنگ، مشتری باید حرفه‌ای‌گری، ابزار به‌روز و آینه‌های مدرن شما را ببیند تا با خیالی آسوده موهایش را به شما بسپارد.
سوئیت اختصاصی عروس VIP اوج شکوه و هویت سالن شماست؛ اینجاست که مراجعین احساس یک ملکه را پیدا می‌کنند و بیشترین ارزش افزوده تجاری شکل می‌گیرد.
اتاق اسپا و پاکسازی پوست، پناهگاهی برای ریلکسیشن و رهایی از استرس روزمره است که رضایت عمیق و وفاداری دائمی مشتریان را تضمین می‌کند.
بار تخصصی مانیکور و پدیکور با رنگ‌بندی‌های چشم‌نواز، بهترین بهانه برای دیدارهای هفتگی و بیشترین اشتراک‌گذاری در شبکه‌های اجتماعی است.
سامانه سالنیار و استودیو توانا به شما این امکان را می‌دهد که این فضای دلنشین را به یک تیزر تصویری حرفه‌ای تبدیل کنید و نوبت‌های سالن را ماه‌ها قبل پر نمایید.
    """.trimIndent()
}
