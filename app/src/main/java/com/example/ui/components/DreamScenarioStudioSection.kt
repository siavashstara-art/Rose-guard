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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.model.BridalCrownCatalog
import com.example.model.BridalCrown
import com.example.model.BridalGownModel
import com.example.model.DreamScenario
import com.example.model.GroomSuitModel
import com.example.model.WardrobeCatalog

@Composable
fun DreamScenarioStudioSection(
    selectedScenario: DreamScenario,
    selectedGown: BridalGownModel,
    selectedSuit: GroomSuitModel,
    selectedCrown: BridalCrown,
    crownScale: Float,
    crownOffsetY: Float,
    onSelectScenario: (DreamScenario) -> Unit,
    onSelectGown: (BridalGownModel) -> Unit,
    onSelectSuit: (GroomSuitModel) -> Unit,
    onSelectCrown: (BridalCrown) -> Unit,
    onScaleCrown: (Float) -> Unit,
    onOffsetYCrown: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF231135)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFFC084FC))),
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
                        imageVector = Icons.Default.Celebration,
                        contentDescription = null,
                        tint = Color(0xFF1B0B26)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "موتور سناریوساز رویایی و فتوشاپ عروس و داماد",
                        color = Color(0xFFF7E7CE),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "۵۰ مدل لباس عروس ترند + ۳۵ مدل کت‌شلوار داماد در ۳ موقعیت رویایی",
                        color = Color(0xFFD1C4E9),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3 Dream Scenarios Selector (Core Feature 5)
        Text(
            text = "انتخاب موقعیت رویایی سناریو (Dream Scenario):",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DreamScenario.values().forEach { sc ->
                val isSelected = sc == selectedScenario
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF45196B) else Color(0xFF191023)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectScenario(sc) }
                        .testTag("scenario_${sc.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when (sc) {
                            DreamScenario.DANCE_STAGE -> Icons.Default.Celebration
                            DreamScenario.GRAND_ENTRANCE -> Icons.Default.MeetingRoom
                            DreamScenario.WEDDING_CAR -> Icons.Default.DirectionsCar
                        }
                        Box(
                            modifier = Modifier
                                .background(if (isSelected) Color(0xFFD4AF37) else Color(0xFF2E1B44), CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF1B0B26) else Color(0xFFECC34C)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sc.titlePersian,
                                color = if (isSelected) Color.White else Color(0xFFF7E7CE),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = sc.subtitlePersian,
                                color = Color(0xFFD1C4E9),
                                fontSize = 11.sp
                            )
                            Text(
                                text = "حس محیطی: ${sc.ambientDescription}",
                                color = Color(0xFFC4B5FD),
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Visual Dream Simulation Stage Box
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13091F)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🌟 پیش‌نمایش موقعیت انتخابی زوجین 🌟",
                    color = Color(0xFFECC34C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF3B165C), Color(0xFF1A092B), Color(0xFF2C1045))
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "👰 💍 🤵",
                            fontSize = 42.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedScenario.titlePersian,
                            color = Color(0xFFF7E7CE),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "نورپردازی: ${selectedScenario.lightingMood}",
                            color = Color(0xFFD4AF37),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "لباس عروس: ${selectedGown.namePersian}\nکت‌شلوار داماد: ${selectedSuit.namePersian}\nتاج عروس: ${selectedCrown.namePersian}",
                            color = Color(0xFFE9D5FF),
                            fontSize = 11.sp,
                            lineHeight = 17.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 50 Bridal Gowns Carousel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "کالکشن ۵۰ مدل لباس عروس ترند روز:",
                color = Color(0xFFECC34C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${selectedGown.id} / ۵۰ مدل",
                color = Color(0xFFD1C4E9),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(WardrobeCatalog.BRIDAL_50_GOWNS) { gown ->
                val isSelected = gown == selectedGown
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF531E82) else Color(0xFF1B1126)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .clickable { onSelectGown(gown) }
                        .testTag("gown_${gown.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = gown.id,
                            color = Color(0xFFD4AF37),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = gown.namePersian,
                            color = if (isSelected) Color.White else Color(0xFFE2D9F3),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = gown.fabricType,
                            color = Color(0xFFB8A4D6),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 35 Groom Suits Carousel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "کالکشن ۳۵ مدل کت‌شلوار داماد ترند:",
                color = Color(0xFFECC34C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${selectedSuit.id} / ۳۵ مدل",
                color = Color(0xFFD1C4E9),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(WardrobeCatalog.GROOM_35_SUITS) { suit ->
                val isSelected = suit == selectedSuit
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF531E82) else Color(0xFF1B1126)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .clickable { onSelectSuit(suit) }
                        .testTag("suit_${suit.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = suit.id,
                            color = Color(0xFFD4AF37),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = suit.namePersian,
                            color = if (isSelected) Color.White else Color(0xFFE2D9F3),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "رنگ: ${suit.colorTone}",
                            color = Color(0xFFB8A4D6),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Crown & Accessories Smart Fitting (Core Feature 4)
        Text(
            text = "فیت هوشمند تاج و اکسسوری روی سر عروس:",
            color = Color(0xFFECC34C),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(BridalCrownCatalog.CROWNS) { crown ->
                val isSelected = crown == selectedCrown
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF531E82) else Color(0xFF1B1126)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 0.8.dp,
                        if (isSelected) Color(0xFFD4AF37) else Color(0xFF38234D)
                    ),
                    modifier = Modifier
                        .clickable { onSelectCrown(crown) }
                        .testTag("crown_${crown.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = crown.namePersian,
                            color = if (isSelected) Color.White else Color(0xFFF7E7CE),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = crown.categoryPersian,
                            color = Color(0xFFD4AF37),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Crown adjustment sliders
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A0E26)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "تنظیم دقیق سایز و ارتفاع تاج روی سر:",
                    color = Color(0xFFD1C4E9),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("اندازه (Scale):", color = Color.White, fontSize = 11.sp, modifier = Modifier.width(90.dp))
                    IconButton(onClick = { onScaleCrown(-0.05f) }) {
                        Icon(Icons.Default.Remove, contentDescription = null, tint = Color(0xFFD4AF37))
                    }
                    Text(
                        text = "%.2f x".format(crownScale),
                        color = Color(0xFFECC34C),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { onScaleCrown(0.05f) }) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFD4AF37))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ارتفاع سر (Y):", color = Color.White, fontSize = 11.sp, modifier = Modifier.width(90.dp))
                    IconButton(onClick = { onOffsetYCrown(-5f) }) {
                        Icon(Icons.Default.Remove, contentDescription = null, tint = Color(0xFFD4AF37))
                    }
                    Text(
                        text = "${crownOffsetY.toInt()} px",
                        color = Color(0xFFECC34C),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { onOffsetYCrown(5f) }) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFD4AF37))
                    }
                }
            }
        }
    }
}
