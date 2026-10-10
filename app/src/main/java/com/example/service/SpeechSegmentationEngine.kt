package com.example.service

import com.example.model.BusinessOffer
import com.example.model.BusinessOfferCategory
import com.example.model.SpeechSegment
import com.example.model.VideoScene
import com.example.model.VisualDisplayType
import java.util.UUID

object SpeechSegmentationEngine {

    val SAMPLE_SALONYAR_SPEECH = """
سلام و درود به همه همکاران و مدیران سالن‌های زیبایی، مزون‌ها و فعالان حوزه زیبایی.
امروز اینجا هستیم تا سامانه یکپارچه «سالنیار» از مجموعه TAVANA را به شما معرفی کنیم.
بزرگترین چالش سالن‌های زیبایی امروز، مدیریت شلوغی، هدررفت نوبت‌ها و بازگرداندن مشتریان قبلی است.
سالنیار برای هر سالن زیبایی یک ویترین اختصاصی و شیک ایجاد می‌کند تا نمونه‌کارها، خدمات و تعرفه‌ها شفاف دیده شوند.
سیستم رزرو آنلاین سالنیار به مشتریان امکان می‌دهد در هر ساعت از شبانه‌روز نوبت بگیرند و پیامک یادآوری دریافت کنند.
قابلیت کلیدی سالنیار، بازگرداندن هوشمند مشتریانی است که بیش از سه ماه به سالن مراجعه نکرده‌اند با پیشنهادهای مناسبتی.
پیشنهاد ویژه تجاری ما: تشکیل شبکه همکاری بین سالن‌های زیبایی، مزون‌های لباس عروس و تالارهای پذیرایی است.
در این مدل همکاری، با معرفی مشتری متقابل میان سالن و مزون، درصد کمیسیون توافقی به طرفین تعلق می‌گیرد.
همچنین استودیوی توانا در ساخت محتوای ویدیویی، تیزر تبلیغاتی و استوری‌های باکیفیت شما را پشتیبانی می‌کند.
در فازهای توسعه آینده، سیستم هوش مصنوعی تحلیل رفتار مشتری و پیش‌بینی تقاضای فصلی به سالنیار افزوده خواهد شد.
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
                text.contains("عروس", ignoreCase = true) || text.contains("مزون", ignoreCase = true) || text.contains("میکاپ", ignoreCase = true) || text.contains("گریم", ignoreCase = true) -> {
                    Triple(
                        "img_tour_bridal",
                        "سوئیت شاهانه عروس VIP با آینه‌های میکاپ هالیوودی، گل‌های ارکیده و دکوراسیون مجلل برای جذب مشتریان خاص و خدمات تشریفات",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("پوست", ignoreCase = true) || text.contains("اسپا", ignoreCase = true) || text.contains("فیشیال", ignoreCase = true) || text.contains("آرامش", ignoreCase = true) -> {
                    Triple(
                        "img_tour_spa",
                        "اتاق اسپا و مراقبت پوست آرامش‌بخش با شمع‌های معطر، رایحه اسطوخودوس و تخت ماساژ لوکس برای ایجاد وفاداری عمیق مشتریان",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("ناخن", ignoreCase = true) || text.contains("مانیکور", ignoreCase = true) || text.contains("پدیکور", ignoreCase = true) || text.contains("بازگرداندن", ignoreCase = true) -> {
                    Triple(
                        "img_tour_nail",
                        "بار مدرن مانیکور، پدیکور و طراحی ناخن با استند صدها رنگ لاک متنوع و مبلمان شیک برای جذب حداکثری مراجعین هفتگی",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("مو", ignoreCase = true) || text.contains("رنگ", ignoreCase = true) || text.contains("کراتین", ignoreCase = true) || text.contains("استایل", ignoreCase = true) || text.contains("ویترین", ignoreCase = true) -> {
                    Triple(
                        "img_tour_hair",
                        "لاین تخصصی استایل و خدمات زیبایی مو با صندلی‌های چرم ارگونومیک، آینه‌های قوسی بک‌لایت و ایستگاه‌های شستشوی مدرن",
                        VisualDisplayType.IMAGE
                    )
                }
                text.contains("رزرو", ignoreCase = true) || text.contains("سیستم", ignoreCase = true) -> {
                    Triple(
                        "img_tour_reception",
                        "اینفوگرافیک رزرو هوشمند نوبت‌ها در کنار لابی مجلل و پذیرش آرامش‌بخش سالن برای جذب مشتریان مدرن",
                        VisualDisplayType.HYBRID
                    )
                }
                text.contains("پیشنهاد", ignoreCase = true) || text.contains("کمیسیون", ignoreCase = true) -> {
                    Triple(
                        "img_tour_bridal",
                        "طرح همکاری مشارکتی سالن و مزون با نمایش سوئیت عروس و کارت درصد همکاری تجاری",
                        VisualDisplayType.HYBRID
                    )
                }
                else -> {
                    // Tour rotation for introduction and general speech sections
                    val cycle = listOf(
                        Triple("img_tour_reception", "لابی ورودی و پذیرش مجلل با سنگ مرمر و نورپردازی گرم طلایی برای ثبت بهترین برداشت اولیه مراجعین", VisualDisplayType.IMAGE),
                        Triple("img_tour_hair", "لاین تخصصی استایل مو و رنگ با طراحی مدرن و تجهیزات حرفه‌ای", VisualDisplayType.IMAGE),
                        Triple("img_tour_bridal", "سوئیت تشریفات و گریم عروس VIP با فضای خصوصی و امکانات شاهانه", VisualDisplayType.IMAGE),
                        Triple("img_tour_spa", "واحه آرامش و فیشیال تخصصی پوست با فضاسازی آرامش‌بخش", VisualDisplayType.IMAGE),
                        Triple("img_tour_nail", "بار مدرن مانیکور و پدیکور با استند دیواری جذاب و پرانرژی", VisualDisplayType.IMAGE)
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
            text.contains("سلام", ignoreCase = true) || text.contains("معرفی", ignoreCase = true) -> "مقدمه و معرفی سالنیار"
            text.contains("چالش", ignoreCase = true) || text.contains("شلوغی", ignoreCase = true) -> "بررسی نیازها و چالش‌ها"
            text.contains("ویترین", ignoreCase = true) -> "ویترین اختصاصی کسب‌وکار"
            text.contains("رزرو", ignoreCase = true) || text.contains("نوبت", ignoreCase = true) -> "نوبت‌دهی آنلاین و پیامک"
            text.contains("بازگرداندن", ignoreCase = true) || text.contains("مشتریان", ignoreCase = true) -> "باشگاه مشتریان و بازگشت"
            text.contains("همکاری", ignoreCase = true) || text.contains("مزون", ignoreCase = true) -> "شبکه همکاری چندجانبه"
            text.contains("کمیسیون", ignoreCase = true) || text.contains("پورسانت", ignoreCase = true) -> "مدل درآمد و سودآوری"
            text.contains("محتوا", ignoreCase = true) || text.contains("تیزر", ignoreCase = true) -> "تولید محتوا و رسانه"
            text.contains("آینده", ignoreCase = true) || text.contains("توسعه", ignoreCase = true) -> "چشم‌انداز و قابلیت‌های آتی"
            else -> "نکات کلیدی سخنرانی"
        }
    }

    private fun countWords(s: String): Int {
        return s.split("\\s+".toRegex()).count { it.isNotBlank() }
    }
}
