package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnalyticsTimeframe
import com.example.model.FinancialAnalyticsCatalog
import com.example.model.FinancialKpiSummary
import com.example.model.MonthlyFinancialPoint
import kotlin.math.roundToInt

/**
 * Recharts-inspired Interactive Comparative Financial Dashboard.
 * Displays high-precision comparative charts for:
 * 1. هزینه‌های سالن‌های زیبایی (Salon Operating Costs)
 * 2. پورسانت‌های ویزیتورها (Visitor Commissions)
 * 3. درآمدهای حاصل از تالارها و مزون‌ها (Ballroom & Maison Revenues)
 */
@Composable
fun FinancialAnalyticsDashboardSection(
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf(AnalyticsTimeframe.MONTHLY) }
    var selectedMonthIndex by remember { mutableIntStateOf(3) } // Default to 'Tir' (peak)

    val currentDataSet = remember(selectedTimeframe) {
        when (selectedTimeframe) {
            AnalyticsTimeframe.MONTHLY -> FinancialAnalyticsCatalog.MONTHLY_DATA
            AnalyticsTimeframe.QUARTERLY -> FinancialAnalyticsCatalog.QUARTERLY_DATA
            AnalyticsTimeframe.ANNUAL -> FinancialAnalyticsCatalog.ANNUAL_DATA
        }
    }

    val kpiSummary = remember(currentDataSet) {
        FinancialAnalyticsCatalog.calculateKpiSummary(currentDataSet)
    }

    // Interactive Profit Simulator States
    var simulatedBrideCount by remember { mutableFloatStateOf(16f) }
    var simulatedAverageContractMillion by remember { mutableFloatStateOf(95f) }
    var simulatedCommissionRate by remember { mutableFloatStateOf(14f) }

    val simulatedTotalRevenue by remember {
        derivedStateOf {
            (simulatedBrideCount * simulatedAverageContractMillion * (simulatedCommissionRate / 100f))
        }
    }
    val simulatedVisitorShare by remember {
        derivedStateOf { simulatedTotalRevenue * 0.40f }
    }
    val simulatedSalonNetShare by remember {
        derivedStateOf { simulatedTotalRevenue * 0.60f }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Header
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B0C2B)),
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.2.dp, Color(0xFFD4AF37)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFD4AF37), Color(0xFFECC34C))
                                    ),
                                    CircleShape
                                )
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                tint = Color(0xFF19062A),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "داشبورد تحلیلی و نمودارهای مقایسه‌ای مالی",
                                color = Color(0xFFF7E7CE),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "طراحی اختصاصی سبک Recharts • مثلث سالن، تالار و مزون",
                                color = Color(0xFFD1C4E9),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF381559), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "بازده لایسنس: +${kpiSummary.roiPercentage}٪",
                            color = Color(0xFF4ADE80),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Timeframe Selector Tabs (Recharts Tab Switcher)
        TabRow(
            selectedTabIndex = selectedTimeframe.ordinal,
            containerColor = Color(0xFF160A24),
            contentColor = Color(0xFFD4AF37),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTimeframe.ordinal]),
                    color = Color(0xFFECC34C),
                    height = 3.dp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF3B1E5C), RoundedCornerShape(14.dp))
        ) {
            AnalyticsTimeframe.values().forEach { timeframe ->
                Tab(
                    selected = selectedTimeframe == timeframe,
                    onClick = {
                        selectedTimeframe = timeframe
                        selectedMonthIndex = 0
                    },
                    text = {
                        Text(
                            text = timeframe.titlePersian,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTimeframe == timeframe) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTimeframe == timeframe) Color(0xFFECC34C) else Color(0xFFB4A3C8)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // KPI Metric Cards Grid (4 Key Metrics)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            KpiMetricMiniCard(
                title = "درآمد از تالارها",
                valueMillion = kpiSummary.totalBallroomRevenueMillion,
                icon = Icons.Default.Apartment,
                tint = Color(0xFF4ADE80),
                modifier = Modifier.weight(1f)
            )
            KpiMetricMiniCard(
                title = "درآمد از مزون‌ها",
                valueMillion = kpiSummary.totalMaisonRevenueMillion,
                icon = Icons.Default.Storefront,
                tint = Color(0xFFECC34C),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            KpiMetricMiniCard(
                title = "پورسانت ویزیتورها",
                valueMillion = kpiSummary.totalVisitorCommissionMillion,
                icon = Icons.Default.AttachMoney,
                tint = Color(0xFFFB923C),
                modifier = Modifier.weight(1f)
            )
            KpiMetricMiniCard(
                title = "هزینه‌های جاری سالن",
                valueMillion = kpiSummary.totalSalonCostsMillion,
                icon = Icons.Default.AccountBalanceWallet,
                tint = Color(0xFFF43F5E),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Net Profit Highlight Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D251A)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF10B981)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "تراز سود خالص پلتفرم و سالن",
                            color = Color(0xFFA7F3D0),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "پس از کسر کلیه هزینه‌ها و پورسانت‌های ویزیتور",
                            color = Color(0xFF6EE7B7),
                            fontSize = 11.sp
                        )
                    }
                }
                Text(
                    text = "${"%.1f".format(kpiSummary.totalNetProfitMillion)} م.ت",
                    color = Color(0xFF34D399),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Main Recharts Comparative Multi-Bar Chart
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF160A24)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.2.dp, Color(0xFF8B5CF6).copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نمودار میله‌ای مقایسه‌ای ۳ ضلعی (Recharts BarChart)",
                        color = Color(0xFFF7E7CE),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ارقام: میلیون تومان",
                        color = Color(0xFF9E8DB5),
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chart Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChartLegendItem(color = Color(0xFF4ADE80), label = "درآمد تالار/مزون")
                    ChartLegendItem(color = Color(0xFFFB923C), label = "پورسانت ویزیتور")
                    ChartLegendItem(color = Color(0xFFF43F5E), label = "هزینه سالن")
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Canvas Recharts Multi-Bar Rendering
                RechartsMultiBarCanvas(
                    data = currentDataSet,
                    selectedIndex = selectedMonthIndex,
                    onSelectIndex = { selectedMonthIndex = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Selected Point Deep-Dive Tooltip Card
                val activePoint = currentDataSet.getOrNull(selectedMonthIndex) ?: currentDataSet.first()
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF241238)),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "جزئیات دوره انتخابی: «${activePoint.monthName}»",
                                color = Color(0xFFECC34C),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "سود خالص: ${"%.1f".format(activePoint.netProfitMillionToman)} م.ت",
                                color = Color(0xFF4ADE80),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("درآمد تالارها: ${activePoint.ballroomRevenuesMillionToman} م.ت", color = Color(0xFF86EFAC), fontSize = 11.sp)
                            Text("درآمد مزون‌ها: ${activePoint.maisonRevenuesMillionToman} م.ت", color = Color(0xFFFDE047), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("پورسانت ویزیتور: ${activePoint.visitorCommissionsMillionToman} م.ت", color = Color(0xFFFDBA74), fontSize = 11.sp)
                            Text("هزینه‌های سالن: ${activePoint.salonCostsMillionToman} م.ت", color = Color(0xFFFDA4AF), fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Donut / Pie Chart: Revenue Distribution
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF160A24)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFF4ADE80).copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "سهم درآمدهای ورودی (Recharts Pie/Donut)",
                        color = Color(0xFFF7E7CE),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.PieChart, contentDescription = null, tint = Color(0xFF4ADE80))
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Donut Canvas
                    Box(
                        modifier = Modifier.size(130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(110.dp)) {
                            val slices = FinancialAnalyticsCatalog.REVENUE_SLICES
                            var startAngle = -90f
                            slices.forEach { slice ->
                                val sweepAngle = (slice.percentage / 100f) * 360f
                                drawArc(
                                    color = Color(slice.colorHex),
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round)
                                )
                                startAngle += sweepAngle
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("۱۰۰٪", color = Color(0xFFF7E7CE), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("اکوسیستم", color = Color(0xFF9E8DB5), fontSize = 9.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Legend list
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FinancialAnalyticsCatalog.REVENUE_SLICES.forEach { slice ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(Color(slice.colorHex), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = slice.title,
                                        color = Color(0xFFE9D5FF),
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = "${slice.percentage.toInt()}٪",
                                    color = Color(slice.colorHex),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Interactive ROI & Profit Simulator
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1032)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.2.dp, Color(0xFFECC34C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = Color(0xFFECC34C))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ماشین‌حساب هوشمند و شبیه‌ساز سود ماهانه",
                            color = Color(0xFFF7E7CE),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "پیش‌بینی زنده",
                        color = Color(0xFF34D399),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Slider 1: Bride count
                Text(
                    text = "تعداد عروس معرفی‌شده در ماه: ${simulatedBrideCount.toInt()} عروس",
                    color = Color(0xFFE9D5FF),
                    fontSize = 12.sp
                )
                Slider(
                    value = simulatedBrideCount,
                    onValueChange = { simulatedBrideCount = it },
                    valueRange = 5f..50f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFECC34C),
                        activeTrackColor = Color(0xFFECC34C)
                    )
                )

                // Slider 2: Average contract value
                Text(
                    text = "میانگین فاکتور تالار و مزون: ${simulatedAverageContractMillion.toInt()} میلیون تومان",
                    color = Color(0xFFE9D5FF),
                    fontSize = 12.sp
                )
                Slider(
                    value = simulatedAverageContractMillion,
                    onValueChange = { simulatedAverageContractMillion = it },
                    valueRange = 30f..250f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF4ADE80),
                        activeTrackColor = Color(0xFF4ADE80)
                    )
                )

                // Slider 3: Commission Rate
                Text(
                    text = "درصد پورسانت توافقی ارجاع: ${simulatedCommissionRate.toInt()}٪",
                    color = Color(0xFFE9D5FF),
                    fontSize = 12.sp
                )
                Slider(
                    value = simulatedCommissionRate,
                    onValueChange = { simulatedCommissionRate = it },
                    valueRange = 5f..25f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFB923C),
                        activeTrackColor = Color(0xFFFB923C)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Simulated Result Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2C1648), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("درآمد ناخالص شبکه از ارجاع:", color = Color(0xFFD1C4E9), fontSize = 12.sp)
                            Text("${"%.1f".format(simulatedTotalRevenue)} م.ت", color = Color(0xFFECC34C), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("سهم ویزیتور فروش (۴۰٪):", color = Color(0xFFFDBA74), fontSize = 12.sp)
                            Text("${"%.1f".format(simulatedVisitorShare)} م.ت", color = Color(0xFFFDBA74), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("سود خالص سالن زیبایی (۶۰٪):", color = Color(0xFF86EFAC), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${"%.1f".format(simulatedSalonNetShare)} م.ت", color = Color(0xFF4ADE80), fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun KpiMetricMiniCard(
    title: String,
    valueMillion: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF190C28)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.45f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color(0xFFBCA7D4),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${"%.1f".format(valueMillion)} م.ت",
                color = tint,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ChartLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = label, color = Color(0xFFDDD0EE), fontSize = 10.sp)
    }
}

