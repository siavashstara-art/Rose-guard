package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BackgroundMusicTrack
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun AudioMixerAndMusicSection(
    speechVolume: Float,
    isNoiseFilterActive: Boolean,
    musicTrack: BackgroundMusicTrack,
    onSetSpeechVolume: (Float) -> Unit,
    onToggleNoiseFilter: () -> Unit,
    onSetMusicVolume: (Float) -> Unit,
    onToggleMusicMute: () -> Unit,
    onToggleFadeInOut: () -> Unit,
    onPickCustomMusic: (Uri) -> Unit,
    onRemoveCustomMusic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val musicFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onPickCustomMusic(uri)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("audio_mixer_section"),
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
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "تنظیم حرفه‌ای صدا و موسیقی پس‌زمینه",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "ترکیب صدای گوینده و موسیقی ملایم تجاری سالنیار",
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
                        text = "میکس همزمان",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PeachPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 1: Main Speech Voice Controls
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = IvorySurfaceVariant,
                border = BorderStroke(1.dp, IvoryCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "بلندی صدای اصلی سخنران:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "${(speechVolume * 100).toInt()}٪",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PeachPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Slider(
                        value = speechVolume,
                        onValueChange = onSetSpeechVolume,
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = PeachPrimary,
                            activeTrackColor = PeachPrimary,
                            inactiveTrackColor = PeachContainer
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("speech_volume_slider")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Speech clarity filter toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "فیلتر شفاف‌سازی کلام و کاهش نویز محیطی",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "بدون تغییر در فایل اصلی؛ اعمال در پیش‌نمایش",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = isNoiseFilterActive,
                            onCheckedChange = { onToggleNoiseFilter() },
                            colors = SwitchDefaults.colors(checkedThumbColor = PeachPrimary, checkedTrackColor = PeachContainer),
                            modifier = Modifier.testTag("noise_filter_switch")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Background Music Controls
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = IvorySurfaceVariant,
                border = BorderStroke(1.dp, IvoryCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = SoftPink, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "موسیقی پس‌زمینه (بیکلام):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (musicTrack.isMuted) "خاموش" else "${(musicTrack.volume * 100).toInt()}٪",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (musicTrack.isMuted) TextMuted else SoftPink
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = musicTrack.isMuted,
                                onClick = onToggleMusicMute,
                                label = { Text(if (musicTrack.isMuted) "صدا قطع است" else "صدا فعال است", fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (musicTrack.isMuted) Icons.Default.MusicOff else Icons.Default.MusicNote,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoftPinkContainer,
                                    selectedLabelColor = SoftPink
                                ),
                                modifier = Modifier.height(30.dp).testTag("music_mute_toggle_btn")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "عنوان آهنگ: ${musicTrack.title}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Music Volume Slider (defaults to 25% to keep speech clear)
                    Slider(
                        value = if (musicTrack.isMuted) 0f else musicTrack.volume,
                        onValueChange = onSetMusicVolume,
                        valueRange = 0f..1f,
                        enabled = !musicTrack.isMuted,
                        colors = SliderDefaults.colors(
                            thumbColor = SoftPink,
                            activeTrackColor = SoftPink,
                            inactiveTrackColor = SoftPinkContainer
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("music_volume_slider")
                    )

                    // Licensing & Commercial safety card
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = IvorySurface,
                        border = BorderStroke(1.dp, IvoryCardBorder.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = StateSuccess, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "وضعیت حق نشر و کپی‌رایت:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = StateSuccess
                                )
                                Text(
                                    text = musicTrack.licenseNotice,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action buttons: Pick custom music from phone or restore
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { musicFilePicker.launch("audio/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = IvorySurface, contentColor = TextPrimary),
                            border = BorderStroke(1.dp, IvoryCardBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(36.dp).testTag("pick_custom_music_btn")
                        ) {
                            Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "انتخاب موسیقی از گوشی", style = MaterialTheme.typography.labelSmall)
                        }

                        if (musicTrack.uriString != null) {
                            OutlinedButton(
                                onClick = onRemoveCustomMusic,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(text = "بازگشت به پیانوی پیش‌فرض", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
