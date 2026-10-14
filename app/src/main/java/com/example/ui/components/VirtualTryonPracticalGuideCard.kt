package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

/**
 * Step-by-step practical visual guide for virtual gown try-on and dream scenarios.
 */
@Composable
fun VirtualTryonPracticalGuideCard(
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    var activeStepIndex by remember { mutableStateOf(0) }

    val tryonSteps = listOf(
        Triple(
            "مرحله ۱: انتخاب سناریوی رویایی تالار و مراسم",
            "ابتدا موقعیت رویایی را با عروس هماهنگ کنید: رقص دو نفره وسط سن با نورپردازی مه، ورود تشریفاتی روی فرش قرمز، یا کنار ماشین عروس لوکس.",
            Icons.Default.Celebration
        ),
        Triple(
            "مرحله ۲: پرو و هماهنگی برش ۵۰ مدل لباس عروس",
            "از میان ۵۰ مدل لباس عروس ترند (اسکارلت، پرنسسی پف‌دار، دم‌ماهی، دانتل و بوهو)، لباسی که اندام را لاغرتر و کشیده‌تر نشان می‌دهد انتخاب کنید.",
            Icons.Default.Palette
        ),
        Triple(
            "مرحله ۳: هارمونی کت‌شلوار داماد (۳۵ مدل)",
            "استایل داماد را با عروس ست کنید؛ برای لباس عروس ساتن کلاسیک، تاکسیدو مشکی یقه شال و برای سبک‌های مدرن، کت‌شلوار دیپلمات برگزینید.",
            Icons.Default.CheckCircle
        ),
        Triple(
            "مرحله ۴: فیت میلی‌متری تاج و اکسسوری روی سر",
            "با استفاده از اسلایدرهای سایز (Scale) و موقعیت عمودی، تاج فرحی یا ریسه کریستالی را دقیقاً روی خط رویش شینیون فیکس نمایید.",
            Icons.Default.Diamond
        ),
        Triple(
            "مرحله ۵: اشتراک خروجی استوری ۹:۱۶ با برند سالن",
            "پیش‌نمایش ساخته‌شده را به صورت استوری برای همراهان یا تلگرام عروس بفرستید تا حس هیجان و اطمینان قطعی ایجاد شود.",
            Icons.Default.Share
        )
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D0E2E)),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFF8B5CF6)))),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFD4AF37), CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = null,
                            tint = Color(0xFF1B0B26)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "راهنمای عملی گام‌به‌گام پرو مجازی لباس و سناریو",
                            color = Color(0xFFECC34C),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "چگونه عروس را در کمتر از ۳ دقیقه شگفت‌زده و شیفته کار خود کنیم؟",
                            color = Color(0xFFD1C4E9),
                            fontSize = 11.sp
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFFD4AF37)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    // Try-On Visual Infographic Banner
                    Image(
                        painter = painterResource(id = R.drawable.img_virtual_tryon_guide),
                        contentDescription = "راهنمای تصویری پرو مجازی لباس عروس",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, Color(0xFF4C2A75), RoundedCornerShape(14.dp))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step Pills Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tryonSteps.forEachIndexed { idx, _ ->
                            val isCurrent = idx == activeStepIndex
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isCurrent) Color(0xFFD4AF37) else Color(0xFF2C1944))
                                    .clickable { activeStepIndex = idx }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "مرحله ${idx + 1}",
                                    color = if (isCurrent) Color(0xFF1B0B26) else Color(0xFFD1C4E9),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Active Step Detail Card
                    val currentStep = tryonSteps[activeStepIndex]
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF26123D)),
                        border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = currentStep.third,
                                    contentDescription = null,
                                    tint = Color(0xFFECC34C),
                                    modifier = Modifier.width(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentStep.first,
                                    color = Color(0xFFF7E7CE),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = currentStep.second,
                                color = Color(0xFFE9D5FF),
                                fontSize = 12.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
