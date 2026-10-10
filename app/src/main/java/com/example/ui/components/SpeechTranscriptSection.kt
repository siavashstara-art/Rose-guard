package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnalysisState
import com.example.model.SpeechSegment
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.OnPeachContainer
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.StateError
import com.example.ui.theme.StateErrorContainer
import com.example.ui.theme.StatePending
import com.example.ui.theme.StatePendingContainer
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AudioStudioViewModel

@Composable
fun SpeechTranscriptSection(
    segments: List<SpeechSegment>,
    analysisState: AnalysisState,
    rawTranscript: String,
    activeSegmentId: String?,
    onUpdateRawTranscript: (String) -> Unit,
    onProcessTranscript: (String) -> Unit,
    onLoadSampleTranscript: () -> Unit,
    onRequestCloudAnalysis: () -> Unit,
    onSeekAndPlay: (SpeechSegment) -> Unit,
    onEditSegment: (SpeechSegment) -> Unit,
    onToggleConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showManualInputBox by remember { mutableStateOf(false) }
    var textInputDraft by remember(rawTranscript) { mutableStateOf(rawTranscript) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("speech_transcript_section"),
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
            // Section Header
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
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "متن سخنرانی و رونویسی فارسی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "گام ۲ از ۵: تحلیل محتوا و زمان‌بندی جملات",
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
                        text = "${segments.size} بخش",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showManualInputBox = !showManualInputBox },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("toggle_transcript_input_btn"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PeachPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.TextFields,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showManualInputBox) "بستن متن خام" else "ورود متن",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                OutlinedButton(
                    onClick = onLoadSampleTranscript,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("load_sample_speech_btn"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SoftPink.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftPink)
                ) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "نمونه سالنیار",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Button(
                    onClick = onRequestCloudAnalysis,
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("request_cloud_analysis_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PeachPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "تحلیل AI",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // Analysis status feedback banner
            when (analysisState) {
                is AnalysisState.Processing -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PeachContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = analysisState.progressMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = OnPeachContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                is AnalysisState.Success -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = StateSuccessContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = analysisState.sourceDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = StateSuccess,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                is AnalysisState.Error -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = StateErrorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = analysisState.errorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = StateError,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                is AnalysisState.Idle -> {}
            }

            // Expandable Manual Input Box
            AnimatedVisibility(visible = showManualInputBox) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .background(IvorySurfaceVariant, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "متن کامل سخنرانی فارسی را وارد یا ویرایش کنید:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = textInputDraft,
                        onValueChange = {
                            textInputDraft = it
                            onUpdateRawTranscript(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .testTag("raw_transcript_text_field"),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        placeholder = {
                            Text("متن سخنرانی فارسی را در این بخش الصاق کنید...")
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            onProcessTranscript(textInputDraft)
                            showManualInputBox = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("apply_transcript_segmentation_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary)
                    ) {
                        Text(
                            text = "اعمال و زمان‌بندی جملات سخنرانی",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Speech Segment Cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                segments.forEachIndexed { index, segment ->
                    val isPlayingNow = segment.id == activeSegmentId

                    SpeechSegmentRowCard(
                        index = index + 1,
                        segment = segment,
                        isPlayingNow = isPlayingNow,
                        onPlayClick = { onSeekAndPlay(segment) },
                        onEditClick = { onEditSegment(segment) },
                        onToggleConfirm = { onToggleConfirm(segment.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SpeechSegmentRowCard(
    index: Int,
    segment: SpeechSegment,
    isPlayingNow: Boolean,
    onPlayClick: () -> Unit,
    onEditClick: () -> Unit,
    onToggleConfirm: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("speech_segment_card_${segment.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlayingNow) PeachContainer.copy(alpha = 0.5f) else IvorySurfaceVariant
        ),
        border = BorderStroke(
            width = if (isPlayingNow) 1.5.dp else 1.dp,
            color = if (isPlayingNow) PeachPrimary else IvoryCardBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Topic, Timestamps, and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isPlayingNow) PeachPrimary else SoftPinkContainer
                    ) {
                        Text(
                            text = "بخش $index",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isPlayingNow) Color.White else SoftPink,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    if (segment.topic.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = segment.topic,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Timing Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = IvorySurface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${AudioStudioViewModel.formatSeconds(segment.startTimeSec)} - ${AudioStudioViewModel.formatSeconds(segment.endTimeSec)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Exact Segment Text
            Text(
                text = segment.text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isPlayingNow) FontWeight.SemiBold else FontWeight.Normal,
                color = TextPrimary,
                lineHeight = 22.sp
            )

            if (segment.editNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "یادداشت ویرایش: ${segment.editNote}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action & Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play from this segment button
                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("play_segment_btn_${segment.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlayingNow) PeachPrimary else IvorySurface,
                        contentColor = if (isPlayingNow) Color.White else PeachPrimary
                    ),
                    border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.5f)),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "پخش از این بخش",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPlayingNow) "در حال پخش" else "پخش این بخش",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Confirmation status toggle
                    FilterChip(
                        selected = segment.isConfirmed,
                        onClick = onToggleConfirm,
                        label = {
                            Text(
                                text = if (segment.isConfirmed) "تأییدشده" else if (segment.isApproximateTiming) "زمان تقریبی" else "نیازمند بررسی",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            if (segment.isConfirmed) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StateSuccessContainer,
                            selectedLabelColor = StateSuccess,
                            containerColor = StatePendingContainer,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = segment.isConfirmed,
                            borderColor = if (segment.isConfirmed) StateSuccess.copy(alpha = 0.3f) else IvoryCardBorder
                        ),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("confirm_chip_${segment.id}")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Edit button
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("edit_segment_btn_${segment.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "ویرایش بخش",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
