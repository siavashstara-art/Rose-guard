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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BodyAnatomy
import com.example.model.FaceShape
import com.example.model.HesitantRecommendation
import com.example.model.SkinTone

@Composable
fun HesitantClientSection(
    selectedFaceShape: FaceShape,
    selectedBodyAnatomy: BodyAnatomy,
    selectedSkinTone: SkinTone,
    recommendation: HesitantRecommendation,
    onSelectFace: (FaceShape) -> Unit,
    onSelectBody: (BodyAnatomy) -> Unit,
    onSelectSkin: (SkinTone) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Banner
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF261536)
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFF8B5CF6))),
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF1B0B26)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "موتور استایل‌شناسی جذب مشتریان مردد",
                        color = Color(0xFFF7E7CE),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "تحلیل آفلاین آناتومی، رفع دودلی عروس و قطعی‌کردن قرارداد در اولین مراجعه",
                        color = Color(0xFFD1C4E9),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Practical Step-by-Step Visual Consultation Guide
        FacialMatchingPracticalGuideCard()

        Spacer(modifier = Modifier.height(20.dp))

        // Step 1: Face Shape
        Text(
            text = "۱. فرم چهره مشتری (Face Shape):",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(FaceShape.values()) { face ->
                val isSelected = face == selectedFaceShape
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF4A1E73) else Color(0xFF191023)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .clickable { onSelectFace(face) }
                        .testTag("face_shape_${face.name}")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFFD4AF37) else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = face.titlePersian,
                                color = if (isSelected) Color.White else Color(0xFFD1C4E9),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step 2: Body Anatomy
        Text(
            text = "۲. آناتومی اندام عروس (Body Anatomy):",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(BodyAnatomy.values()) { body ->
                val isSelected = body == selectedBodyAnatomy
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF4A1E73) else Color(0xFF191023)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .clickable { onSelectBody(body) }
                        .testTag("body_anatomy_${body.name}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFFD4AF37) else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = body.titlePersian,
                            color = if (isSelected) Color.White else Color(0xFFD1C4E9),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step 3: Skin Tone
        Text(
            text = "۳. تناژ رنگ پوست (Skin Tone):",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SkinTone.values().forEach { skin ->
                val isSelected = skin == selectedSkinTone
                SuggestionChip(
                    onClick = { onSelectSkin(skin) },
                    label = {
                        Text(
                            text = skin.titlePersian,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (isSelected) Color(0xFFD4AF37) else Color(0xFF1F132C),
                        labelColor = if (isSelected) Color(0xFF150820) else Color(0xFFE9D5FF)
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = if (isSelected) Color(0xFFD4AF37) else Color(0xFF452C63)
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Results Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E102A)
            ),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.5.dp,
                    Brush.verticalGradient(
                        listOf(Color(0xFFD4AF37), Color(0xFF581C87), Color(0xFF1E102A))
                    ),
                    RoundedCornerShape(22.dp)
                )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4ADE80)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "نتایج استایل‌شناسی علمی اختصاصی",
                            color = Color(0xFFF7E7CE),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Match badge
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFD4AF37), Color(0xFFF59E0B))
                                ),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${recommendation.matchPercentage}٪ تطابق چهره",
                            color = Color(0xFF160924),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                RecommendationItem(
                    label = "مدل شینیون برگزیده:",
                    value = recommendation.recommendedHairstyle,
                    rationale = recommendation.hairstyleReason
                )

                RecommendationItem(
                    label = "سبک میکاپ و کانتورینگ:",
                    value = recommendation.recommendedMakeup,
                    rationale = recommendation.makeupReason
                )

                RecommendationItem(
                    label = "مدل برش لباس عروس:",
                    value = recommendation.recommendedDressCut,
                    rationale = recommendation.dressReason
                )

                RecommendationItem(
                    label = "اکسسوری و تاج مناسب:",
                    value = recommendation.recommendedCrownType,
                    rationale = recommendation.crownReason
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Closer tip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF32174A), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFECC34C)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "راهبرد قطعی‌کردن قرارداد توسط مدیر سالن:",
                                color = Color(0xFFF5D77F),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = recommendation.psychologicalCloserTip,
                            color = Color(0xFFEDE9FE),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationItem(
    label: String,
    value: String,
    rationale: String
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            color = Color(0xFFECC34C),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = rationale,
            color = Color(0xFFC4B5FD),
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
