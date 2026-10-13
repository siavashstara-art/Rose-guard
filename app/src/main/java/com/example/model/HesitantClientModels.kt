package com.example.model

/**
 * Hesitant Client Styling & Face Analysis Engine.
 * Recommends optimal bridal makeup, hairstyle, gown, and accessories.
 */
enum class FaceShape(val titlePersian: String, val descriptionPersian: String) {
    OVAL("بیضی (ایده‌آل)", "متعادل‌ترین فرم چهره با فک متقارن و گونه‌های نرم"),
    ROUND("گرد", "طول و عرض برابر، زاویه ملایم فک و گونه‌های پر"),
    HEART("قلبی / مثلث معکوس", "پیشانی عریض و چانه ظریف و نوک‌تیز"),
    SQUARE("مربعی", "زاویه فک مقتدر، پیشانی پهن و استخوان‌بندی برجسته"),
    LONG("کشیده / مستطیلی", "طول صورت بیشتر از عرض با پیشانی بلند و چانه باریک")
}

enum class BodyAnatomy(val titlePersian: String, val descriptionPersian: String) {
    HOURGLASS("ساعت شنی", "کمر باریک و تناسب سینه و باسن"),
    PEAR("گلابی / مثلثی", "باسن پهن‌تر از بالاتنه و شانه‌های ظریف"),
    RECTANGLE("مستطیلی / خطی", "فرم بدنی یکدست و بدون فرورفتگی شدید کمر"),
    PETITE("ظریف و قدکوتاه", "اندام ریزنقش نیازمند کشیده‌تر نشان دادن قامت"),
    CURVY_PLUS("توپر و پرانرژی", "انحناهای زیبا نیازمند فرم‌دهی جذاب و اصولی")
}

enum class SkinTone(val titlePersian: String, val paletteDesc: String) {
    FAIR("سفید و مهتابی", "پالت رنگی هلویی لایت، رژلب گوشتی، سایه رزگلد"),
    MEDIUM_WHEAT("گندمی طبیعی", "پالت برنزی ملایم، طلایی گرم، رژلب مرجانی"),
    WARM_BRONZE("سبزه / برنز", "پالت کاراملی گرم، هایلایتر شامپاینی، رژلب نود شکلاتی")
}

data class HesitantRecommendation(
    val faceShape: FaceShape,
    val bodyAnatomy: BodyAnatomy,
    val skinTone: SkinTone,
    val matchPercentage: Int,
    val recommendedHairstyle: String,
    val hairstyleReason: String,
    val recommendedMakeup: String,
    val makeupReason: String,
    val recommendedDressCut: String,
    val dressReason: String,
    val recommendedCrownType: String,
    val crownReason: String,
    val psychologicalCloserTip: String
)

