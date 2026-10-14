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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Psychology
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
 * Step-by-step practical visual guide for facial matching and closing hesitant salon clients.
 */
@Composable
fun FacialMatchingPracticalGuideCard(
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    var activeStepIndex by remember { mutableStateOf(0) }

    val guideSteps = listOf(
        Triple(
            "گام ۱: تشخیص هندسه چهره و تناسبات طلایی",
            "موهای مراجعه‌کننده را به پشت هدایت کرده و با اسلایدرهای زیر، فرم فک و پیشانی (بیضی، گرد، قلبی، مربعی، کشیده) را تعیین کنید تا تطابق بالای ۹۵٪ ثبت شود.",
            Icons.Default.Face
        ),
        Triple(
            "گام ۲: شناسایی آندرتون پوست و کرم‌پودر ضدآب",
            "رنگ رگ‌های مچ دست و تناژ پوست را بسنجید. برای آندرتون گرم رژ هلویی-طلایی و برای آندرتون سرد رنگ‌های رزی و نود مخملی بهترین ترکیب را ایجاد می‌کنند.",
            Icons.Default.AutoAwesome
        ),
        Triple(
            "گام ۳: کانتورینگ سینمایی و زاویه‌سازی متقارن",
            "با زاویه‌سازی شقیقه‌ها و سایه‌روشن در استخوان گونه، چهره در لنز دوربین‌های ۴K مراسم بی‌نقص جلوه کرده و نیازی به روتوش افراطی نخواهد داشت.",
            Icons.Default.CheckCircle
        ),
        Triple(
            "گام ۴: فیت کردن شینیون و تاج هماهنگ با قد داماد",
            "اگر قد عروس با داماد برابر است، شینیون پایین و ریسه ظریف؛ و اگر اختلاف قد زیاد است، شینیون خطی مرتفع همراه با تاج فرحی سلطنتی پیشنهاد دهید.",
            Icons.Default.Info
        ),
        Triple(
            "گام ۵: تکنیک قطعی‌سازی روانشناختی و دریافت بیعانه",
            "به مشتری نگویید «فکر کن خبر بده». بگویید: «با این تحلیل چهره، دقیقاً ۱ مدل اختصاصی داریم که انحصاری برای شما رزرو می‌کنیم. فقط تاریخ مراسمتان را ثبت کنیم تا تایم خالی پر نشود.»",
            Icons.Default.Psychology
        )
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0E30)),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFFA855F7)))),
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
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF1B0B26)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "راهنمای عملی گام‌به‌گام تطابق چهره و جذب مشتری",
                            color = Color(0xFFECC34C),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "راهنمای تصویری برای پرسنل جهت بستن قرارداد در همان جلسه اول",
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
                    // Infographic Image Banner
                    Image(
                        painter = painterResource(id = R.drawable.img_face_matching_guide),
                        contentDescription = "راهنمای آنالیز تصویری چهره",
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
                        guideSteps.forEachIndexed { idx, _ ->
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
                                    text = "گام ${idx + 1}",
                                    color = if (isCurrent) Color(0xFF1B0B26) else Color(0xFFD1C4E9),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Active Step Detail Card
                    val currentStep = guideSteps[activeStepIndex]
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
