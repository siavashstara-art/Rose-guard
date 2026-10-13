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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PrivacyMaskType
import com.example.model.TransformationArchive
import com.example.model.TransformationRecord

@Composable
fun BeforeAfterSection(
    selectedRecord: TransformationRecord,
    activeMask: PrivacyMaskType,
    splitRatio: Float,
    onSplitChange: (Float) -> Unit,
    onSelectMask: (PrivacyMaskType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF26143A)),
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
                        imageVector = Icons.Default.Compare,
                        contentDescription = null,
                        tint = Color(0xFF1B0B26)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "ثبت قبل و بعد با لایه‌های حفظ حریم خصوصی",
                        color = Color(0xFFF7E7CE),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "نقاب‌های خلیجی با گل رز، ماسک‌های بالماسکه بدون کاهش جلوه شینیون",
                        color = Color(0xFFD1C4E9),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Interactive Before-After Split Canvas
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF160E21)),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(22.dp))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Split Display: Left is After, Right is Before
                Row(modifier = Modifier.fillMaxSize()) {
                    // AFTER section
                    Box(
                        modifier = Modifier
                            .weight(splitRatio)
                            .fillMaxHeight()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF381552), Color(0xFF1C092C), Color(0xFF451A66))
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "✨ بعد از میکاپ VIP",
                                color = Color(0xFFECC34C),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "👑 شینیون خطی ژورنالی\n💎 کانتورینگ سینمایی\n🌹 فیکس ضدآب ۱۸ ساعته",
                                color = Color.White,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )

                            // Show active privacy mask overlay if selected
                            if (activeMask != PrivacyMaskType.NONE) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xD90D0517), RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "🛡️ ${activeMask.titlePersian}",
                                        color = Color(0xFFF7E7CE),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Divider Line
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFD4AF37))
                    )

                    // BEFORE section
                    Box(
                        modifier = Modifier
                            .weight(1f - splitRatio)
                            .fillMaxHeight()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF1F1B24), Color(0xFF121016), Color(0xFF28232F))
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "📷 قبل از شروع کار",
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "چهره نچرال مشتری\nزیرسازی پوست\nثبت آرشیو سالن",
                                color = Color(0xFFAFAFAF),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Slider Control at Bottom
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color(0xCC000000))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "👈 اسلایدر را برای مقایسه قبل و بعد جابجا کنید 👉",
                            color = Color(0xFFD1C4E9),
                            fontSize = 11.sp
                        )
                        Slider(
                            value = splitRatio,
                            onValueChange = onSplitChange,
                            valueRange = 0.1f..0.9f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFD4AF37),
                                activeTrackColor = Color(0xFFD4AF37),
                                inactiveTrackColor = Color(0xFF553C7B)
                            ),
                            modifier = Modifier.testTag("before_after_slider")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Privacy Masks Section (Core Feature 3)
        Text(
            text = "لایه‌های حفاظتی و حریم خصوصی چهره:",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "برای مشتریانی که مایل به اشتراک عکس کامل نیستند، لایه‌های لوکس زیر را فعال کنید:",
            color = Color(0xFFD1C4E9),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PrivacyMaskType.values().forEach { mask ->
                val isSelected = mask == activeMask
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF45196B) else Color(0xFF1A1124)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectMask(mask) }
                        .testTag("privacy_mask_${mask.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) Color(0xFFD4AF37) else Color(0xFF2E1B44),
                                    CircleShape
                                )
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = if (mask == PrivacyMaskType.NONE) Icons.Default.Collections else Icons.Default.Shield,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF1B0B26) else Color(0xFFECC34C)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mask.titlePersian,
                                color = if (isSelected) Color.White else Color(0xFFF7E7CE),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = mask.descriptionPersian,
                                color = Color(0xFFC4B5FD),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "ویژگی: ${mask.motifStyle}",
                                color = Color(0xFFECC34C),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
