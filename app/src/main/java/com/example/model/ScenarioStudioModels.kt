package com.example.model

/**
 * Dream Wedding Scenarios and 50 Bridal Gowns + 35 Groom Suits catalog.
 */
enum class DreamScenario(
    val id: String,
    val titlePersian: String,
    val subtitlePersian: String,
    val ambientDescription: String,
    val lightingMood: String
) {
    DANCE_STAGE(
        "dance_stage",
        "۱. رقص رویایی در وسط سن تالار",
        "در میان تشویق و نگاه تحسین‌آمیز مهمانان و مه‌سرد",
        "نورپردازی اسپات‌لایت متمرکز، رقص ملایم دونفره، تشویق ایستاده خانواده‌ها و مهمانان، باران گلبرگ‌های سفید",
        "اسپات‌لایت طلایی-نقره‌ای با افکت بوکه و درخشش کریستال‌های لوستر"
    ),
    GRAND_ENTRANCE(
        "grand_entrance",
        "۲. لحظه ورود به سالن و استقبال مهمانان",
        "ورود باشکوه روی فرش قرمز با موزیک ورود و دست‌زدن حضار",
        "فرش قرمز تالار، آتش‌بازی سرد تشریفاتی در دو طرف مسیر، لبخند پرغرور عروس و داماد در بدرقه نگاه‌ها",
        "نور گرم و پرانرژی با فلش‌های دوربین عکاسان و چلچراغ‌های سالن"
    ),
    WEDDING_CAR(
        "wedding_car",
        "۳. در کنار ماشین عروس لوکس و تزئین‌شده",
        "ماشین کلاسیک گل‌آرایی‌شده با همان لباس‌های انتخابی نرم‌افزار",
        "ماشین بنز یا پورشه یا فورد کلاسیک با دیزاین گل ارکیده و رز سفید، ایستادن عاشقانه دست‌دردست زوجین",
        "نور ملایم غروب رمانتیک (Golden Hour) و انعکاس‌های بدنه صیقلی ماشین"
    )
}

data class BridalGownModel(
    val id: String,
    val namePersian: String,
    val styleCategory: String,
    val fabricType: String,
    val trendYear: String = "۲۰۲۶",
    val maisonName: String = "مزون تخصصی لباس عروس پرنسس رویال (الهیه)",
    val maisonAddress: String = "تهران، الهیه، خیابان فرشته، برج پالادیوم، پلاک ۴",
    val maisonPhone: String = "02122001122",
    val maisonInstagram: String = "@royal_princess_maison",
    val slotRentalFeeToman: Long = 5_000_000L,
    val rentalPriceToman: Long = 18_000_000L,
    val referralDiscountPercent: Int = 15
)

data class GroomSuitModel(
    val id: String,
    val namePersian: String,
    val cutStyle: String,
    val colorTone: String,
    val trendYear: String = "۲۰۲۶",
    val maisonName: String = "شو‌روم تخصصی کت‌شلوار داماد الگانس (زعفرانیه)",
    val maisonAddress: String = "تهران، زعفرانیه، مرکز خرید پالاس، طبقه ۲",
    val maisonPhone: String = "02122003344",
    val maisonInstagram: String = "@elegance_groom_suits",
    val slotRentalFeeToman: Long = 4_000_000L,
    val rentalPriceToman: Long = 12_000_000L,
    val referralDiscountPercent: Int = 12
)

object WardrobeCatalog {
    // 50 Trend Bridal Gown Models
    val BRIDAL_50_GOWNS: List<BridalGownModel> by lazy {
        val categories = listOf(
            "پف‌دار سوپرپرنسسی (Ballgown)",
            "دم‌ماهی اشرافی (Mermaid)",
            "اسکارلت سینمایی (Scarlet A-Line)",
            "ساتن آمریکایی مینیمال (Royal Satin)",
            "گیپور و دانتل فرانسوی (French Lace)",
            "بوهو رمانتیک و آستین توری (Boho Romantic)",
            "شنل‌دار و دنباله‌دار سلطنتی (Royal Train)",
            "شاین و کریستال‌دوزی شب‌نما (Glitter Glam)",
            "یقه دلبری و آف‌شولدر مدرن (Off-Shoulder)",
            "پرهای شترمرغ و ابریشم دست‌دوز (Couture Luxury)"
        )
        val list = mutableListOf<BridalGownModel>()
        var count = 1
        for (cat in categories) {
            for (sub in 1..5) {
                val code = "BG-${count.toString().padStart(2, '0')}"
                val title = when (sub) {
                    1 -> "مدل پلاتینیوم $cat طرح لوتوس"
                    2 -> "مدل رویال $cat با مرواریددوزی برجسته"
                    3 -> "مدل الیزابت $cat با دامن طبقاتی ارگانزا"
                    4 -> "مدل کریستینا $cat با کریستال‌های سواروسکی"
                    else -> "مدل پاریسی $cat دنباله ۳ متری ابریشمی"
                }
                val fabric = when (sub) {
                    1 -> "ساتن میسادو و تور ابریشم فرانسه"
                    2 -> "دانتل کارشده با منجوق و مروارید طبیعی"
                    3 -> "ارگانزای شاین ۳D با لایه کرینولین"
                    4 -> "تور ژپون‌دار فنردوزی مزونی"
                    else -> "ابریشم شانتون با نگین‌های اتریشی"
                }
                list.add(BridalGownModel(code, title, cat, fabric))
                count++
            }
        }
        list
    }

    // 35 Trend Groom Suit Models
    val GROOM_35_SUITS: List<GroomSuitModel> by lazy {
        val styles = listOf(
            "تاکسیدو یقه شال ساتن (Black Tuxedo)" to "مشکی اعلا ایتالیایی",
            "کت‌شلوار دیپلمات اسلیم‌فیت (Diplomat Slim)" to "دودی و نوک‌مدادی براق",
            "دبل‌برست شش‌دکمه کلاسیک (Double-Breasted)" to "سرمه‌ای سیر رویال",
            "تاکسیدو ژاکارد با نقش برجسته (Jacquard Tuxedo)" to "مشکی با موتیف زرین",
            "کت تک شیری با شلوار مشکی (Off-White Dinner)" to "شیری عاجی و مشکی تضادی",
            "سه تکه جلیقه‌دار پرنس ولز (Prince of Wales)" to "طوسی روشن با خطوط محو",
            "مخمل اشرافی شب (Velvet Evening)" to "زرشکی سوپرلوکس و سرمه‌ای مخمل"
        )
        val list = mutableListOf<GroomSuitModel>()
        var count = 1
        for ((style, defaultColor) in styles) {
            for (v in 1..5) {
                val code = "GS-${count.toString().padStart(2, '0')}"
                val name = when (v) {
                    1 -> "کت‌شلوار $style استایل میلان"
                    2 -> "کت‌شلوار $style سفارشی ناپولی"
                    3 -> "کت‌شلوار $style برش ساویل رو لندن"
                    4 -> "کت‌شلوار $style طرح دیور با دوخت دست‌دوز"
                    else -> "کت‌شلوار $style دوخت اختصاصی پکیج داماد VIP"
                }
                val color = when (v) {
                    1 -> defaultColor
                    2 -> "مشکی زغالی با پیراهن کراوات مخمل"
                    3 -> "سورمه‌ای اقیانوسی با دکمه‌سردست طلا"
                    4 -> "طوسی گرانیتی با پوشت ابریشمی"
                    else -> "شامپاینی دودی با پاپیون ابریشم خالص"
                }
                list.add(GroomSuitModel(code, name, style, color))
                count++
            }
        }
        list
    }
}
