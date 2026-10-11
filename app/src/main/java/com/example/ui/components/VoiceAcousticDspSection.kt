package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
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
import com.example.model.CuratedMusicCatalog
import com.example.model.CuratedMusicTrack
import com.example.model.RoomSizePreset
import com.example.model.VoiceAcousticSettings
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

/**
 * Audio control console for:
 * 1. Subtle Echo & Engaging Room Reverb DSP on the speaker's voice (کلام نافذ)
 * 2. Gentle, peaceful background underscore music (زیرصدای آرامش‌بخش)
 */
@Composable
fun VoiceAcousticDspSection(
    acousticSettings: VoiceAcousticSettings,
    curatedTracks: List<CuratedMusicTrack>,
    selectedTrackId: String,
    musicTrack: BackgroundMusicTrack,
    speechVolume: Float,
    isNoiseFilterActive: Boolean,
    onSetVoiceAcousticEnabled: (Boolean) -> Unit,
    onSetRoomSizePreset: (RoomSizePreset) -> Unit,
    onSetEchoLevel: (Float) -> Unit,
    onSetRoomSizeAmount: (Float) -> Unit,
    onSetVocalWarmth: (Float) -> Unit,
    onSetAutoDucking: (Boolean) -> Unit,
    onSelectCuratedTrack: (CuratedMusicTrack) -> Unit,
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
            .testTag("voice_acoustic_dsp_section"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        border = BorderStroke(1.2.dp, IvoryCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = SoftPinkContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SpatialAudio,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "تنظیمات صدای نافذ و زیرصدای آرامش‌بخش",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "اکوی ملایم، روم‌سایز گیرا و موسیقی ملایم مدیران",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PeachContainer
                ) {
                    Text(
                        text = "جلوه صوتی زنده",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PeachPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // PART 1: VOCAL ACOUSTIC REVERB & SUBTLE ECHO DSP
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = IvorySurfaceVariant),
                border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Master Reverb Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "اکوی ملایم و روم‌سایز گیرا (Voice Reverb & Echo)",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "طنین دلنشین آکوستیک برای نفوذ بیشتر کلام سخنران",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Switch(
                            checked = acousticSettings.isEnabled,
                            onCheckedChange = onSetVoiceAcousticEnabled,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = PeachPrimary
                            ),
                            modifier = Modifier.testTag("voice_reverb_toggle")
                        )
                    }

                    AnimatedVisibility(
                        visible = acousticSettings.isEnabled,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(modifier = Modifier.padding(top = 14.dp)) {
                            // Room Size Presets
                            Text(
                                text = "فضا و اندازه اتاق آکوستیک (Room Size Preset):",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                RoomSizePreset.values().forEach { preset ->
                                    val isSelected = acousticSettings.roomSize == preset
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { onSetRoomSizePreset(preset) },
                                        label = {
                                            Text(
                                                text = preset.labelPersian,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PeachContainer,
                                            selectedLabelColor = PeachPrimary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Subtle Echo Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "میزان اکوی ملایم (Subtle Echo & Reflections):",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${(acousticSettings.echoLevel * 100).toInt()}% (ملایم و طبیعی)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PeachPrimary
                                )
                            }

                            Slider(
                                value = acousticSettings.echoLevel,
                                onValueChange = onSetEchoLevel,
                                valueRange = 0.05f..0.60f,
                                colors = SliderDefaults.colors(
                                    thumbColor = PeachPrimary,
                                    activeTrackColor = PeachPrimary,
                                    inactiveTrackColor = PeachContainer
                                ),
                                modifier = Modifier.testTag("echo_slider")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Room Size Depth Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "عمق و طنین اتاق (Room Size & Decay):",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${(acousticSettings.roomSizeAmount * 100).toInt()}% (حس حضور گیرا)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PeachPrimary
                                )
                            }

                            Slider(
                                value = acousticSettings.roomSizeAmount,
                                onValueChange = onSetRoomSizeAmount,
                                valueRange = 0.10f..0.80f,
                                colors = SliderDefaults.colors(
                                    thumbColor = PeachPrimary,
                                    activeTrackColor = PeachPrimary,
                                    inactiveTrackColor = PeachContainer
                                ),
                                modifier = Modifier.testTag("room_size_slider")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Vocal Warmth / Bass Presence Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "گرمای صدا و صمیمیت (Vocal Warmth / Proximity):",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${(acousticSettings.vocalWarmth * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PeachPrimary
                                )
                            }

                            Slider(
                                value = acousticSettings.vocalWarmth,
                                onValueChange = onSetVocalWarmth,
                                valueRange = 0.0f..0.80f,
                                colors = SliderDefaults.colors(
                                    thumbColor = PeachPrimary,
                                    activeTrackColor = PeachPrimary,
                                    inactiveTrackColor = PeachContainer
                                ),
                                modifier = Modifier.testTag("warmth_slider")
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Smart Auto-Ducking Switch
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "داکینگ هوشمند (Auto-Ducking)",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "کاهش خودکار ولوم موسیقی حین گفتار گوینده برای وضوح حداکثری کلام",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }

                                Switch(
                                    checked = acousticSettings.autoDucking,
                                    onCheckedChange = onSetAutoDucking,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = PeachPrimary
                                    ),
                                    modifier = Modifier.testTag("auto_ducking_toggle")
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // PART 2: PEACEFUL & RELAXING UNDERSCORE MUSIC CATALOG
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = IvorySurfaceVariant),
                border = BorderStroke(1.dp, IvoryCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "موسیقی ملایم و آرامش‌بخش زیرصدا",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StateSuccessContainer
                        ) {
                            Text(
                                text = "مجوز آزاد تجاری (CC0)",
                                style = MaterialTheme.typography.labelSmall,
                                color = StateSuccess,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "انتخاب تراک آرامش‌بخش متناسب با حس سخنرانی:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Curated Tracks List
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        curatedTracks.forEach { track ->
                            val isSelected = track.id == selectedTrackId
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectCuratedTrack(track) }
                                    .testTag("curated_track_${track.id}"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PeachContainer else IvorySurface,
                                border = BorderStroke(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) PeachPrimary else IvoryCardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isSelected) PeachPrimary else IvorySurfaceVariant,
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = if (isSelected) Icons.Default.MusicNote else Icons.Default.LibraryMusic,
                                                    contentDescription = null,
                                                    tint = if (isSelected) Color.White else TextSecondary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column {
                                            Text(
                                                text = track.title,
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = track.persianMood,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isSelected) PeachPrimary else TextSecondary,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = PeachPrimary
                                        ) {
                                            Text(
                                                text = "در حال پخش",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Music Volume Slider (Underscore levels)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "بلندی صدای موسیقی (پایین‌تر از سخنران):",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary
                        )
                        Text(
                            text = if (musicTrack.isMuted) "بی‌صدا (Mute)" else "${(musicTrack.volume * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (musicTrack.isMuted) TextMuted else PeachPrimary
                        )
                    }

                    Slider(
                        value = musicTrack.volume,
                        onValueChange = onSetMusicVolume,
                        valueRange = 0f..1f,
                        enabled = !musicTrack.isMuted,
                        colors = SliderDefaults.colors(
                            thumbColor = PeachPrimary,
                            activeTrackColor = PeachPrimary,
                            inactiveTrackColor = PeachContainer
                        ),
                        modifier = Modifier.testTag("music_volume_slider")
                    )

                    // Quick volume preset buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = !musicTrack.isMuted && musicTrack.volume in 0.15f..0.22f,
                            onClick = {
                                if (musicTrack.isMuted) onToggleMusicMute()
                                onSetMusicVolume(0.20f)
                            },
                            label = { Text("زیرصدای ملایم (۲۰٪)", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp) },
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = !musicTrack.isMuted && musicTrack.volume in 0.28f..0.35f,
                            onClick = {
                                if (musicTrack.isMuted) onToggleMusicMute()
                                onSetMusicVolume(0.30f)
                            },
                            label = { Text("متوسط (۳۰٪)", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp) },
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = musicTrack.isMuted,
                            onClick = onToggleMusicMute,
                            label = { Text("قطع صدا", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp) },
                            modifier = Modifier.weight(0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Custom Music Picker from storage
                    OutlinedButton(
                        onClick = { musicFilePicker.launch("audio/*") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("انتخاب موسیقی دلخواه از حافظه گوشی", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
