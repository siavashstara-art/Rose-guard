package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AudioPlaybackUiState
import com.example.model.ImageFitMode
import com.example.model.SubtitlePosition
import com.example.model.SubtitleStyle
import com.example.model.VideoAspectRatio
import com.example.model.VideoScene
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.OnPeachContainer
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AudioStudioViewModel

@Composable
fun VideoCanvasPreview(
    scenes: List<VideoScene>,
    activeSceneId: String?,
    playbackState: AudioPlaybackUiState,
    aspectRatio: VideoAspectRatio,
    imageFitMode: ImageFitMode,
    subtitleStyle: SubtitleStyle = SubtitleStyle(),
    onSetAspectRatio: (VideoAspectRatio) -> Unit,
    onSetImageFitMode: (ImageFitMode) -> Unit,
    onTogglePlayPause: () -> Unit,
    onPickImageForActiveScene: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeScene = scenes.firstOrNull { it.sceneId == activeSceneId } ?: scenes.firstOrNull()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && activeScene != null) {
            onPickImageForActiveScene(activeScene.sceneId, uri.toString())
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("video_canvas_preview_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        border = BorderStroke(1.dp, IvoryCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = PeachContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "پیش‌نمایش تصویر و زیرنویس هماهنگ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "گام ۳ از ۵: همگام‌سازی بصری با صوت سخنرانی",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Scene Counter Pill
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = IvorySurfaceVariant
                ) {
                    val activeIndex = if (activeScene != null) scenes.indexOf(activeScene) + 1 else 1
                    Text(
                        text = "صحنه $activeIndex از ${scenes.size}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Aspect Ratio & Scale Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = aspectRatio == VideoAspectRatio.PORTRAIT_9_16,
                        onClick = { onSetAspectRatio(VideoAspectRatio.PORTRAIT_9_16) },
                        label = { Text("۹:۱۶ عمودی", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PeachContainer,
                            selectedLabelColor = PeachPrimary
                        ),
                        modifier = Modifier.height(32.dp).testTag("aspect_ratio_portrait_btn")
                    )

                    FilterChip(
                        selected = aspectRatio == VideoAspectRatio.LANDSCAPE_16_9,
                        onClick = { onSetAspectRatio(VideoAspectRatio.LANDSCAPE_16_9) },
                        label = { Text("۱۶:۹ افقی", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PeachContainer,
                            selectedLabelColor = PeachPrimary
                        ),
                        modifier = Modifier.height(32.dp).testTag("aspect_ratio_landscape_btn")
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = imageFitMode == ImageFitMode.FIT,
                        onClick = { onSetImageFitMode(ImageFitMode.FIT) },
                        label = { Text("کامل (Fit)", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftPinkContainer,
                            selectedLabelColor = SoftPink
                        ),
                        modifier = Modifier.height(32.dp)
                    )

                    FilterChip(
                        selected = imageFitMode == ImageFitMode.FILL,
                        onClick = { onSetImageFitMode(ImageFitMode.FILL) },
                        label = { Text("پر کردن (Crop)", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftPinkContainer,
                            selectedLabelColor = SoftPink
                        ),
                        modifier = Modifier.height(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Video Canvas Box with Aspect Ratio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio.ratioFloat)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF231C1A))
                    .border(BorderStroke(1.5.dp, PeachPrimary.copy(alpha = 0.4f)), RoundedCornerShape(20.dp))
                    .testTag("video_screen_canvas"),
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                val imageModel = remember(activeScene?.assetUri) {
                    com.example.model.SalonTourImageResolver.resolveImageModel(context, activeScene?.assetUri)
                }

                // Scene Visual Layer (Image or Stylized Placeholder)
                if (imageModel != null) {
                    AsyncImage(
                        model = imageModel,
                        contentDescription = "تصویر صحنه ${activeScene?.sceneId}",
                        contentScale = if (imageFitMode == ImageFitMode.FILL) ContentScale.Crop else ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Stylized Clear Placeholder with Visual Description
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PeachContainer.copy(alpha = 0.25f),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    tint = PeachPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "جاینگهدار تصویر صحنه",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFECE5)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = activeScene?.visualPromptDescription ?: "نمای داخلی سالن زیبایی و معرفی خدمات سالنیار",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD4C5C0),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(36.dp).testTag("canvas_pick_image_btn")
                        ) {
                            Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "انتخاب عکس از گوشی", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                // Subtitle Overlay positioned according to subtitleStyle
                if (subtitleStyle.isVisible) {
                    val subAlignment = when (subtitleStyle.position) {
                        SubtitlePosition.TOP -> Alignment.TopCenter
                        SubtitlePosition.CENTER -> Alignment.Center
                        SubtitlePosition.BOTTOM -> Alignment.BottomCenter
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(subAlignment)
                            .then(
                                if (subtitleStyle.position == SubtitlePosition.BOTTOM) {
                                    Modifier.background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color(0xCC000000))
                                        )
                                    )
                                } else if (subtitleStyle.position == SubtitlePosition.TOP) {
                                    Modifier.background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color(0xCC000000), Color.Transparent)
                                        )
                                    )
                                } else Modifier
                            )
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(subtitleStyle.backgroundColorHex)
                            ) {
                                Text(
                                    text = activeScene?.subtitleText ?: "زیرنویس هماهنگ با سخنرانی",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = subtitleStyle.fontSizeSp.sp,
                                        lineHeight = (subtitleStyle.fontSizeSp * 1.35f).sp
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(subtitleStyle.textColorHex),
                                    textAlign = TextAlign.Center,
                                    maxLines = subtitleStyle.maxLines,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Timecode badge
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = PeachPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                val currentSec = playbackState.currentPositionMs / 1000.0
                                Text(
                                    text = "${AudioStudioViewModel.formatSeconds(currentSec)} / ${AudioStudioViewModel.formatDuration(playbackState.totalDurationMs)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFFFECE5),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Quick Play/Pause overlay button at Center (if clicked)
                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(54.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color(0x88000000))
                ) {
                    Icon(
                        imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "پخش / توقف",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row below canvas: Change/Replace image for active scene
            if (activeScene?.assetUri != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تصویر اختصاصی صحنه فعال شد",
                        style = MaterialTheme.typography.labelSmall,
                        color = StateSuccess,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = IvorySurfaceVariant, contentColor = PeachPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp).testTag("replace_active_image_btn")
                    ) {
                        Text(text = "جایگزینی عکس این صحنه", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
