package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignCenter
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SubtitleBackgroundPreset
import com.example.model.SubtitleColorPreset
import com.example.model.SubtitlePosition
import com.example.model.SubtitleStyle
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SubtitleStyleEditorSection(
    subtitleStyle: SubtitleStyle,
    onSetFontSize: (Float) -> Unit,
    onSetTextColor: (Long) -> Unit,
    onSetBackgroundColor: (Long) -> Unit,
    onSetPosition: (SubtitlePosition) -> Unit,
    onToggleVisibility: () -> Unit,
    onSetMaxLines: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("subtitle_style_editor_section"),
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
                                imageVector = Icons.Default.Subtitles,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "شخصی‌سازی و استایل زیرنویس فارسی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "تنظیم فونت، رنگ، پس‌زمینه و جایگاه نمایش روی ویدیو",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (subtitleStyle.isVisible) "نمایش" else "مخفی",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (subtitleStyle.isVisible) PeachPrimary else TextMuted
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(
                        checked = subtitleStyle.isVisible,
                        onCheckedChange = { onToggleVisibility() },
                        colors = SwitchDefaults.colors(checkedThumbColor = PeachPrimary, checkedTrackColor = PeachContainer),
                        modifier = Modifier.testTag("subtitle_visibility_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Font Size Control
            Text(
                text = "اندازه قلم زیرنویس:",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val sizes = listOf("کوچک (۱۴)" to 14f, "استاندارد (۱۷)" to 17f, "بزرگ و تیتر (۲۱)" to 21f)
                sizes.forEach { (label, sizeVal) ->
                    val isSelected = subtitleStyle.fontSizeSp == sizeVal
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSetFontSize(sizeVal) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PeachContainer,
                            selectedLabelColor = PeachPrimary
                        ),
                        modifier = Modifier.testTag("font_size_chip_$sizeVal")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Color Preset Control
            Text(
                text = "رنگ متن زیرنویس:",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubtitleColorPreset.values().forEach { preset ->
                    val isSelected = subtitleStyle.textColorHex == preset.colorHex
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSetTextColor(preset.colorHex) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(preset.colorHex))
                                        .border(0.5.dp, Color.Gray, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(preset.labelPersian, style = MaterialTheme.typography.labelSmall, fontSize = 11.sp)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PeachContainer,
                            selectedLabelColor = PeachPrimary
                        ),
                        modifier = Modifier.testTag("color_chip_${preset.name}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Background Preset Control
            Text(
                text = "کادر و پس‌زمینه زیرنویس:",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubtitleBackgroundPreset.values().forEach { bgPreset ->
                    val isSelected = subtitleStyle.backgroundColorHex == bgPreset.colorHex
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSetBackgroundColor(bgPreset.colorHex) },
                        label = { Text(bgPreset.labelPersian, style = MaterialTheme.typography.labelSmall, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftPinkContainer,
                            selectedLabelColor = SoftPink
                        ),
                        modifier = Modifier.testTag("bg_chip_${bgPreset.name}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Vertical Position & Line Mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "جایگاه نمایش روی کادر:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        SubtitlePosition.values().forEach { pos ->
                            val isSelected = subtitleStyle.position == pos
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSetPosition(pos) },
                                label = { Text(pos.labelPersian.substringBefore(" "), style = MaterialTheme.typography.labelSmall, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PeachContainer,
                                    selectedLabelColor = PeachPrimary
                                ),
                                modifier = Modifier.testTag("position_chip_${pos.name}")
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = "حداکثر خطوط:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(1, 2).forEach { lines ->
                            FilterChip(
                                selected = subtitleStyle.maxLines == lines,
                                onClick = { onSetMaxLines(lines) },
                                label = { Text("$lines خط", style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoftPinkContainer,
                                    selectedLabelColor = SoftPink
                                ),
                                modifier = Modifier.testTag("max_lines_chip_$lines")
                            )
                        }
                    }
                }
            }
        }
    }
}
