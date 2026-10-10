package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SpeechSegment
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SegmentEditDialog(
    segment: SpeechSegment,
    onDismiss: () -> Unit,
    onSave: (SpeechSegment) -> Unit
) {
    var textDraft by remember { mutableStateOf(segment.text) }
    var startDraft by remember { mutableStateOf(segment.startTimeSec.toString()) }
    var endDraft by remember { mutableStateOf(segment.endTimeSec.toString()) }
    var topicDraft by remember { mutableStateOf(segment.topic) }
    var noteDraft by remember { mutableStateOf(segment.editNote) }
    var confirmedDraft by remember { mutableStateOf(segment.isConfirmed) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("segment_edit_dialog"),
        shape = RoundedCornerShape(22.dp),
        containerColor = IvorySurface,
        title = {
            Text(
                text = "ویرایش بخش گفتار و زمان‌بندی",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "متن بخش سخنرانی:",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = textDraft,
                    onValueChange = { textDraft = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_segment_text_field"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "شروع (ثانیه):",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = startDraft,
                            onValueChange = { startDraft = it },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "پایان (ثانیه):",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = endDraft,
                            onValueChange = { endDraft = it },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "موضوع یا مفهوم کلیدی:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = topicDraft,
                    onValueChange = { topicDraft = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "یادداشت ویرایش:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = noteDraft,
                    onValueChange = { noteDraft = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    placeholder = { Text("مثلاً: اصلاح تلفظ نام برند") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Checkbox(
                        checked = confirmedDraft,
                        onCheckedChange = { confirmedDraft = it },
                        colors = CheckboxDefaults.colors(checkedColor = PeachPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "این بخش توسط کاربر تأیید شد",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sTime = startDraft.toDoubleOrNull() ?: segment.startTimeSec
                    val eTime = endDraft.toDoubleOrNull() ?: segment.endTimeSec
                    val updated = segment.copy(
                        text = textDraft.trim(),
                        startTimeSec = sTime,
                        endTimeSec = eTime.coerceAtLeast(sTime),
                        topic = topicDraft.trim(),
                        editNote = noteDraft.trim(),
                        isConfirmed = confirmedDraft,
                        isApproximateTiming = false
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_segment_btn")
            ) {
                Text(text = "ذخیره تغییرات", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_segment_btn")
            ) {
                Text(text = "انصراف", color = TextSecondary)
            }
        }
    )
}
