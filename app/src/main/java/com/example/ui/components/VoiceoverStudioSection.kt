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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiVoicePersona
import com.example.model.SiavashAlimiriSpeech
import com.example.service.AiVoicePlayState

@Composable
fun VoiceoverStudioSection(
    playState: AiVoicePlayState,
    onTogglePlay: () -> Unit,
    onSelectPersona: (AiVoicePersona) -> Unit,
    onToggleEcho: () -> Unit,
    onToggleMusic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Header
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF231238)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFFA855F7))),
                    RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD4AF37), CircleShape)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = Color(0xFF1B0B26)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "سخنرانی مهندس سیاوش علی‌میری",
                        color = Color(0xFFF7E7CE),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "طراح ۲۵ نرم‌افزار جهانی، سازنده شهر متابورسی توانا و اکوسیستم سالن‌یار",
                        color = Color(0xFFD1C4E9),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Playback Card with Status & Audio FX
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF170E22)),
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.5.dp, Color(0xFFD4AF37)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status line
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = if (playState.isPlaying) Color(0xFF4ADE80) else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = playState.statusMessage,
                            color = Color(0xFFE9D5FF),
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF3B1E5C), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "بخش ${playState.currentParagraphIndex + 1} از ${playState.totalParagraphs}",
                            color = Color(0xFFD4AF37),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Play / Pause Button
                Button(
                    onClick = onTogglePlay,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (playState.isPlaying) Color(0xFFEF4444) else Color(0xFFD4AF37)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("toggle_voiceover_speech")
                ) {
                    Icon(
                        imageVector = if (playState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (playState.isPlaying) Color.White else Color(0xFF1B0B26)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (playState.isPlaying) "توقف پخش سخنرانی" else "پخش سخنرانی با صدای هوش مصنوعی انتخابی",
                        color = if (playState.isPlaying) Color.White else Color(0xFF1B0B26),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Audio FX Switches
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SpatialAudio, contentDescription = null, tint = Color(0xFFECC34C))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("اکو و ریورب ملایم", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("ایجاد عمق و طنین استودیویی", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                    Switch(
                        checked = playState.isEchoReverbEnabled,
                        onCheckedChange = { onToggleEcho() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color(0xFFECC34C))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("موسیقی ملایم پس‌زمینه", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("مناسب تیزرهای تجاری و لوکس", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                    Switch(
                        checked = playState.isBackgroundMusicEnabled,
                        onCheckedChange = { onToggleMusic() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AI Voice Persona Selection
        Text(
            text = "انتخاب صدای هوش مصنوعی (AI Voice Persona):",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "شما می‌توانید صدای گوینده متناسب با جلسه و سلیقه مدیر سالن را انتخاب کنید:",
            color = Color(0xFFD1C4E9),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AiVoicePersona.values().forEach { persona ->
                val isSelected = persona == playState.activePersona
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF491C73) else Color(0xFF191024)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectPersona(persona) }
                        .testTag("ai_persona_${persona.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(if (isSelected) Color(0xFFD4AF37) else Color(0xFF2C1A42), CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF1B0B26) else Color(0xFFECC34C)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = persona.titlePersian,
                                color = if (isSelected) Color.White else Color(0xFFF7E7CE),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = persona.descriptionPersian,
                                color = Color(0xFFD1C4E9),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "تنظیمات صوتی: ${persona.echoReverbDepth}",
                                color = Color(0xFFD4AF37),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Full Speech Text Reader Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C0F2B)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "متن کامل سخنرانی با مدیران سالن‌های زیبایی:",
                    color = Color(0xFFECC34C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = SiavashAlimiriSpeech.FULL_SPEECH_TEXT,
                    color = Color(0xFFEDE9FE),
                    fontSize = 12.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
