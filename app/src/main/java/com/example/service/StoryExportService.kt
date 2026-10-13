package com.example.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.model.PrivacyMaskType
import com.example.model.StoryDraft
import com.example.model.StoryInteractiveSticker
import java.io.File
import java.io.FileOutputStream

object StoryExportService {

    fun generateStoryBitmap(draft: StoryDraft): Bitmap {
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Luxury gradient background
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                intArrayOf(Color.parseColor("#1B0E2A"), Color.parseColor("#0C0714"), Color.parseColor("#261439")),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Golden decorative border
        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 14f
            color = Color.parseColor("#D4AF37")
        }
        val innerRect = RectF(40f, 40f, width - 40f, height - 40f)
        canvas.drawRoundRect(innerRect, 32f, 32f, borderPaint)

        // Header Salon Badge
        val badgePaint = Paint().apply {
            color = Color.parseColor("#381F54")
            style = Paint.Style.FILL
        }
        val badgeRect = RectF(120f, 100f, width - 120f, 210f)
        canvas.drawRoundRect(badgeRect, 55f, 55f, badgePaint)

        val goldTextPaint = Paint().apply {
            color = Color.parseColor("#F5D77F")
            textSize = 46f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("👑 ${draft.salonBrandName} 👑", width / 2f, 175f, goldTextPaint)

        // Center Visual Showcase Area
        val showcasePaint = Paint().apply {
            color = Color.parseColor("#170F22")
            style = Paint.Style.FILL
        }
        val showcaseRect = RectF(90f, 260f, width - 90f, 1180f)
        canvas.drawRoundRect(showcaseRect, 28f, 28f, showcasePaint)

        // Template Badge
        val tagBgPaint = Paint().apply {
            color = Color.parseColor("#D4AF37")
        }
        val tagRect = RectF(200f, 300f, width - 200f, 380f)
        canvas.drawRoundRect(tagRect, 40f, 40f, tagBgPaint)

        val tagTextPaint = Paint().apply {
            color = Color.parseColor("#11081A")
            textSize = 38f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(draft.template.badgeText, width / 2f, 355f, tagTextPaint)

        // Visual Center Graphic (Luxury Stylized Silhouette / Crown & Veil)
        val crownPaint = Paint().apply {
            color = Color.parseColor("#ECC34C")
            textSize = 130f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("✨ 👰 💎", width / 2f, 580f, crownPaint)

        // Privacy Mask Overlay preview on story
        if (draft.privacyMaskApplied != PrivacyMaskType.NONE) {
            val maskBadgePaint = Paint().apply {
                color = Color.parseColor("#80241038")
            }
            val maskRect = RectF(160f, 650f, width - 160f, 790f)
            canvas.drawRoundRect(maskRect, 24f, 24f, maskBadgePaint)

            val maskTextPaint = Paint().apply {
                color = Color.parseColor("#F3C68F")
                textSize = 34f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            canvas.drawText("🌹 ${draft.privacyMaskApplied.titlePersian}", width / 2f, 735f, maskTextPaint)
        }

        // Headline
        val headlinePaint = Paint().apply {
            color = Color.WHITE
            textSize = 54f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(draft.headline, width / 2f, 890f, headlinePaint)

        // Subheadline
        val subPaint = Paint().apply {
            color = Color.parseColor("#D9CBE8")
            textSize = 34f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        // Multi-line wrap
        val words = draft.subheadline.split(" ")
        var line1 = ""
        var line2 = ""
        for (w in words) {
            if ((line1 + " " + w).length < 34) {
                line1 += " $w"
            } else {
                line2 += " $w"
            }
        }
        canvas.drawText(line1.trim(), width / 2f, 980f, subPaint)
        if (line2.isNotBlank()) {
            canvas.drawText(line2.trim(), width / 2f, 1040f, subPaint)
        }

        // Stickers Section
        var stickerY = 1240f
        val stickerPaint = Paint().apply {
            color = Color.parseColor("#FFE8A3")
            textSize = 38f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        for (sticker in draft.activeStickers) {
            val stickBoxPaint = Paint().apply {
                color = Color.parseColor("#441B6B")
            }
            val sRect = RectF(140f, stickerY, width - 140f, stickerY + 80f)
            canvas.drawRoundRect(sRect, 20f, 20f, stickBoxPaint)
            canvas.drawText("${sticker.visualEmoji} ${sticker.titlePersian}", width / 2f, stickerY + 55f, stickerPaint)
            stickerY += 105f
        }

        // Footer Contact Strip
        val footerBg = Paint().apply {
            color = Color.parseColor("#150A21")
        }
        val fRect = RectF(80f, height - 250f, width - 80f, height - 90f)
        canvas.drawRoundRect(fRect, 30f, 30f, footerBg)

        val footerText = Paint().apply {
            color = Color.parseColor("#D4AF37")
            textSize = 34f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("📱 رزرو و مشاوره: ${draft.phoneContact}", width / 2f, height - 190f, footerText)

        val instaText = Paint().apply {
            color = Color.WHITE
            textSize = 32f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("اینستاگرام: ${draft.instagramHandle} | سالن‌یار VIP", width / 2f, height - 130f, instaText)

        return bitmap
    }

    fun saveStoryToCache(context: Context, draft: StoryDraft): Uri? {
        return try {
            val bitmap = generateStoryBitmap(draft)
            val cacheDir = File(context.cacheDir, "stories")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            val file = File(cacheDir, "salonyar_story_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            null
        }
    }

    fun shareStory(context: Context, draft: StoryDraft, targetPackage: String? = null) {
        val uri = saveStoryToCache(context, draft)
        if (uri == null) {
            Toast.makeText(context, "خطا در تولید تصویر استوری", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, "${draft.headline}\n${draft.salonBrandName}\n${draft.instagramHandle}")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (targetPackage != null) {
            intent.setPackage(targetPackage)
        }

        try {
            val chooser = Intent.createChooser(intent, "اشتراک‌گذاری استوری سالن‌یار در اینستاگرام یا تلگرام")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "برنامه مورد نظر یافت نشد. باز کردن منوی اشتراک عمومی...", Toast.LENGTH_SHORT).show()
            val generalIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(generalIntent, "اشتراک‌گذاری"))
        }
    }
}
