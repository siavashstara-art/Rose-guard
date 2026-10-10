package com.example.model

/**
 * Vertical position of subtitles on the video canvas.
 */
enum class SubtitlePosition(val labelPersian: String) {
    BOTTOM("پایین کادر (استاندارد)"),
    CENTER("مرکز تصویر (تایپوگرافی)"),
    TOP("بالای کادر")
}

/**
 * Color presets for subtitles.
 */
enum class SubtitleColorPreset(val labelPersian: String, val colorHex: Long) {
    WHITE("سفید خالص", 0xFFFFFFFF),
    GOLD("طلایی لوکس سالنیار", 0xFFFFD54F),
    PEACH("هلویی گرم", 0xFFFFAB91),
    SOFT_PINK("صورتی ملایم", 0xFFF48FB1),
    CYAN("فیروزه‌ای مدرن", 0xFF80DEEA)
}

/**
 * Background style presets for subtitles.
 */
enum class SubtitleBackgroundPreset(val labelPersian: String, val colorHex: Long) {
    TRANSLUCENT_BLACK("مشکی نیمه‌شفاف (سینمایی)", 0xAA000000),
    DARK_CARD("کارت تیره مات", 0xDD1F1B1A),
    WARM_IVORY("عاجی و هلویی نیمه‌شفاف", 0xDDFBF8F5),
    TRANSPARENT("بدون پس‌زمینه (حاشیه متن)", 0x00000000)
}

/**
 * Subtitle styling and display preferences.
 */
data class SubtitleStyle(
    val fontSizeSp: Float = 16f,
    val textColorHex: Long = 0xFFFFFFFF,
    val backgroundColorHex: Long = 0xAA000000,
    val position: SubtitlePosition = SubtitlePosition.BOTTOM,
    val maxLines: Int = 2,
    val isVisible: Boolean = true
)

/**
 * Background music metadata and configuration.
 */
data class BackgroundMusicTrack(
    val id: String,
    val title: String,
    val artistOrSource: String,
    val licenseNotice: String,
    val uriString: String? = null,
    val volume: Float = 0.25f,
    val isMuted: Boolean = false,
    val isFadeInOutEnabled: Boolean = true,
    val startOffsetSec: Double = 0.0
)

/**
 * Audio mixing parameters between primary speech voice and background music.
 */
data class AudioMixSettings(
    val speechVolume: Float = 1.0f,
    val musicVolume: Float = 0.22f,
    val isMusicMuted: Boolean = false,
    val isNoiseReductionFilterActive: Boolean = false
)

/**
 * Room acoustic size preset for vocal reverb & presence.
 */
enum class RoomSizePreset(val labelPersian: String, val decayMs: Short, val roomLevelMb: Short) {
    STUDIO_WARM("استودیوی گرم و متمرکز", 900, -1200),
    ENGAGING_ROOM("روم گیرا و دلنشین (توصیه مدیران)", 1400, -800),
    ACOUSTIC_HALL("تالار آکوستیک مجلل و پرطنین", 2200, -500),
    INTIMATE_BOOTH("کابین ضبط صمیمی و بدون بازتاب اضافه", 600, -1600)
}

/**
 * Settings for vocal enhancement: subtle echo, room reverb, and voice warmth.
 */
data class VoiceAcousticSettings(
    val isEnabled: Boolean = true,
    val roomSize: RoomSizePreset = RoomSizePreset.ENGAGING_ROOM,
    val echoLevel: Float = 0.20f, // 20% subtle echo / reflections
    val roomSizeAmount: Float = 0.35f, // 35% room presence
    val vocalWarmth: Float = 0.30f, // 30% warmth and intimacy
    val autoDucking: Boolean = true // gentle volume lowering of music when speaking
)

/**
 * Curated peaceful and captivating background music catalog.
 */
data class CuratedMusicTrack(
    val id: String,
    val title: String,
    val persianMood: String,
    val sourceOrStyle: String,
    val defaultVolume: Float = 0.22f
)

object CuratedMusicCatalog {
    val TRACKS: List<CuratedMusicTrack> = listOf(
        CuratedMusicTrack(
            id = "ambient_piano_01",
            title = "پیانو ملایم امبینت و آرامش‌بخش سالنیار",
            persianMood = "آرامش عمیق، نفوذ کلام و پرستیژ مدیران",
            sourceOrStyle = "پیانو نئو-کلاسیک با بافت صوتی امبینت و ریورب ابریشمی",
            defaultVolume = 0.22f
        ),
        CuratedMusicTrack(
            id = "strings_dreamy_02",
            title = "ملودی رؤیایی سازهای زهی و هارپ",
            persianMood = "شکوه، لطافت و اثرگذاری نافذ بر احساس شنونده",
            sourceOrStyle = "ارکستر زهی ملایم همراه با چنگ و پدهای سینمایی",
            defaultVolume = 0.20f
        ),
        CuratedMusicTrack(
            id = "spa_meditation_03",
            title = "فرکانس آرامش عمیق، اسپا و ذن",
            persianMood = "رفع استرس، آرامش مطلق و تمرکز روی پیام صوتی",
            sourceOrStyle = "اصوات طبیعی فرکانس ۵۲۸ هرتز با کاسه تبتی و صدای آب",
            defaultVolume = 0.18f
        ),
        CuratedMusicTrack(
            id = "acoustic_warmth_04",
            title = "گیتار آکوستیک لایت و گرم برندینگ",
            persianMood = "صمیمیت، انگیزه و انتقال حس اعتماد و رشد کسب‌وکار",
            sourceOrStyle = "گیتار نایلون ملایم با آکوردهای باز و ملودیک",
            defaultVolume = 0.22f
        )
    )
}

