package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioFileInfo
import com.example.model.AudioPlaybackUiState
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
fun AudioPlayerCard(
    audioInfo: AudioFileInfo,
    playbackState: AudioPlaybackUiState,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onReplayFromStart: () -> Unit,
    onChangeAudio: () -> Unit,
    onRemoveAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isUserSeeking by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0f) }

    val currentPosition = if (isUserSeeking) {
        (sliderValue * playbackState.totalDurationMs).toLong()
    } else {
        playbackState.currentPositionMs
    }

    val progressFraction = if (playbackState.totalDurationMs > 0) {
        (currentPosition.toFloat() / playbackState.totalDurationMs).coerceIn(0f, 1f)
    } else 0f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("audio_player_card"),
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
            // Header: Title and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PeachContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "آیکون صوت",
                                tint = PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = audioInfo.fileName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "صوت سخنرانی بارگذاری‌شده",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Active status pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (playbackState.isPlaying) PeachContainer else StateSuccessContainer
                ) {
                    Text(
                        text = if (playbackState.isPlaying) "در حال پخش" else "آماده برای پردازش",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (playbackState.isPlaying) OnPeachContainer else StateSuccess,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Metadata Chips (Format, Duration, Size)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetadataPill(
                    label = "فرمت",
                    value = audioInfo.formatDescription,
                    containerColor = IvorySurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                MetadataPill(
                    label = "مدت زمان",
                    value = AudioStudioViewModel.formatDuration(audioInfo.durationMs),
                    containerColor = SoftPinkContainer,
                    modifier = Modifier.weight(1f)
                )
                MetadataPill(
                    label = "حجم فایل",
                    value = AudioStudioViewModel.formatFileSize(audioInfo.fileSizeBytes),
                    containerColor = IvorySurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Playback Slider & Timers
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IvorySurfaceVariant, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Slider(
                    value = if (isUserSeeking) sliderValue else progressFraction,
                    onValueChange = {
                        isUserSeeking = true
                        sliderValue = it
                    },
                    onValueChangeFinished = {
                        val targetMs = (sliderValue * playbackState.totalDurationMs).toLong()
                        onSeekTo(targetMs)
                        isUserSeeking = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("audio_seek_slider"),
                    colors = SliderDefaults.colors(
                        thumbColor = PeachPrimary,
                        activeTrackColor = PeachPrimary,
                        inactiveTrackColor = PeachContainer
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AudioStudioViewModel.formatDuration(currentPosition),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = AudioStudioViewModel.formatDuration(audioInfo.durationMs),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Playback Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Replay Button
                IconButton(
                    onClick = onReplayFromStart,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("audio_replay_button"),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = IvorySurfaceVariant,
                        contentColor = TextSecondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "پخش مجدد از ابتدا",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Main Play/Pause Button
                Button(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .testTag("audio_play_pause_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PeachPrimary,
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (playbackState.isPlaying) "توقف موقت" else "پخش صدا",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Remove audio from project Button
                IconButton(
                    onClick = onRemoveAudio,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("audio_remove_button"),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = IvorySurfaceVariant,
                        contentColor = SoftPink
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "حذف فایل از پروژه",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action row: Change File or Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onChangeAudio,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("change_audio_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PeachPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "انتخاب فایل دیگر",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                FilledTonalButton(
                    onClick = onRemoveAudio,
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("remove_project_audio_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = SoftPinkContainer,
                        contentColor = SoftPink
                    )
                ) {
                    Text(
                        text = "حذف از پروژه",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun MetadataPill(
    label: String,
    value: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
