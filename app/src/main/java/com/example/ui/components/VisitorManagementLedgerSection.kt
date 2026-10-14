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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReferralDirection
import com.example.model.ReferralPartnerType
import com.example.model.ReferralStage
import com.example.model.SupportTicket
import com.example.model.TwoWayBridalReferral
import com.example.model.TwoWayReferralCatalog
import com.example.model.VisitorManagementCatalog
import com.example.model.VisitorSaleRecord

@Composable
fun VisitorManagementLedgerSection(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }

    val salesRecords = remember { mutableStateListOf<VisitorSaleRecord>().apply { addAll(VisitorManagementCatalog.INITIAL_SALES_RECORDS) } }
    val supportTickets = remember { mutableStateListOf<SupportTicket>().apply { addAll(VisitorManagementCatalog.INITIAL_TICKETS) } }
    val twoWayReferrals = remember { mutableStateListOf<TwoWayBridalReferral>().apply { addAll(TwoWayReferralCatalog.INITIAL_TWO_WAY_REFERRALS) } }

    // Ticket form states
    var ticketSubject by remember { mutableStateOf("") }
    var ticketMessage by remember { mutableStateOf("") }

    // New 2-way Referral form states
    var newBrideName by remember { mutableStateOf("") }
    var newBridePhone by remember { mutableStateOf("") }
    var newPartnerName by remember { mutableStateOf("") }
    var newWeddingDate by remember { mutableStateOf("۱۴۰۵/۱۰/۱۵") }
    var newDirection by remember { mutableStateOf(ReferralDirection.SALON_TO_PARTNER) }
    var newBudgetTomanStr by remember { mutableStateOf("30000000") }

    val totalSalonPayments = salesRecords.sumOf { it.salonPaymentToman }
    val totalVisitorCommissions = salesRecords.sumOf { it.visitorCommissionToman }
    val totalReferralCommissionsPaid = twoWayReferrals.sumOf { it.partnerOrSalonShareToman }

    Column(modifier = modifier.fillMaxWidth()) {
        // Hero Management Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF221133)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFF9333EA))),
                    RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD4AF37), CircleShape)
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = Color(0xFF1B0B26)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "دفتر مدیریت ویزیتورها و شبکه ارجاع دوطرفه عروس",
                        color = Color(0xFFF7E7CE),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "مدیریت فروش لایسنس‌ها، پرداختی آرایشگاه، پورسانت ویزیتور و تسهیم ۵۰/۵۰ سود با تالارها و مزون‌ها",
                        color = Color(0xFFD1C4E9),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Financial 3-Pillar Summary Metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16271D)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF4ADE80)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("پرداختی سالن‌ها", color = Color(0xFF86EFAC), fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${totalSalonPayments / 1_000_000} م.ت",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B1D10)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFFBBF24)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("پورسانت ویزیتورها", color = Color(0xFFFDE68A), fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${totalVisitorCommissions / 1_000_000} م.ت",
                        color = Color(0xFFECC34C),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF132233)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF38BDF8)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("پاداش ارجاع عروس", color = Color(0xFFBAE6FD), fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${totalReferralCommissionsPaid / 1_000_000} م.ت",
                        color = Color(0xFF38BDF8),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF160B24),
            contentColor = Color(0xFFECC34C),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFFD4AF37)
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("فروش ویزیتورها", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("ارجاع دوطرفه عروس", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("تیکت و فاکتور", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> {
                // Tab 0: Detailed Visitor Sales Ledger
                Text(
                    text = "فهرست فروش و مشخصات لایسنس‌های هر سالن:",
                    color = Color(0xFFECC34C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    salesRecords.forEach { record ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF190F26)),
                            border = BorderStroke(1.dp, Color(0xFF4C2A75)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.width(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "ویزیتور: ${record.visitorName}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF381A54), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = record.licenseCode, color = Color(0xFFD4AF37), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "آرایشگاه: ${record.salonName} | مدیریت: ${record.salonManager}",
                                    color = Color(0xFFF7E7CE),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.width(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = record.salonAddress, color = Color(0xFFC4B5FD), fontSize = 11.sp)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray, modifier = Modifier.width(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "تلفن: ${record.salonPhone}", color = Color(0xFFC4B5FD), fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "دوره: ${record.startDateShamsi} تا ${record.endDateShamsi}", color = Color(0xFFDDD6FE), fontSize = 11.sp)
                                    Text(text = "باقیمانده: ${record.daysRemaining} روز", color = Color(0xFF4ADE80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF24153B))
                                        .padding(8.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Column {
                                            Text(text = "پرداختی سالن:", color = Color.Gray, fontSize = 10.sp)
                                            Text(text = "${record.salonPaymentToman / 1_000_000} م.ت", color = Color(0xFF4ADE80), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Column {
                                            Text(text = "پورسانت ویزیتور:", color = Color.Gray, fontSize = 10.sp)
                                            Text(text = "${record.visitorCommissionToman / 1_000_000} م.ت", color = Color(0xFFECC34C), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Column {
                                            Text(text = "وضعیت:", color = Color.Gray, fontSize = 10.sp)
                                            Text(text = record.paymentStatus, color = Color.White, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Tab 1: 360-degree Two-Way Bridal Referral Ecosystem
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1229)),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFFD4AF37))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "سازوکار سود متقابل و تسهیم ۵۰٪ پورسانت‌ها:",
                                color = Color(0xFFECC34C),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "۱. تالار یا مزون عروس را به آرایشگاه معرفی می‌کند -> پلتفرم پورسانت گرفته و ۵۰٪ را نقداً به تالاردار/مزون‌دار می‌دهد.\n" +
                                   "۲. آرایشگاه عروس را به تالار یا مزون معرفی می‌کند -> پلتفرم پورسانت گرفته و ۵۰٪ را نقداً به آرایشگاه پرداخت می‌کند.\n" +
                                   "در این مدل، تمام طرف‌ها بدون دغدغه و با رضایت کامل از شبکه سود می‌برند.",
                            color = Color(0xFFE9D5FF),
                            fontSize = 11.sp,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // List of 2-way referrals
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    twoWayReferrals.forEach { item ->
                        val isFromPartner = item.direction == ReferralDirection.PARTNER_TO_SALON
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF180E24)),
                            border = BorderStroke(1.2.dp, Color(item.direction.badgeColorHex)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.brideGroomName,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Box(
                                        modifier = Modifier
                                            .background(Color(item.stage.colorHex).copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                            .border(1.dp, Color(item.stage.colorHex), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = item.stage.titlePersian, color = Color(item.stage.colorHex), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Direction Badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(item.direction.badgeColorHex).copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = item.direction.titlePersian,
                                        color = Color(item.direction.badgeColorHex),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "طرف همکار: ${item.partnerName} (${item.partnerType.titlePersian})",
                                    color = Color(0xFFDDD6FE),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "سالن زیبایی مقصد/مبدا: ${item.salonName}",
                                    color = Color(0xFFF7E7CE),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "تاریخ مراسم: ${item.weddingDateShamsi} | تلفن تماس: ${item.contactPhone}",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Profit Split Box (50% / 50%)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF221336))
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = "کل قرارداد:", color = Color.Gray, fontSize = 9.sp)
                                            Text(text = "${item.totalContractBudgetToman / 1_000_000} م.ت", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Column {
                                            Text(text = "سهم ۵۰٪ معرف:", color = Color(0xFF86EFAC), fontSize = 9.sp)
                                            Text(text = "${item.partnerOrSalonShareToman / 1_000_000} م.ت نقدی", color = Color(0xFF4ADE80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Column {
                                            Text(text = "سهم ۵۰٪ پلتفرم:", color = Color(0xFFFDE68A), fontSize = 9.sp)
                                            Text(text = "${item.platformShareToman / 1_000_000} م.ت", color = Color(0xFFECC34C), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Golden Triangle Exclusive Bride & Groom Discount Voucher
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF2A1542))
                                        .border(1.dp, Color(0xFFECC34C), RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "🎁 بن تخفیف طلایی هدیه به عروس و داماد:",
                                                color = Color(0xFFFDE68A),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${item.exclusiveBrideDiscountPercent}٪ تخفیف نقدی (${item.brideDiscountAmountToman / 1_000_000} میلیون تومان هدیه باشگاه)",
                                                color = Color(0xFF4ADE80),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFD4AF37), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = item.voucherCode,
                                                color = Color(0xFF1B0B26),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                }

                                if (item.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = item.description, color = Color(0xFFD1C4E9), fontSize = 10.sp, lineHeight = 15.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Registration Form for 2-Way Referral
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF241438)),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ثبت ارجاع جدید و محاسبه ۵۰٪ سهم نقدی:",
                            color = Color(0xFFECC34C),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Direction Selector
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (newDirection == ReferralDirection.SALON_TO_PARTNER) Color(0xFF38BDF8) else Color(0xFF1E0E2C))
                                    .clickable { newDirection = ReferralDirection.SALON_TO_PARTNER }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "از سالن به تالار/مزون",
                                    color = if (newDirection == ReferralDirection.SALON_TO_PARTNER) Color(0xFF0F172A) else Color(0xFFE2E8F0),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (newDirection == ReferralDirection.PARTNER_TO_SALON) Color(0xFF4ADE80) else Color(0xFF1E0E2C))
                                    .clickable { newDirection = ReferralDirection.PARTNER_TO_SALON }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "از تالار/مزون به سالن",
                                    color = if (newDirection == ReferralDirection.PARTNER_TO_SALON) Color(0xFF064E3B) else Color(0xFFE2E8F0),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = newBrideName,
                            onValueChange = { newBrideName = it },
                            label = { Text("نام و نام خانوادگی عروس و داماد", color = Color(0xFFC4B5FD)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFFD4AF37),
                                unfocusedBorderColor = Color(0xFF4A2F6E)
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newBridePhone,
                                onValueChange = { newBridePhone = it },
                                label = { Text("شماره همراه", color = Color(0xFFC4B5FD)) },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFFD4AF37),
                                    unfocusedBorderColor = Color(0xFF4A2F6E)
                                )
                            )
                            OutlinedTextField(
                                value = newPartnerName,
                                onValueChange = { newPartnerName = it },
                                label = { Text("نام تالار یا مزون همکار", color = Color(0xFFC4B5FD)) },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFFD4AF37),
                                    unfocusedBorderColor = Color(0xFF4A2F6E)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (newBrideName.isNotBlank() && newPartnerName.isNotBlank()) {
                                    val budget = newBudgetTomanStr.toLongOrNull() ?: 20_000_000L
                                    val commission = (budget * 0.10).toLong()
                                    val halfShare = commission / 2
                                    val discountPercent = 15
                                    val discountAmount = (budget * discountPercent / 100)
                                    val voucher = "VIP-GLD-${(twoWayReferrals.size + 1) * 1111}"
                                    twoWayReferrals.add(
                                        TwoWayBridalReferral(
                                            id = "REF-2WAY-${twoWayReferrals.size + 1}",
                                            direction = newDirection,
                                            brideGroomName = newBrideName,
                                            contactPhone = newBridePhone.ifBlank { "09120000000" },
                                            weddingDateShamsi = newWeddingDate,
                                            partnerType = if (newDirection == ReferralDirection.SALON_TO_PARTNER) ReferralPartnerType.WEDDING_HALL else ReferralPartnerType.BRIDAL_MAISON,
                                            partnerName = newPartnerName,
                                            salonName = "سالن زیبایی و عروس گل‌آرا",
                                            totalContractBudgetToman = budget,
                                            exclusiveBrideDiscountPercent = discountPercent,
                                            brideDiscountAmountToman = discountAmount,
                                            totalCommissionToman = commission,
                                            platformShareToman = halfShare,
                                            partnerOrSalonShareToman = halfShare,
                                            voucherCode = voucher,
                                            stage = ReferralStage.SUBMITTED,
                                            description = "ارجاع جدید ثبت گردید. بن تخفیف طلایی $discountPercent٪ به عروس تعلق گرفت و به محض قطعی شدن، مبلغ ${halfShare / 1_000_000} میلیون تومان سهم نقدی معرف تسویه می‌شود."
                                        )
                                    )
                                    newBrideName = ""
                                    newBridePhone = ""
                                    newPartnerName = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1B0B26))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ثبت ارجاع و محاسبه سهم ۵۰٪ نقدی", color = Color(0xFF1B0B26), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            2 -> {
                // Tab 2: Tickets & Invoicing
                Text(
                    text = "سیستم ارسال تیکت، فاکتور و پشتیبانی سالن:",
                    color = Color(0xFFECC34C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    supportTickets.forEach { ticket ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1030)),
                            border = BorderStroke(1.dp, Color(0xFF45246B)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = ticket.subject, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF381B52), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = ticket.status, color = Color(0xFF4ADE80), fontSize = 10.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = ticket.message, color = Color(0xFFD1C4E9), fontSize = 11.sp, lineHeight = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ارسال‌کننده: ${ticket.visitorName} | تاریخ: ${ticket.createdDateShamsi} | اولویت: ${ticket.priority}",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF241438)),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "ارسال تیکت / فاکتور جدید:", color = Color(0xFFECC34C), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = ticketSubject,
                            onValueChange = { ticketSubject = it },
                            label = { Text("موضوع تیکت (مثال: درخواست صدور فاکتور رسمی ۹ میلیون)", color = Color(0xFFC4B5FD)) },
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
                            value = ticketMessage,
                            onValueChange = { ticketMessage = it },
                            label = { Text("متن تیکت و توضیحات مالی / ویزیتور", color = Color(0xFFC4B5FD)) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFFD4AF37),
                                unfocusedBorderColor = Color(0xFF4A2F6E)
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (ticketSubject.isNotBlank() && ticketMessage.isNotBlank()) {
                                    supportTickets.add(
                                        SupportTicket(
                                            ticketId = "TCK-${supportTickets.size + 401}",
                                            salonName = "سالن زیبایی و عروس گل‌آرا",
                                            visitorName = "مهندس سیاوش علی‌میری",
                                            subject = ticketSubject,
                                            message = ticketMessage,
                                            priority = "فوری",
                                            createdDateShamsi = "۱۴۰۵/۰۷/۱۴",
                                            status = "ثبت شد - در صف پاسخ"
                                        )
                                    )
                                    ticketSubject = ""
                                    ticketMessage = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ارسال رسمی تیکت به مرکز مدیریت توانا", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
