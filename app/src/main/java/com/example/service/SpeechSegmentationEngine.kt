package com.example.service

import com.example.model.BusinessOffer
import com.example.model.BusinessOfferCategory
import com.example.model.SpeechSegment
import com.example.model.VideoScene
import com.example.model.VisualDisplayType
import java.util.UUID

object SpeechSegmentationEngine {

    val SAMPLE_SALONYAR_SPEECH = """
با سلام و احترام؛ من سیاوش علیمیری، طراح سیستم‌ها و اپلیکیشن‌های تخصصی و سازنده شهر متاورسی «توانا» هستم. تاکنون بیش از ۲۵ نرم‌افزار کاربردی در سطح جهان طراحی کرده‌ام.
در طول ماه، عروس‌ها و مشتریان زیادی برای خدمات زیبایی به سالن شما مراجعه می‌کنند؛ اما چالش اصلی این است که خیلی از آنها بعد از استعلام قیمت، ممکن است به ده سالن دیگر سر بزنند و برنگردند!
ما با اپلیکیشن «سالن‌یار» این مسیر را دگرگون کرده‌ایم! کافی است از عروس یک عکس ساده بگیرید. سامانه ما دارای ۵۰ مدل لباس عروس ترند روز و ۳۵ مدل کت‌شلوار داماد است.
مشتری می‌تواند پیش از مراسم، لباس عروس، مدل شینیون و آرایش را به صورت مجازی روی اندام و چهره خود ببیند! این برنامه کاملاً آفلاین عمل می‌کند و امنیت کامل تصاویر مشتری را تضمین خواهد کرد.
همچنین این اپلیکیشن مجهز به سیستم مدیریت برگشت مشتری است که به آن سیستم «برگردان مشتری» می‌گوییم؛ این قابلیت مراجعین قبلی را شناسایی کرده و به سالن شما بازمی‌گرداند.
از دیگر امکانات فوق‌العاده برنامه، حفظ کامل حریم خصوصی با قابلیت تار کردن (Blur) و پیکسل کردن چهره افراد در عکس‌ها، توانایی ساخت خودکار استوری و تیزرهای ویدیویی برای اینستاگرام، و قابلیت شخصی‌سازی کامل اپلیکیشن با قرار دادن عکس، آدرس، تلفن، مشخصات و کانال‌های اینترنتی سالن شماست.
از شما دعوت می‌کنم این برنامه را برای آرایشگاهتان تهیه کنید؛ این برنامه فقط و فقط به صورت یک‌ساله و با قیمت استثنایی ۹ میلیون تومان به فروش می‌رسد، هزینه‌ای که با جذب اولین عروس یا برگشت اولین مشتری‌ها بلافاصله برای شما جبران خواهد شد!
علاوه بر این، شما با بازاریابی و معرفی محصولات ما به سایر همکاران نیز می‌توانید درآمد مازاد کسب کنید که جزئیات آن را همکاران ما خدمت شما ارائه خواهند کرد. با تشکر از توجه شما.
""".trimIndent()

    /**
     * Segments Persian speech text into timed segments proportionally distributed
     * over the total audio duration, or default 180 seconds if no audio is present.
     */
    fun processTextToSegments(
        rawText: String,
        totalDurationSec: Double
    ): List<SpeechSegment> {
        val cleanedLines = rawText.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (cleanedLines.isEmpty()) return emptyList()

        // Count total words to distribute time proportionally
        val totalWords = cleanedLines.sumOf { countWords(it) }.coerceAtLeast(1)
        val audioDuration = if (totalDurationSec > 5.0) totalDurationSec else 180.0

        var accumulatedTime = 0.0
        val segments = mutableListOf<SpeechSegment>()

        cleanedLines.forEachIndexed { index, line ->
            val wordsInLine = countWords(line).coerceAtLeast(1)
            val segmentDuration = (wordsInLine.toDouble() / totalWords) * audioDuration
            val start = (accumulatedTime * 10).toLong() / 10.0
            accumulatedTime += segmentDuration
            val end = (accumulatedTime * 10).toLong() / 10.0

            val topic = detectTopic(line)

            segments.add(
                SpeechSegment(
                    id = "seg_${index + 1}",
                    text = line,
                    startTimeSec = start,
                    endTimeSec = end.coerceAtMost(audioDuration),
                    topic = topic,
                    isConfirmed = false,
                    editNote = "",
                    isApproximateTiming = true
                )
            )
        }

        return segments
    }

