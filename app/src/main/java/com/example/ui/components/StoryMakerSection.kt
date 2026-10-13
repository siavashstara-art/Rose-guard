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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.model.PrivacyMaskType
import com.example.model.StoryDraft
import com.example.model.StoryInteractiveSticker
import com.example.model.StoryTemplate

@Composable
fun StoryMakerSection(
    draft: StoryDraft,
    onSelectTemplate: (StoryTemplate) -> Unit,
    onUpdateTexts: (String, String) -> Unit,
    onToggleSticker: (StoryInteractiveSticker) -> Unit,
    onShareInstagram: () -> Unit,
    onShareTelegram: () -> Unit,
    onShareGeneral: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF261239)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFFE879F9))),
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
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color(0xFF1B0B26)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "استوریساز هوشمند اینستاگرام و تلگرام",
                        color = Color(0xFFF7E7CE),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "طراحی حرفه‌ای استوری با نسبت ۹:۱۶ و ارسال مستقیم با یک کلیک",
                        color = Color(0xFFD1C4E9),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 9:16 Canvas Live Preview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0717)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(9f / 16f)
                .align(Alignment.CenterHorizontally)
                .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(24.dp))
                .testTag("story_9_16_canvas_preview")
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2B1040), Color(0xFF12071C), Color(0xFF381552))
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Salon Top Badge
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF43196E), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "👑 ${draft.salonBrandName}",
                            color = Color(0xFFF7E7CE),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Template Tag
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFD4AF37), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = draft.template.badgeText,
                            color = Color(0xFF1B0B26),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Silhouette Artwork
                    Text(text = "✨ 👰 💎", fontSize = 48.sp)

                    if (draft.privacyMaskApplied != PrivacyMaskType.NONE) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xCC2A103D), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🌹 لایه حفاظتی: ${draft.privacyMaskApplied.titlePersian}",
                                color = Color(0xFFF5D77F),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Headline
                    Text(
                        text = draft.headline,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subheadline
                    Text(
                        text = draft.subheadline,
                        color = Color(0xFFD8C7EB),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Active Stickers
                    draft.activeStickers.forEach { sticker ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF45196B), RoundedCornerShape(8.dp))
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text(
                                text = "${sticker.visualEmoji} ${sticker.titlePersian}",
                                color = Color(0xFFECC34C),
                                fontSize = 10.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Footer Contact
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xE6150921), RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📱 مشاوره و رزرو: ${draft.phoneContact}",
                                color = Color(0xFFD4AF37),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "اینستاگرام: ${draft.instagramHandle}",
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Template Selection Chips
        Text(
            text = "انتخاب قالب استوری:",
            color = Color(0xFFECC34C),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(StoryTemplate.values()) { tmpl ->
                val isSelected = tmpl == draft.template
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF531E82) else Color(0xFF191024)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .clickable { onSelectTemplate(tmpl) }
                        .testTag("story_template_${tmpl.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = tmpl.titlePersian,
                            color = if (isSelected) Color.White else Color(0xFFE9D5FF),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = tmpl.badgeText,
                            color = Color(0xFFD4AF37),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Headline & Subheadline Inputs
        Text(
            text = "شخصی‌سازی متن‌های استوری:",
            color = Color(0xFFECC34C),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = draft.headline,
            onValueChange = { onUpdateTexts(it, draft.subheadline) },
            label = { Text("تیتر اصلی استوری", color = Color(0xFFC4B5FD)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFD4AF37),
                unfocusedBorderColor = Color(0xFF4A2F6E)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = draft.subheadline,
            onValueChange = { onUpdateTexts(draft.headline, it) },
            label = { Text("توضیحات فرعی استوری", color = Color(0xFFC4B5FD)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFD4AF37),
                unfocusedBorderColor = Color(0xFF4A2F6E)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stickers selector
        Text(
            text = "استیکرهای تعاملی روی استوری:",
            color = Color(0xFFECC34C),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            StoryInteractiveSticker.values().forEach { sticker ->
                val isActive = draft.activeStickers.contains(sticker)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isActive) Color(0xFF44186B) else Color(0xFF191024),
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (isActive) Color(0xFFD4AF37) else Color(0xFF38234D),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { onToggleSticker(sticker) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = sticker.visualEmoji, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = sticker.titlePersian,
                        color = if (isActive) Color.White else Color(0xFFD1C4E9),
                        fontSize = 12.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    if (isActive) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFFD4AF37)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Share Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onShareInstagram,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("share_instagram_button")
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("اینستاگرام", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
            }

            Button(
                onClick = onShareTelegram,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF229ED9)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("share_telegram_button")
            ) {
                Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("تلگرام", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
            }

            Button(
                onClick = onShareGeneral,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("share_general_button")
            ) {
                Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF1B0B26))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ارسال", fontWeight = FontWeight.Bold, color = Color(0xFF1B0B26), fontSize = 12.sp)
            }
        }
    }
}