/**
 * Native Canvas-based Multi-Bar Chart mimicking Recharts responsive aesthetic.
 */
@Composable
fun RechartsMultiBarCanvas(
    data: List<MonthlyFinancialPoint>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit
) {
    val maxVal = remember(data) {
        val highest = data.maxOfOrNull { it.totalRevenueMillionToman } ?: 100.0
        (highest * 1.25).coerceAtLeast(60.0)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .background(Color(0xFF0F0619), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    // Handled per column below
                }
        ) {
            val width = size.width
            val height = size.height
            val chartBottom = height - 26.dp.toPx()
            val chartHeight = chartBottom - 10.dp.toPx()

            // Draw Y-axis reference grid lines
            val gridSteps = 4
            for (g in 0..gridSteps) {
                val y = chartBottom - (chartHeight * (g.toFloat() / gridSteps))
                drawLine(
                    color = Color(0xFF2D1845),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw each month's 3 grouped bars
            val count = data.size
            val groupWidth = width / count

            for (i in 0 until count) {
                val point = data[i]
                val groupCenterX = (i * groupWidth) + (groupWidth / 2f)
                val barWidth = (groupWidth * 0.22f).coerceAtMost(16.dp.toPx())
                val spacing = 3.dp.toPx()

                val isSelected = i == selectedIndex

                // Bar heights (clamped)
                val revHeight = ((point.totalRevenueMillionToman / maxVal) * chartHeight).toFloat()
                val comHeight = ((point.visitorCommissionsMillionToman / maxVal) * chartHeight).toFloat()
                val costHeight = ((point.salonCostsMillionToman / maxVal) * chartHeight).toFloat()

                // Selected background column highlight
                if (isSelected) {
                    drawRoundRect(
                        color = Color(0xFF3F1F63).copy(alpha = 0.45f),
                        topLeft = Offset(i * groupWidth, 4.dp.toPx()),
                        size = Size(groupWidth, chartBottom),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )
                }

                // Bar 1: Costs (Pink / Rose)
                val x1 = groupCenterX - barWidth - spacing
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFFB7185), Color(0xFFE11D48))
                    ),
                    topLeft = Offset(x1, chartBottom - costHeight),
                    size = Size(barWidth, costHeight),
                    cornerRadius = CornerRadius(3.dp.toPx())
                )

                // Bar 2: Visitor Commissions (Orange / Amber)
                val x2 = groupCenterX
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFFDBA74), Color(0xFFEA580C))
                    ),
                    topLeft = Offset(x2, chartBottom - comHeight),
                    size = Size(barWidth, comHeight),
                    cornerRadius = CornerRadius(3.dp.toPx())
                )

                // Bar 3: Revenues (Green / Emerald)
                val x3 = groupCenterX + barWidth + spacing
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF86EFAC), Color(0xFF16A34A))
                    ),
                    topLeft = Offset(x3, chartBottom - revHeight),
                    size = Size(barWidth, revHeight),
                    cornerRadius = CornerRadius(3.dp.toPx())
                )
            }
        }

        // Clickable touch targets overlay and labels
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { idx, point ->
                val isSelected = idx == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { onSelectIndex(idx) },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = point.shortName,
                        fontSize = 10.sp,
                        color = if (isSelected) Color(0xFFECC34C) else Color(0xFF9E8DB5),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