    /**
     * Extracts commercial offers and categorized product features strictly from the speech.
     */
    fun extractBusinessOffers(segments: List<SpeechSegment>): List<BusinessOffer> {
        val offers = mutableListOf<BusinessOffer>()

        segments.forEach { seg ->
            val text = seg.text

            // Category A: Confirmed features in SalonYar
            if (text.contains("ویترین اختصاصی", ignoreCase = true) || text.contains("نمونه‌کار", ignoreCase = true)) {
                offers.add(
                    BusinessOffer(
                        id = "offer_feat_1",
                        title = "ویترین اختصاصی آنلاین سالن",
                        description = "امکان ساخت صفحه معرفی و نمونه‌کار با لینک اختصاصی برای جذب مشتریان جدید",
                        sourceQuote = text,
                        category = BusinessOfferCategory.CONFIRMED_FEATURE,
                        timestampSec = seg.startTimeSec
                    )
                )
            }
            if (text.contains("رزرو آنلاین", ignoreCase = true) || text.contains("مدیریت نوبت", ignoreCase = true)) {
                offers.add(
                    BusinessOffer(
                        id = "offer_feat_2",
                        title = "رزرو و نوبت‌دهی هوشمند ۲۴ ساعته",
                        description = "نوبت‌دهی خودکار همراه با ارسال پیامک یادآوری و کاهش کنسلی‌های سالن",
                        sourceQuote = text,
                        category = BusinessOfferCategory.CONFIRMED_FEATURE,
                        timestampSec = seg.startTimeSec
                    )
                )
            }
            if (text.contains("بازگرداندن", ignoreCase = true) || text.contains("مشتریان قبلی", ignoreCase = true)) {
                offers.add(
                    BusinessOffer(
                        id = "offer_feat_3",
                        title = "باشگاه مشتریان و بازگرداندن مراجعین",
                        description = "شناسایی هوشمند مشتریان غیرفعال و ارسال خودکار آفرها و کدهای تخفیف مناسبتی",
                        sourceQuote = text,
                        category = BusinessOfferCategory.CONFIRMED_FEATURE,
                        timestampSec = seg.startTimeSec
                    )
                )
            }

            // Category B: Commercial Partnership Proposals in the speech
            if (text.contains("شبکه همکاری", ignoreCase = true) || text.contains("مزون", ignoreCase = true) || text.contains("تالار", ignoreCase = true)) {
                offers.add(
                    BusinessOffer(
                        id = "offer_biz_1",
                        title = "شبکه هم‌افزایی سالن زیبایی، مزون و تالار",
                        description = "معرفی متقابل مشتریان عروس و خدمات مجالس میان کسب‌وکارهای مکمل",
                        sourceQuote = text,
                        category = BusinessOfferCategory.SPEECH_OFFER,
                        timestampSec = seg.startTimeSec
                    )
                )
            }
            if (text.contains("کمیسیون", ignoreCase = true) || text.contains("درصد", ignoreCase = true) || text.contains("پورسانت", ignoreCase = true)) {
                offers.add(
                    BusinessOffer(
                        id = "offer_biz_2",
                        title = "مدل درآمدی کمیسیون معرفی مشتری",
                        description = "دریافت درصد توافقی به ازای معرفی هر مشتری قطعی بین طرفین همکاری",
                        sourceQuote = text,
                        category = BusinessOfferCategory.SPEECH_OFFER,
                        timestampSec = seg.startTimeSec
                    )
                )
            }
            if (text.contains("محتوای ویدیویی", ignoreCase = true) || text.contains("تیزر", ignoreCase = true) || text.contains("استوری", ignoreCase = true)) {
                offers.add(
                    BusinessOffer(
                        id = "offer_biz_3",
                        title = "پشتیبانی تولید محتوا و تیزرسازی TAVANA",
                        description = "حمایت رسانه‌ای استودیوی توانا در تولید محتوا و جذب در شبکه‌های اجتماعی",
                        sourceQuote = text,
                        category = BusinessOfferCategory.SPEECH_OFFER,
                        timestampSec = seg.startTimeSec
                    )
                )
            }

            // Category C: Future ideas and roadmap
            if (text.contains("فازهای توسعه آینده", ignoreCase = true) || text.contains("نسخه‌های آینده", ignoreCase = true) || text.contains("پیش‌بینی تقاضا", ignoreCase = true)) {
                offers.add(
                    BusinessOffer(
                        id = "offer_future_1",
                        title = "هوش مصنوعی پیش‌بینی تقاضا و رفتار مشتری",
                        description = "ابزار پیش‌بینی پیک مراجعات و مدیریت اتوماتیک قیمت‌ها در روزهای شلوغ",
                        sourceQuote = text,
                        category = BusinessOfferCategory.FUTURE_IDEA,
                        timestampSec = seg.startTimeSec
                    )
                )
            }
        }

        return offers.distinctBy { it.id }
    }

