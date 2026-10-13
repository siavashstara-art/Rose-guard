package com.example.model

/**
 * Bridal Crowns and Accessories with smart head fitting parameters.
 */
data class BridalCrown(
    val id: String,
    val namePersian: String,
    val categoryPersian: String,
    val descriptionPersian: String,
    val heightProfile: String,
    val defaultScale: Float = 1.0f,
    val defaultOffsetY: Float = 0.0f
)

object BridalCrownCatalog {
    val CROWNS = listOf(
        BridalCrown(
            id = "CR-01",
            namePersian = "تاج فرحی سلطنتی (ملکه)",
            categoryPersian = "کلاسیک و مرتفع",
            descriptionPersian = "ارتفاع بلند با نگین‌های زیرکونیا و پایه‌های نقره‌ای دست‌ساز، نماد اصالت و شکوه ایرانی",
            heightProfile = "بلند (۷ الی ۱۰ سانتی‌متر)",
            defaultScale = 1.15f,
            defaultOffsetY = -20f
        ),
        BridalCrown(
            id = "CR-02",
            namePersian = "تاج رومی کریستالی برگ زیتون",
            categoryPersian = "مدرن و مینیمال",
            descriptionPersian = "طراحی انعطاف‌پذیر با موتیف برگ‌های کریستالی برنزی و طلایی با جلوه اروپایی",
            heightProfile = "متوسط (۴ الی ۵ سانتی‌متر)",
            defaultScale = 1.0f,
            defaultOffsetY = -5f
        ),
        BridalCrown(
            id = "CR-03",
            namePersian = "ریسه مروارید باروک و کریستال فرانسوی",
            categoryPersian = "بوهو و رمانتیک",
            descriptionPersian = "بافته‌شده با سیم مفتول نرم نقره‌ای و دانه‌های مروارید طبیعی پرورشی مناسب شینیون باز",
            heightProfile = "ظریف و خوابیده",
            defaultScale = 0.95f,
            defaultOffsetY = 5f
        ),
        BridalCrown(
            id = "CR-04",
            namePersian = "نیم‌تاج نگین‌دار پرنسسی ویکتوریا",
            categoryPersian = "پرنسسی اشرافی",
            descriptionPersian = "طاق منحنی با نگین‌های اتریشی درخشان مناسب چهره‌های گرد و قلبی",
            heightProfile = "متوسط رو به بالا (۶ سانتی‌متر)",
            defaultScale = 1.05f,
            defaultOffsetY = -12f
        ),
        BridalCrown(
            id = "CR-05",
            namePersian = "تل مرواریدی ضخیم پادشاهی",
            categoryPersian = "ترند روز ۲۰۲۶",
            descriptionPersian = "کاور برجسته تمام‌مروارید برفی، ترند شماره یک شوهای مد میلان و پاریس",
            heightProfile = "برجسته و پهن",
            defaultScale = 1.0f,
            defaultOffsetY = 0f
        )
    )
}

typealias BridalAccessoryCatalog = BridalCrownCatalog
