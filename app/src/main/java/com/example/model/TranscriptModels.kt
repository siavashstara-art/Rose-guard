package com.example.model

/**
 * Visual presentation type for a video scene in TAVANA Studio.
 */
enum class VisualDisplayType(val labelPersian: String) {
    IMAGE("تصویر مفهومی"),
    CHART("نمودار و اینفوگرافیک"),
    TEXT("تایپوگرافی و متن برجسته"),
    HYBRID("ترکیبی (تصویر + کارت اطلاعات)")
}

/**
 * Three distinct categories of commercial/product highlights as mandated:
 * الف) قابلیت موجود و تأییدشده در محصول (سالنیار)
 * ب) پیشنهاد تجاری مطرح‌شده در سخنرانی
 * ج) ایده یا قابلیت آینده که هنوز پیاده‌سازی نشده است
 */
enum class BusinessOfferCategory(val titlePersian: String, val badgeColorHex: Long) {
    CONFIRMED_FEATURE("قابلیت موجود و تأییدشده در سالنیار", 0xFF2E7D32),
    SPEECH_OFFER("پیشنهاد تجاری مطرح‌شده در سخنرانی", 0xFFE26A50),
    FUTURE_IDEA("ایده یا قابلیت آینده (توسعه)", 0xFF8E24AA)
}

/**
 * Speech segment with timing and verification metadata.
 */
data class SpeechSegment(
    val id: String,
    val text: String,
    val startTimeSec: Double,
    val endTimeSec: Double,
    val topic: String = "",
    val isConfirmed: Boolean = false,
    val editNote: String = "",
    val isApproximateTiming: Boolean = true
) {
    val durationSec: Double
        get() = (endTimeSec - startTimeSec).coerceAtLeast(0.0)
}

/**
 * Extracted commercial item, partner offer, or product feature.
 */
data class BusinessOffer(
    val id: String,
    val title: String,
    val description: String,
    val sourceQuote: String,
    val category: BusinessOfferCategory,
    val timestampSec: Double? = null
)

/**
 * Video scene infrastructure preparing for Phase 3 visual generation.
 */
data class VideoScene(
    val sceneId: String,
    val speechSegmentId: String,
    val startTimeSec: Double,
    val endTimeSec: Double,
    val subtitleText: String,
    val visualPromptDescription: String,
    val displayType: VisualDisplayType = VisualDisplayType.IMAGE,
    val assetUri: String? = null,
    val isVisualApproved: Boolean = false,
    val isReady: Boolean = assetUri != null,
    val fitMode: ImageFitMode = ImageFitMode.FIT
)

/**
 * State representing transcription and speech analysis.
 */
sealed interface AnalysisState {
    data object Idle : AnalysisState
    data class Processing(val progressMessage: String) : AnalysisState
    data class Success(val sourceDescription: String) : AnalysisState
    data class Error(val errorMessage: String) : AnalysisState
}