object HesitantClientEngine {
    fun analyzeAndRecommend(
        faceShape: FaceShape,
        bodyAnatomy: BodyAnatomy,
        skinTone: SkinTone
    ): HesitantRecommendation {
        val (hair, hairReason) = when (faceShape) {
            FaceShape.OVAL -> "شینیون خطی کلاسیک نیمه‌باز اروپایی" to "فرم چهره شما کاملاً متقارن است؛ این مدل تعادل چشمگیر چشم و لب‌ها را حفظ می‌کند."
            FaceShape.ROUND -> "شینیون حجیم در تاج سر با تارهای ریخته دور چهره" to "افزایش ارتفاع شینیون در بالای سر باعث کشیده‌تر و لاغرتر دیده شدن فرم گرد صورت می‌شود."
            FaceShape.HEART -> "شینیون پرحجم در پشت گردن با چتری کج" to "حجم مو در پایین سر تعادل بی‌نظیری با باریکی چانه ایجاد کرده و زیبایی نگاه را دوچندان می‌کند."
            FaceShape.SQUARE -> "براشینگ مواج رمانتیک با پیچ و تاب ملایم" to "امواج نرم مو خطوط تیز و استخوانی فک را لطیف و زنانه جلوه می‌دهد."
            FaceShape.LONG -> "شینیون باز مواج با حجم در طرفین سر" to "عرض دادن به موها در دو طرف چهره از کشیدگی بیش از حد صورت می‌کاهد."
        }

        val (makeup, makeupReason) = when (skinTone) {
            SkinTone.FAIR -> "میکاپ اسموکی لایت با تناژ رزگلد و رژلب پیچی ساتن" to "کنتراست لطیف با پوست سفید بدون رنگ‌پریدگی، با جلوه شاداب و کریستالی."
            SkinTone.MEDIUM_WHEAT -> "میکاپ اروپایی نود با کانتورینگ گرم و هایلایتر شامپاینی" to "هماهنگی کامل با پیگمنت گندمی پوست و نمایش درخشش طبیعی در نور تالار."
            SkinTone.WARM_BRONZE -> "میکاپ کالیفرنیایی شاین با کانتور برنزه و رژ کالباسی مخملی" to "جلوه‌ای اشرافی، لوکس و پرطراوت متناسب با پوست سبزه."
        }

        val (dress, dressReason) = when (bodyAnatomy) {
            BodyAnatomy.HOURGLASS -> "لباس عروس مدل دم‌ماهی (Mermaid) با دانتل شاین" to "انحنای بی‌نقص ساعت شنی را به بهترین نحو در سن رقص نمایان می‌سازد."
            BodyAnatomy.PEAR -> "لباس عروس پرنسسی (A-Line) با بالاتنه کارشده" to "توجه را به بالاتنه و سرشانه هدایت کرده و پایین‌تنه را با دامن پف‌دار رویایی می‌پوشاند."
            BodyAnatomy.RECTANGLE -> "لباس عروس کمرکرستی امپایر با دامن چندلایه" to "خط کمر را برجسته و باریک‌تر نشان داده و ساختاری جذاب ایجاد می‌کند."
            BodyAnatomy.PETITE -> "لباس عروس اسکارلت با یقه دلبری و قد کشیده" to "قد عروس را بلندتر و حرکات را در طول تالار سبک‌بال و باوقار نشان می‌دهد."
            BodyAnatomy.CURVY_PLUS -> "لباس عروس بوهو ساتن با آستین توری و بالاتنه V" to "فرم‌دهی کشیده به گردن و بالاتنه با حس راحتی و وقار سلطنتی."
        }

        val (crown, crownReason) = when (faceShape) {
            FaceShape.ROUND, FaceShape.SQUARE -> "تاج فرحی ارتفاع‌دار با کریستال‌های تراش‌خورده" to "ایجاد ارتفاع بصری و افزودن ابهت ملکه به استایل."
            else -> "ریسه مروارید فرانسوی و تل کریستالی منعطف" to "ظرافت اروپایی و همخوانی بی‌نقص با بافت شینیون مدرن."
        }

        val matchPercent = 94 + ((faceShape.ordinal + bodyAnatomy.ordinal + skinTone.ordinal) % 6)

        val closingTip = "فرمول تبدیل تردید به قرارداد: این شبیه‌سازی را به مشتری نشان دهید و بگویید «ما آناتومی شما را دقیقاً تحلیل کردیم؛ هر سالن دیگری بروید این استایل علمی را پیاده نخواهد کرد!»"

        return HesitantRecommendation(
            faceShape = faceShape,
            bodyAnatomy = bodyAnatomy,
            skinTone = skinTone,
            matchPercentage = matchPercent,
            recommendedHairstyle = hair,
            hairstyleReason = hairReason,
            recommendedMakeup = makeup,
            makeupReason = makeupReason,
            recommendedDressCut = dress,
            dressReason = dressReason,
            recommendedCrownType = crown,
            crownReason = crownReason,
            psychologicalCloserTip = closingTip
        )
    }
}
