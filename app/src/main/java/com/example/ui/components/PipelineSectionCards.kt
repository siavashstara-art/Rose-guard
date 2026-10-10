package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.StatePending
import com.example.ui.theme.StatePendingContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class PipelineStageItem(
    val stepNumber: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val statusLabel: String,
    val phaseTag: String
)

@Composable
fun PipelineRoadmapSection(
    hasAudioLoaded: Boolean,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        PipelineStageItem(
            stepNumber = "گام ۲",
            title = "متن سخنرانی (رونویسی گفتار و تحلیل)",
            description = "تحلیل هوشمند فایل صوتی و تبدیل دقیق واژگان گفتاری فارسی به متن دارای زمان‌بندی و پیشنهادهای تجاری",
            icon = Icons.Default.RecordVoiceOver,
            statusLabel = "تکمیل‌شده (رونویسی و تفکیک پیشنهادها)",
            phaseTag = "phase_transcript_section"
        ),
        PipelineStageItem(
            stepNumber = "گام ۳",
            title = "تصاویر هوشمند، شخصی‌سازی و لایسنس",
            description = "هماهنگی تصاویر با سخنرانی، شخصی‌سازی ویترین سالن برای ویزیتور و سیستم مدیریت ۲۰۰ لایسنس",
            icon = Icons.Default.Image,
            statusLabel = "تکمیل‌شده (تصاویر، ابعاد و لایسنس)",
            phaseTag = "phase_scenes_section"
        ),
        PipelineStageItem(
            stepNumber = "گام ۴",
            title = "زیرنویس هوشمند، هماهنگی صدا، تصویر و موسیقی",
            description = "شخصی‌سازی کامل زیرنویس فارسی، میکس صدای اصلی با موسیقی پس‌زمینه بدون حق نشر و تنظیم بالانس",
            icon = Icons.Default.ClosedCaption,
            statusLabel = "تکمیل‌شده و فعال (زیرنویس، میکسر و موسیقی)",
            phaseTag = "phase_subtitles_section"
        ),
        PipelineStageItem(
            stepNumber = "گام ۵",
            title = "خروجی نهایی ویدیو (MP4)",
            description = "تلفیق همزمان صدای اصلی سخنرانی، فریم‌های تصویری و لایه‌های زیرنویس و رندر فایل ویدیویی",
            icon = Icons.Default.Movie,
            statusLabel = "در انتظار گام ۵ (رندر نهایی)",
            phaseTag = "phase_export_section"
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = SoftPinkContainer,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SoftPink,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "نقشهٔ راه استودیوی توانا",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "بخش‌های هوشمند تولید ویدیو از سخنرانی",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = IvorySurfaceVariant
            ) {
                Text(
                    text = "فاز فعلی: ۳ از ۵",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        stages.forEach { stage ->
            PipelineStageCard(stage = stage)
        }
    }
}

@Composable
private fun PipelineStageCard(
    stage: PipelineStageItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stage.phaseTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = IvorySurface.copy(alpha = 0.85f)
        ),
        border = BorderStroke(1.dp, IvoryCardBorder.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Stage Icon Badge
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = PeachContainer.copy(alpha = 0.6f),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = stage.icon,
                        contentDescription = stage.title,
                        tint = PeachPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stage.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatePendingContainer
                    ) {
                        Text(
                            text = stage.stepNumber,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stage.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LockClock,
                        contentDescription = null,
                        tint = StatePending,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stage.statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
