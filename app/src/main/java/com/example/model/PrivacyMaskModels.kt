package com.example.model

/**
 * Privacy Protective Masks and Overlays for clients who do not wish to publish full faces.
 */
enum class PrivacyMaskType(
    val id: String,
    val titlePersian: String,
    val descriptionPersian: String,
    val motifStyle: String
) {
    NONE(
        "none",
        "بدون ماسک (نمایش کامل)",
        "نمایش بدون پوشش برای مشتریانی که رضایت کامل دارند",
        "فاقد لایه حفاظتی"
    ),
    GULF_ROSE_GOLD(
        "gulf_rose_gold",
        "نقاب خلیجی و عربی با گل رز طلایی",
        "پوشش لب و گونه با بافت ابریشمی مشکی و نقوش گل رز و شاخه‌های اسلیمی طلایی",
        "حفظ جلوه کامل چشم، ابرو، شینیون و تاج"
    ),
    ARABIC_BLACK_ROSE(
        "arabic_black_rose",
        "نقاب عربی مخمل با رز قرمز تیره",
        "پوشش بخش پایینی چهره با روبنده عربی اشرافی و موتیف رز مخملی برجسته",
        "استایل لوکس با تمرکز بر آرایش چشم و مو"
    ),
    VENETIAN_MASQUERADE(
        "venetian_masquerade",
        "ماسک بالماسکه ونیزی نگین‌دار",
        "پوشش لوکس اطراف چشم با گیپور مشکی و نگین‌های درخشان بدون پوشاندن لب و مو",
        "جذابیت سینمایی و حفظ حریم شخصی چشم‌ها"
    ),
    FRENCH_LACE_VEIL(
        "french_lace_veil",
        "روبنده توری فرانسوی با گل رز",
        "تور مشبک ظریف پیشانی و چشم با شکوفه‌های رز سفید و کریستال‌های ریز",
        "کلاسیک، شاعرانه و کاملاً پوشاننده هویت"
    ),
    AESTHETIC_BLUR_SHIELD(
        "aesthetic_blur_shield",
        "تار محو هنری (Aesthetic Blur)",
        "افکت بلور ملایم شیشه‌ای (Frosted Glass) روی چشم‌ها و دهان همراه با درخشش نور",
        "مدرن و بدون تغییر در رنگ‌آمیزی کلی"
    )
}
