package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.VideoScene
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.StateError
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AudioStudioViewModel

@Composable
fun SceneEditorSection(
    scenes: List<VideoScene>,
    activeSceneId: String?,
    onAddScene: () -> Unit,
    onDeleteScene: (String) -> Unit,
    onMoveSceneUp: (String) -> Unit,
    onMoveSceneDown: (String) -> Unit,
    onUpdateSceneImage: (String, String) -> Unit,
    onRemoveSceneImage: (String) -> Unit,
    onSeekAndPlayScene: (VideoScene) -> Unit,
    onToggleApproval: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("scene_editor_section"),
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
            // Header with Add Scene Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = SoftPinkContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = SoftPink,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "مدیریت و ویرایش صحنه‌های ویدیو",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "انتخاب عکس از گوشی، تغییر ترتیب و اصلاح زمان‌ها",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Button(
                    onClick = onAddScene,
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("add_scene_btn"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "صحنه جدید", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scenes list
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                scenes.forEachIndexed { index, scene ->
                    val isCurrentPlaying = scene.sceneId == activeSceneId

                    SceneEditorCard(
                        index = index + 1,
                        isFirst = index == 0,
                        isLast = index == scenes.size - 1,
                        scene = scene,
                        isPlaying = isCurrentPlaying,
                        onMoveUp = { onMoveSceneUp(scene.sceneId) },
                        onMoveDown = { onMoveSceneDown(scene.sceneId) },
                        onDelete = { onDeleteScene(scene.sceneId) },
                        onPickImage = { uriStr -> onUpdateSceneImage(scene.sceneId, uriStr) },
                        onRemoveImage = { onRemoveSceneImage(scene.sceneId) },
                        onPlay = { onSeekAndPlayScene(scene) },
                        onToggleApproval = { onToggleApproval(scene.sceneId, !scene.isVisualApproved) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SceneEditorCard(
    index: Int,
    isFirst: Boolean,
    isLast: Boolean,
    scene: VideoScene,
    isPlaying: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit,
    onPickImage: (String) -> Unit,
    onRemoveImage: () -> Unit,
    onPlay: () -> Unit,
    onToggleApproval: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onPickImage(uri.toString())
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("scene_card_${scene.sceneId}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) PeachContainer.copy(alpha = 0.45f) else IvorySurfaceVariant
        ),
        border = BorderStroke(
            width = if (isPlaying) 1.5.dp else 1.dp,
            color = if (isPlaying) PeachPrimary else IvoryCardBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Scene Index, Type, Reorder & Delete actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isPlaying) PeachPrimary else PeachContainer
                    ) {
                        Text(
                            text = "صحنه $index",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isPlaying) Color.White else PeachPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IvorySurface
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${AudioStudioViewModel.formatSeconds(scene.startTimeSec)} - ${AudioStudioViewModel.formatSeconds(scene.endTimeSec)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Reorder & Delete buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = !isFirst,
                        modifier = Modifier.size(32.dp).testTag("move_up_${scene.sceneId}")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "انتقال به بالا", tint = if (isFirst) TextMuted else TextSecondary, modifier = Modifier.size(16.dp))
                    }

                    IconButton(
                        onClick = onMoveDown,
                        enabled = !isLast,
                        modifier = Modifier.size(32.dp).testTag("move_down_${scene.sceneId}")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "انتقال به پایین", tint = if (isLast) TextMuted else TextSecondary, modifier = Modifier.size(16.dp))
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp).testTag("delete_scene_${scene.sceneId}")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "حذف صحنه", tint = StateError, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Middle: Thumbnail and Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Image Thumbnail or Picker Box
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEDE4DC))
                        .clickable { photoPickerLauncher.launch("image/*") }
                        .testTag("scene_thumb_${scene.sceneId}"),
                    contentAlignment = Alignment.Center
                ) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val thumbModel = remember(scene.assetUri) {
                        com.example.model.SalonTourImageResolver.resolveImageModel(context, scene.assetUri)
                    }

                    if (thumbModel != null) {
                        AsyncImage(
                            model = thumbModel,
                            contentDescription = "تصویر صحنه",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "انتخاب عکس", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 9.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "زیرنویس: ${scene.subtitleText}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "شرح تصویر: ${scene.visualPromptDescription}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer action bar: Play scene, change photo, toggle approval
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPlay,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("play_scene_btn_${scene.sceneId}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlaying) PeachPrimary else IvorySurface,
                        contentColor = if (isPlaying) Color.White else PeachPrimary
                    ),
                    border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.5f)),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isPlaying) "در حال پخش" else "پخش این صحنه", style = MaterialTheme.typography.labelSmall)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (scene.assetUri != null) {
                        OutlinedButton(
                            onClick = onRemoveImage,
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, StateError.copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StateError)
                        ) {
                            Text(text = "حذف عکس", style = MaterialTheme.typography.labelSmall, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    FilterChip(
                        selected = scene.isVisualApproved,
                        onClick = onToggleApproval,
                        label = {
                            Text(
                                text = if (scene.isVisualApproved) "تصویر تأیید شد" else "نیازمند تأیید",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StateSuccessContainer,
                            selectedLabelColor = StateSuccess
                        ),
                        modifier = Modifier.height(32.dp)
                    )
                }
            }
        }
    }
}
