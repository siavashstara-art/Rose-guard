package com.example.ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.ViewCarousel
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.BusinessOffer
import com.example.model.SalonTourCatalog
import com.example.model.SalonTourStation
import com.example.model.VideoScene
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
import java.text.NumberFormat
import java.util.Locale

/**
 * Creative hub for Client Attraction Tour, Commercial Proposals, and Growth Metrics.
 * The core strategic showcase requested by salon managers.
 */
@Composable
fun SalonClientAttractionHub(
    tourStations: List<SalonTourStation>,
    selectedStationId: String,
    activeScene: VideoScene?,
    businessOffers: List<BusinessOffer>,
    activeTab: Int,
    onTabSelected: (Int) -> Unit,
    onSelectStation: (String) -> Unit,
    onApplyStationToActiveScene: (String) -> Unit,
    onApplyFullTourToAllScenes: () -> Unit,
    onLoadSpeech36TourPreset: () -> Unit,
    onPlayScene: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentStation = tourStations.firstOrNull { it.id == selectedStationId } ?: tourStations.first()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("client_attraction_hub"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        border = BorderStroke(1.2.dp, IvoryCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with luxury accent badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = PeachContainer,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "مرکز جذب مشتری و پیشنهادات تجاری",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "تور بصری اختصاصی سالنیار و موتور رشد درآمد کسب‌وکار",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = StateSuccessContainer,
                    border = BorderStroke(1.dp, StateSuccess.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StateSuccess, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ویژه مدیران ۳۶",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = StateSuccess
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Speech 36 Preset Activation Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = IvorySurfaceVariant,
                border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.25f))
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
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = PeachPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "پرزنتیشن ۵ فاز استراتژیک مهندس علیمیری (فروش سالن‌یار)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "شهر متاورسی توانا، حل چالش استعلام قیمت، کاتالوگ لباس عروس/داماد، پرو آفلاین و مدل Win-Win",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Button(
                        onClick = onLoadSpeech36TourPreset,
                        colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("load_speech_36_preset_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("اجرای سناریو فروش", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Tabs
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = Color.Transparent,
                contentColor = PeachPrimary,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = PeachPrimary,
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { onTabSelected(0) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ViewCarousel, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تور ۶ ایستگاه", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { onTabSelected(1) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.People, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("برگردان مشتری و حریم خصوصی", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Tab(
                    selected = activeTab == 2,
                    onClick = { onTabSelected(2) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("پیشنهادات تجاری", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Tab(
                    selected = activeTab == 3,
                    onClick = { onTabSelected(3) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Insights, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("طرح ۹ میلیون و ROI", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (activeTab) {
                0 -> {
                    // TAB 0: LUXURY VISUAL TOUR
                    VisualTourTabContent(
                        stations = tourStations,
                        currentStation = currentStation,
                        context = context,
                        onSelectStation = onSelectStation,
                        onApplyStationToActiveScene = { onApplyStationToActiveScene(currentStation.id) },
                        onApplyFullTourToAllScenes = onApplyFullTourToAllScenes,
                        onPlayScene = onPlayScene
                    )
                }
                1 -> {
                    // TAB 1: CUSTOMER RECOVERY ENGINE & FACEGUARD PRIVACY
                    CustomerRecoveryAndPrivacyTabContent()
                }
                2 -> {
                    // TAB 2: COMMERCIAL OFFERS & PARTNERSHIP NETWORK
                    CommercialOffersTabContent(businessOffers = businessOffers)
                }
                3 -> {
                    // TAB 3: CLIENT ATTRACTION METRICS & 9M TOMAN ROI CALCULATOR
                    AttractionRoiTabContent()
                }
            }
        }
    }
}

@Composable
private fun VisualTourTabContent(
    stations: List<SalonTourStation>,
    currentStation: SalonTourStation,
    context: Context,
    onSelectStation: (String) -> Unit,
    onApplyStationToActiveScene: () -> Unit,
    onApplyFullTourToAllScenes: () -> Unit,
    onPlayScene: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Horizontal station selector pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stations.forEach { station ->
                val isSelected = station.id == currentStation.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectStation(station.id) },
                    label = {
                        Text(
                            text = station.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PeachContainer,
                        selectedLabelColor = PeachPrimary
                    ),
                    modifier = Modifier.testTag("station_chip_${station.id}")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Large Showcase Station Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("showcase_station_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = IvorySurfaceVariant),
            border = BorderStroke(1.dp, IvoryCardBorder)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Image Box with Badge Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(Color(0xFF231C1A))
                ) {
                    val imageModel = remember(currentStation.drawableResName) {
                        com.example.model.SalonTourImageResolver.resolveImageModel(
                            context,
                            currentStation.drawableResName
                        )
                    }

                    if (imageModel != null) {
                        AsyncImage(
                            model = imageModel,
                            contentDescription = currentStation.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Top Badges Overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(currentStation.badgeColorHex)
                        ) {
                            Text(
                                text = currentStation.attractionBadge,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0x99000000)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = currentStation.clientAttractionStat,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                // Station Description & Details
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentStation.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = currentStation.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = PeachPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentStation.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Feature highlights
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(IvorySurface, RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        currentStation.keyHighlights.forEach { highlight ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StateSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = highlight,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onApplyStationToActiveScene,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("apply_station_active_btn"),
                            border = BorderStroke(1.dp, PeachPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("اعمال بر صحنه جاری", style = MaterialTheme.typography.labelSmall, color = PeachPrimary)
                        }

                        Button(
                            onClick = onApplyFullTourToAllScenes,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                            modifier = Modifier.weight(1.3f).testTag("apply_all_tour_btn")
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("اعمال سراسری بر تمام صحنه‌ها", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommercialOffersTabContent(businessOffers: List<BusinessOffer>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "پیشنهادات تجاری استخراج‌شده از سخنان مدیران و پلن‌های هم‌افزایی:",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        val sampleOffers = if (businessOffers.isNotEmpty()) businessOffers else listOf(
            BusinessOffer(
                id = "off_1",
                title = "شبکه هم‌افزایی سالن زیبایی، مزون عروس و تالار",
                description = "معرفی متقابل مراجعین با توافق کمیسیون ۱۵٪ به ازای هر رزرو قطعی تشریفات",
                sourceQuote = "پیشنهاد ویژه ما تشکیل شبکه همکاری بین سالن‌های زیبایی، مزون‌ها و تالارها است.",
                category = com.example.model.BusinessOfferCategory.SPEECH_OFFER
            ),
            BusinessOffer(
                id = "off_2",
                title = "ویترین اختصاصی آنلاین سالنیار",
                description = "صفحه شیک اختصاصی با نام برند شما برای معرفی پکیج‌ها و نمونه‌کارها در شبکه‌های اجتماعی",
                sourceQuote = "سالنیار برای هر سالن زیبایی یک ویترین اختصاصی و شیک ایجاد می‌کند.",
                category = com.example.model.BusinessOfferCategory.CONFIRMED_FEATURE
            ),
            BusinessOffer(
                id = "off_3",
                title = "باشگاه مشتریان و بازگرداندن هوشمند مراجعین",
                description = "سیستم خودکار ارسال آفر و تخفیف ویژه به مشتریانی که بیش از ۳ ماه مراجعه نداشته‌اند",
                sourceQuote = "قابلیت کلیدی بازگرداندن هوشمند مشتریان با پیشنهادهای مناسبتی است.",
                category = com.example.model.BusinessOfferCategory.CONFIRMED_FEATURE
            ),
            BusinessOffer(
                id = "off_4",
                title = "تولید تیزر اختصاصی و تیزرسازی استودیو توانا",
                description = "حمایت رسانه‌ای در تولید ویدیوهای تبلیغاتی و استوری‌های جذب مشتری اینستاگرام",
                sourceQuote = "استودیوی توانا در ساخت محتوای ویدیویی و تیزر تبلیغاتی شما را پشتیبانی می‌کند.",
                category = com.example.model.BusinessOfferCategory.SPEECH_OFFER
            )
        )

        sampleOffers.forEach { offer ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = IvorySurfaceVariant,
                border = BorderStroke(1.dp, IvoryCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = offer.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(offer.category.badgeColorHex).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = offer.category.titlePersian,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(offer.category.badgeColorHex),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = offer.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )

                    if (offer.sourceQuote.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IvorySurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "«${offer.sourceQuote}»",
                                style = MaterialTheme.typography.labelSmall,
                                color = PeachPrimary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AttractionRoiTabContent() {
    var activeStationsCount by remember { mutableFloatStateOf(6f) }
    val formatter = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("fa-IR")) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // High-level Metrics Cards Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = "جذب مشتری جدید",
                value = "+۴۲٪",
                subtitle = "با تور تصویری ویدیویی",
                color = PeachPrimary,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "نرخ تکمیل نوبت",
                value = "۸۸٪",
                subtitle = "کاهش کنسلی با پیامک",
                color = StateSuccess,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "سبد خرید VIP",
                value = "۳.۸ برابر",
                subtitle = "لاین عروس و تشریفات",
                color = Color(0xFFD4AF37),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Interactive ROI Growth Simulator
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = IvorySurfaceVariant,
            border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ماشین‌حساب رشد درآمد و سودآوری سالن شما:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "تعداد صندلی‌ها / پرسنل فعال سالن: ${activeStationsCount.toInt()} ایستگاه",
                    style = MaterialTheme.typography.bodySmall,
                    color = PeachPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Slider(
                    value = activeStationsCount,
                    onValueChange = { activeStationsCount = it },
                    valueRange = 2f..20f,
                    steps = 17,
                    colors = SliderDefaults.colors(
                        thumbColor = PeachPrimary,
                        activeTrackColor = PeachPrimary,
                        inactiveTrackColor = PeachContainer
                    ),
                    modifier = Modifier.testTag("roi_slider")
                )

                val estimatedExtraAppointments = (activeStationsCount * 14).toInt()
                val estimatedMonthlyRevenueToman = (activeStationsCount * 18_500_000).toLong()

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "نوبت‌های افزوده ماهانه:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(
                            text = "+$estimatedExtraAppointments نوبت قطعی",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = StateSuccess
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "پیش‌بینی رشد درآمد سالنیار:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(
                            text = "${formatter.format(estimatedMonthlyRevenueToman)} تومان / ماه",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = PeachPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = IvorySurfaceVariant,
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun CustomerRecoveryAndPrivacyTabContent() {
    var isBlurActive by remember { mutableStateOf(true) }
    var isPixelateActive by remember { mutableStateOf(false) }
    var isInstagramStoryFormat by remember { mutableStateOf(true) }
    var sentSmsIndex by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // 1. Customer Recovery Header & Stats
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF3949AB).copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Color(0xFF3949AB).copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = null, tint = Color(0xFF3949AB), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "سیستم برگردان مشتری (Customer Recovery)", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "شناسایی هوشمند مراجعین غیرفعال و بازگرداندن با آفر شخصی", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StateSuccessContainer
                ) {
                    Text(text = "نرخ موفقیت: ۷۸٪", style = MaterialTheme.typography.labelSmall, color = StateSuccess, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
        }

        // Inactive Clients List
        val inactiveClients = listOf(
            Triple("سرکار خانم مریم رضایی", "لاین تخصصی رنگ و لایت • ۴ ماه عدم مراجعه", "هدیه بازگشت: ۲۰٪ تخفیف رنگ مو"),
            Triple("سرکار خانم سارا شمس", "لاین مراقبت پوست و فیشیال • ۵ ماه عدم مراجعه", "هدیه بازگشت: یک جلسه آبرسانی رایگان"),
            Triple("سرکار خانم نیلوفر بهرامی", "لاین احیا و کراتین مو • ۶ ماه عدم مراجعه", "هدیه بازگشت: هیدروژن تراپی رایگان")
        )

        inactiveClients.forEachIndexed { idx, client ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = IvorySurfaceVariant,
                border = BorderStroke(1.dp, IvoryCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = client.first, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = client.second, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                        Text(text = client.third, style = MaterialTheme.typography.labelSmall, color = PeachPrimary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
                    }
                    Button(
                        onClick = { sentSmsIndex = idx },
                        colors = ButtonDefaults.buttonColors(containerColor = if (sentSmsIndex == idx) StateSuccess else PeachPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(text = if (sentSmsIndex == idx) "پیامک ارسال شد ✓" else "پیامک برگردان", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                    }
                }
            }
        }

        // 2. FaceGuard Privacy & Instagram Story Controls
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = IvorySurfaceVariant,
            border = BorderStroke(1.dp, IvoryCardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "حفظ حریم خصوصی (FaceGuard) و استوری اینستاگرام", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "تار کردن چهره (Blur) برای امنیت تصاویر", style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                        Text(text = "مات کردن صورت مراجعین در عکس‌های نمونه‌کار بدون اجازه", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                    }
                    Switch(checked = isBlurActive, onCheckedChange = { isBlurActive = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "شطرنجی کردن چهره (Pixelate)", style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                        Text(text = "حفاظت با جلوه پیکسلی مدرن ویژه شبکه‌های اجتماعی", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                    }
                    Switch(checked = isPixelateActive, onCheckedChange = { isPixelateActive = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "قالب استوری ۹:۱۶ اینستاگرام", style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                        Text(text = "تولید تیزر عمودی با واترمارک، لوگو و تلفن سالن شما", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
                    }
                    Switch(checked = isInstagramStoryFormat, onCheckedChange = { isInstagramStoryFormat = it })
                }
            }
        }

        // 3. Pricing Plan & Affiliate Revenue Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SoftPinkContainer.copy(alpha = 0.5f),
            border = BorderStroke(1.2.dp, SoftPink)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "قیمت اشتراک سالانه سالن‌یار:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "۹,۰۰۰,۰۰۰ تومان / سال", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = PeachPrimary)
                }

                Text(
                    text = "• بازگشت آنی سرمایه: هزینه نرم‌افزار تنها با جذب اولین عروس یا بازگشت مراجعین خاموش جبران می‌شود.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary
                )
                Text(
                    text = "• درآمد بازاریابی: با معرفی سالن‌یار به سایر همکاران و آرایشگاه‌ها، پورسانت نقدی دریافت کنید.",
                    style = MaterialTheme.typography.labelSmall,
                    color = StateSuccess,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
