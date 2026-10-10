package com.example.service

import com.example.BuildConfig
import com.example.model.BusinessOffer
import com.example.model.BusinessOfferCategory
import com.example.model.SpeechSegment
import com.example.model.VideoScene
import com.example.model.VisualDisplayType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiSpeechService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun analyzePersianSpeechWithGemini(
        transcriptText: String,
        totalDurationSec: Double
    ): Result<Triple<List<SpeechSegment>, List<BusinessOffer>, List<VideoScene>>> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyConfigured()) {
            return@withContext Result.failure(
                IllegalStateException("کلید Gemini API هنوز در تنظیمات برنامه (Secrets) ثبت نشده است. می‌توانید از موتور پردازش داخلی و ورود مستقیم متن استفاده کنید.")
            )
        }

        try {
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val prompt = """
شما دستیار تخصصی تحلیل سخنرانی فارسی برای پلتفرم «استودیوی صوتی ویدیویی توانا (TAVANA Studio)» و محصول «سالنیار» هستید.
متن سخنرانی فارسی زیر با مدت زمان کلی $totalDurationSec ثانیه در دست است:
\"\"\"
$transcriptText
\"\"\"

وظایف شما:
۱. این سخنرانی را بدون خلاصه کردن و بدون حذف یا بازنویسی آزادانه، به جملات یا بخش‌های دقیق با زمان‌بندی (startSec و endSec بین ۰ تا $totalDurationSec) تقسیم کن.
۲. برای هر بخش، یک موضوع کوتاه (topic) استخراج کن.
۳. پیشنهادها و امکانات تجاری مطرح‌شده را استخراج کرده و دقیقاً در یکی از این سه دسته قرار بده:
   - CONFIRMED_FEATURE (قابلیت موجود در سالنیار)
   - SPEECH_OFFER (پیشنهاد تجاری مطرح‌شده در سخنرانی)
   - FUTURE_IDEA (ایده یا قابلیت آینده)
۴. برای هر بخش، یک سناریوی صحنه ویدیویی با شرح تصویر و نوع نمایش (IMAGE, CHART, TEXT, HYBRID) پیشنهاد بده.

پاسخ را دقیقاً در قالب این ساختار JSON برگردان بدون هیچ متن اضافی:
{
  "segments": [
    {
      "id": "seg_1",
      "text": "متن دقیق جمله",
      "startTimeSec": 0.0,
      "endTimeSec": 15.0,
      "topic": "موضوع بخش"
    }
  ],
  "businessOffers": [
    {
      "id": "offer_1",
      "title": "عنوان پیشنهاد",
      "description": "توضیحات",
      "sourceQuote": "متن مستند سخنرانی",
      "category": "CONFIRMED_FEATURE",
      "timestampSec": 5.0
    }
  ],
  "videoScenes": [
    {
      "sceneId": "scene_1",
      "speechSegmentId": "seg_1",
      "startTimeSec": 0.0,
      "endTimeSec": 15.0,
      "subtitleText": "متن زیرنویس",
      "visualPromptDescription": "توصیف تصویر صحنه",
      "displayType": "IMAGE"
    }
  ]
}
""".trimIndent()

            val requestJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)

                val generationConfig = JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.2)
                }
                put("generationConfig", generationConfig)
            }

            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(endpoint)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: "Unknown error"
                return@withContext Result.failure(Exception("خطا در ارتباط با سرور هوش مصنوعی (${response.code}): $errorBody"))
            }

            val responseBody = response.body?.string() ?: ""
            val jsonRoot = JSONObject(responseBody)
            val candidates = jsonRoot.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val parts = firstCandidate?.optJSONObject("content")?.optJSONArray("parts")
            val rawOutput = parts?.optJSONObject(0)?.optString("text") ?: ""

            if (rawOutput.isBlank()) {
                return@withContext Result.failure(Exception("پاسخی از مدل هوش مصنوعی دریافت نشد."))
            }

            // Parse result
            val parsedResult = JSONObject(rawOutput)
            val segmentsJson = parsedResult.optJSONArray("segments") ?: JSONArray()
            val offersJson = parsedResult.optJSONArray("businessOffers") ?: JSONArray()
            val scenesJson = parsedResult.optJSONArray("videoScenes") ?: JSONArray()

            val parsedSegments = mutableListOf<SpeechSegment>()
            for (i in 0 until segmentsJson.length()) {
                val item = segmentsJson.getJSONObject(i)
                parsedSegments.add(
                    SpeechSegment(
                        id = item.optString("id", "seg_${i + 1}"),
                        text = item.optString("text", ""),
                        startTimeSec = item.optDouble("startTimeSec", 0.0),
                        endTimeSec = item.optDouble("endTimeSec", 10.0),
                        topic = item.optString("topic", ""),
                        isConfirmed = false,
                        editNote = "",
                        isApproximateTiming = false
                    )
                )
            }

            val parsedOffers = mutableListOf<BusinessOffer>()
            for (i in 0 until offersJson.length()) {
                val item = offersJson.getJSONObject(i)
                val catStr = item.optString("category", "CONFIRMED_FEATURE")
                val category = when (catStr) {
                    "CONFIRMED_FEATURE" -> BusinessOfferCategory.CONFIRMED_FEATURE
                    "SPEECH_OFFER" -> BusinessOfferCategory.SPEECH_OFFER
                    else -> BusinessOfferCategory.FUTURE_IDEA
                }
                parsedOffers.add(
                    BusinessOffer(
                        id = item.optString("id", "offer_${i + 1}"),
                        title = item.optString("title", ""),
                        description = item.optString("description", ""),
                        sourceQuote = item.optString("sourceQuote", ""),
                        category = category,
                        timestampSec = item.optDouble("timestampSec", 0.0)
                    )
                )
            }

            val parsedScenes = mutableListOf<VideoScene>()
            for (i in 0 until scenesJson.length()) {
                val item = scenesJson.getJSONObject(i)
                val typeStr = item.optString("displayType", "IMAGE")
                val displayType = when (typeStr) {
                    "CHART" -> VisualDisplayType.CHART
                    "TEXT" -> VisualDisplayType.TEXT
                    "HYBRID" -> VisualDisplayType.HYBRID
                    else -> VisualDisplayType.IMAGE
                }
                parsedScenes.add(
                    VideoScene(
                        sceneId = item.optString("sceneId", "scene_${i + 1}"),
                        speechSegmentId = item.optString("speechSegmentId", "seg_${i + 1}"),
                        startTimeSec = item.optDouble("startTimeSec", 0.0),
                        endTimeSec = item.optDouble("endTimeSec", 10.0),
                        subtitleText = item.optString("subtitleText", ""),
                        visualPromptDescription = item.optString("visualPromptDescription", ""),
                        displayType = displayType,
                        assetUri = null,
                        isVisualApproved = false
                    )
                )
            }

            Result.success(Triple(parsedSegments, parsedOffers, parsedScenes))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