    /**
     * Builds video scenes linked to speech segments, populated with luxury visual tour assets.
     */
    fun generateVideoScenes(segments: List<SpeechSegment>): List<VideoScene> {
        return segments.mapIndexed { index, seg ->
            val text = seg.text

            val (tourImage, tourDescription, displayType) = when {
                text.contains("سیاوش علیمیری", ignoreCase = true) || text.contains("متاورس", ignoreCase = true) || text.contains("توانا", ignoreCase = true) || text.contains("۲۵", ignoreCase = true) -> {
                    Triple(
                        "tavana_icon",
                        "فاز ۱: محیط مدرن فناوری و شهر متاورسی توانا، معرفی مهندس سیاوش علیمیری، طراح ۲۵ نرم‌افزار جهانی با نشان رسمی و پرسونای قدرتمند مهندسی",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("چالش", ignoreCase = true) || text.contains("استعلام قیمت", ignoreCase = true) || text.contains("ده سالن", ignoreCase = true) || text.contains("برنگردند", ignoreCase = true) -> {
                    Triple(
                        "img_tour_reception",
                        "فاز ۲: سالن زیبایی پر رفت‌وآمد و لابی مجلل، همراه با نمایش چالش تردید مراجعین و عدم بازگشت پس از استعلام قیمت",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("عکس ساده", ignoreCase = true) || text.contains("۵۰ مدل لباس عروس", ignoreCase = true) || text.contains("۳۵ مدل کت", ignoreCase = true) || text.contains("کت‌شلوار", ignoreCase = true) -> {
                    Triple(
                        "img_tour_bridal",
                        "فاز ۳: قلاب جذب سالن‌یار در سوئیت عروس VIP، عکاسی ساده از چهره و کاتالوگ سه‌بعدی ۵۰ لباس عروس ترند روز و ۳۵ کت‌شلوار داماد",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("پرو", ignoreCase = true) || text.contains("مجازی", ignoreCase = true) || text.contains("شینیون", ignoreCase = true) || text.contains("آفلاین", ignoreCase = true) || text.contains("امنیت", ignoreCase = true) -> {
                    Triple(
                        "img_tour_spa",
                        "فاز ۴: پرو جادویی مجازی لباس عروس و شینیون روی چهره با حالت تغییر قبل و بعد (Before/After) و کارکرد کاملاً آفلاین با امنیت ۱۰۰٪ تصاویر",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("برگردان", ignoreCase = true) || text.contains("تار کردن", ignoreCase = true) || text.contains("پیکسل", ignoreCase = true) || text.contains("استوری", ignoreCase = true) || text.contains("شخصی‌سازی", ignoreCase = true) || text.contains("حریم خصوصی", ignoreCase = true) -> {
                    Triple(
                        "img_tour_hair",
                        "فاز ۵: سیستم برگردان مشتری، محافظت چهره FaceGuard (تار و شطرنجی کردن)، استوری‌ساز ۹:۱۶ اینستاگرام و شخصی‌سازی برند و آدرس سالن",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("۹ میلیون", ignoreCase = true) || text.contains("۹میلیون", ignoreCase = true) || text.contains("اولین عروس", ignoreCase = true) || text.contains("بازاریابی", ignoreCase = true) || text.contains("تشکر", ignoreCase = true) -> {
                    Triple(
                        "img_tour_nail",
                        "فاز ۶: اشتراک سالانه فقط ۹ میلیون تومان با جبران آنی در اولین عروس، درآمدزایی پورسانتی از بازاریابی همکاران و دعوت رسمی به همکاری",
                        VisualDisplayType.IMAGE
                    )
                }
                else -> {
                    // Fallback to sequential stations
                    val cycle = listOf(
                        Triple("tavana_icon", "فاز ۱: معرفی مهندس سیاوش علیمیری و شهر متاورسی توانا", VisualDisplayType.IMAGE),
                        Triple("img_tour_reception", "فاز ۲: چالش سرگردانی و استعلام قیمت عروس‌ها", VisualDisplayType.IMAGE),
                        Triple("img_tour_bridal", "فاز ۳: قلاب جذب سالن‌یار با کاتالوگ لباس عروس و داماد", VisualDisplayType.IMAGE),
                        Triple("img_tour_spa", "فاز ۴: پرو جادویی مجازی، شینیون و امنیت ۱۰۰٪ آفلاین", VisualDisplayType.IMAGE),
                        Triple("img_tour_hair", "فاز ۵: سیستم برگردان مشتری و استوری‌ساز اینستاگرام", VisualDisplayType.IMAGE),
                        Triple("img_tour_nail", "فاز ۶: قیمت ۹ میلیون تومان و درآمدزایی از معرفی به همکاران", VisualDisplayType.IMAGE)
                    )
                    cycle[index % cycle.size]
                }
            }

            VideoScene(
                sceneId = "scene_${index + 1}",
                speechSegmentId = seg.id,
                startTimeSec = seg.startTimeSec,
                endTimeSec = seg.endTimeSec,
                subtitleText = seg.text,
                visualPromptDescription = tourDescription,
                displayType = displayType,
                assetUri = tourImage,
                isVisualApproved = true,
                isReady = true
            )
        }
    }

    private fun detectTopic(text: String): String {
        return when {
            text.contains("سیاوش", ignoreCase = true) || text.contains("متاورس", ignoreCase = true) || text.contains("توانا", ignoreCase = true) -> "فاز ۱: معرفی مهندس علیمیری و شهر متاورسی توانا"
            text.contains("چالش", ignoreCase = true) || text.contains("قیمت", ignoreCase = true) || text.contains("سالن", ignoreCase = true) -> "فاز ۲: چالش استعلام قیمت و ریزش عروس‌ها"
            text.contains("لباس عروس", ignoreCase = true) || text.contains("کت", ignoreCase = true) || text.contains("عکس", ignoreCase = true) -> "فاز ۳: قلاب سالن‌یار (۵۰ لباس عروس + ۳۵ کت‌شلوار)"
            text.contains("پرو", ignoreCase = true) || text.contains("شینیون", ignoreCase = true) || text.contains("آفلاین", ignoreCase = true) -> "فاز ۴: پرو جادویی مجازی و امنیت آفلاین"
            text.contains("برگردان", ignoreCase = true) || text.contains("استوری", ignoreCase = true) || text.contains("تار", ignoreCase = true) -> "فاز ۵: سیستم برگردان مشتری، محافظت چهره و استوری‌ساز"
            text.contains("۹ میلیون", ignoreCase = true) || text.contains("۹میلیون", ignoreCase = true) || text.contains("اولین عروس", ignoreCase = true) || text.contains("بازاریابی", ignoreCase = true) -> "فاز ۶: قیمت ۹ میلیون تومان و درآمد بازاریابی"
            else -> "نکات کلیدی سخنرانی"
        }
    }

    private fun countWords(s: String): Int {
        return s.split("\\s+".toRegex()).count { it.isNotBlank() }
    }
}
