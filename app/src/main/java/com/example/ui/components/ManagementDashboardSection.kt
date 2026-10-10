package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CommercialPartnershipRecord
import com.example.model.FinancialTransaction
import com.example.model.LicenseStatus
import com.example.model.SalonLicense
import com.example.model.VisitorAccount
import com.example.service.SalonDossier
import com.example.service.TavanaManagementRepository
import com.example.ui.theme.IvoryCardBorder
import com.example.ui.theme.IvorySurface
import com.example.ui.theme.IvorySurfaceVariant
import com.example.ui.theme.PeachContainer
import com.example.ui.theme.PeachPrimary
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.StateError
import com.example.ui.theme.StateErrorContainer
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ManagementDashboardSection(
    isUnlocked: Boolean,
    allLicenses: List<SalonLicense>,
    visitorAccounts: List<VisitorAccount>,
    partnerships: List<CommercialPartnershipRecord>,
    transactions: List<FinancialTransaction>,
    onUnlock: (String) -> Boolean,
    onLock: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pinDraft by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("management_dashboard_section"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        border = BorderStroke(1.dp, IvoryCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = SoftPinkContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = SoftPink,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "TAVANA Management Dashboard",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "داشبورد مدیریت مرکزی توانا (مستقل از محیط مشتری)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                if (isUnlocked) {
                    OutlinedButton(
                        onClick = onLock,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp).testTag("lock_management_btn")
                    ) {
                        Text(text = "خروج امن", style = MaterialTheme.typography.labelSmall, color = StateError)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isUnlocked) {
                // PIN Access Gate
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = IvorySurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "احراز هویت مدیر ارشد توانا",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "جهت حفظ محرمانگی مالی و قراردادها، مشتریان و سالن‌ها به این بخش دسترسی ندارند.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(0.85f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = pinDraft,
                                onValueChange = {
                                    pinDraft = it
                                    pinError = false
                                },
                                label = { Text("رمز عبور مدیریت (پیش‌فرض: 2026)") },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("management_pin_field")
                            )

                            Button(
                                onClick = {
                                    val ok = onUnlock(pinDraft)
                                    if (!ok) {
                                        pinError = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(52.dp).testTag("submit_management_pin_btn")
                            ) {
                                Text(text = "ورود", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (pinError) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "رمز عبور اشتباه است. (رمز تستی: 2026)", style = MaterialTheme.typography.bodySmall, color = StateError)
                        }
                    }
                }
            } else {
                // Unlocked Dashboard Content with Tabs
                val tabTitles = listOf("۲۰۰ مجوز مشتری", "پرونده سالن‌ها", "عملکرد ویزیتورها", "حسابداری و تراکنش‌ها")

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = IvorySurfaceVariant,
                    contentColor = PeachPrimary,
                    edgePadding = 8.dp,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                ) {
                    tabTitles.forEachIndexed { idx, title ->
                        Tab(
                            selected = selectedTab == idx,
                            onClick = { selectedTab = idx },
                            text = { Text(text = title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    0 -> LicensesRegistryTab(licenses = allLicenses)
                    1 -> SalonsDossierTab()
                    2 -> VisitorsPerformanceTab(visitors = visitorAccounts)
                    3 -> AccountingWalletTab(partnerships = partnerships, transactions = transactions)
                }
            }
        }
    }
}

@Composable
private fun LicensesRegistryTab(licenses: List<SalonLicense>) {
    var statusFilter by remember { mutableStateOf<LicenseStatus?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filtered = licenses.filter {
        (statusFilter == null || it.status == statusFilter) &&
                (searchQuery.isBlank() || it.formattedIndex.contains(searchQuery) || it.clientSecretKey.contains(searchQuery) || (it.allocatedSalonName?.contains(searchQuery) == true))
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "سامانه مدیریت ۲۰۰ لایسنس (۰۰۱ الی ۲۰۰):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Text(text = "نمایش ${filtered.size} از ۲۰۰", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = statusFilter == null,
                onClick = { statusFilter = null },
                label = { Text("همه", style = MaterialTheme.typography.labelSmall) }
            )
            FilterChip(
                selected = statusFilter == LicenseStatus.ACTIVE,
                onClick = { statusFilter = if (statusFilter == LicenseStatus.ACTIVE) null else LicenseStatus.ACTIVE },
                label = { Text("فعال", style = MaterialTheme.typography.labelSmall) }
            )
            FilterChip(
                selected = statusFilter == LicenseStatus.EXPIRED,
                onClick = { statusFilter = if (statusFilter == LicenseStatus.EXPIRED) null else LicenseStatus.EXPIRED },
                label = { Text("منقضی", style = MaterialTheme.typography.labelSmall) }
            )
            FilterChip(
                selected = statusFilter == LicenseStatus.REVOKED,
                onClick = { statusFilter = if (statusFilter == LicenseStatus.REVOKED) null else LicenseStatus.REVOKED },
                label = { Text("لغوشده", style = MaterialTheme.typography.labelSmall) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // First 10 items preview with clean scrolling
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filtered.take(8).forEach { lic ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = IvorySurfaceVariant,
                    border = BorderStroke(1.dp, IvoryCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(shape = RoundedCornerShape(6.dp), color = PeachContainer) {
                                    Text(text = "کد ${lic.formattedIndex}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PeachPrimary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = lic.allocatedSalonName ?: "بدون تخصیص (آزاد)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "کلید: ${lic.clientSecretKey}", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 10.sp)
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(lic.status.colorHex).copy(alpha = 0.15f)
                        ) {
                            Text(text = lic.status.titlePersian, style = MaterialTheme.typography.labelSmall, color = Color(lic.status.colorHex), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SalonsDossierTab() {
    val dossiers = TavanaManagementRepository().getSalonDossiers()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        dossiers.forEach { d ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = IvorySurfaceVariant,
                border = BorderStroke(1.dp, IvoryCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = d.salonName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Surface(shape = RoundedCornerShape(6.dp), color = StateSuccessContainer) {
                            Text(text = d.contractStatus, style = MaterialTheme.typography.labelSmall, color = StateSuccess, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "مدیریت: ${d.contactPerson} • تلفن: ${d.phone}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text(text = "ویزیتور مسئول: ${d.assignedVisitorName} (پورسانت: ${d.visitorCommissionPercent}٪)", style = MaterialTheme.typography.bodySmall, color = PeachPrimary)
                    Text(text = "مدت قرارداد: ${d.contractStartDate} تا ${d.contractEndDate}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun VisitorsPerformanceTab(visitors: List<VisitorAccount>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visitors.forEach { v ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = IvorySurfaceVariant,
                border = BorderStroke(1.dp, IvoryCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${v.name} (${v.id})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "${v.closedContractsCount} قرارداد بسته شده", style = MaterialTheme.typography.labelSmall, color = StateSuccess, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "کل پورسانت کسب‌شده: ${v.totalEarnedCommissionToman / 1000000} میلیون تومان", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text(text = "مانده قابل پرداخت: ${v.remainingCommissionToman / 1000000} میلیون تومان", style = MaterialTheme.typography.bodySmall, color = PeachPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountingWalletTab(
    partnerships: List<CommercialPartnershipRecord>,
    transactions: List<FinancialTransaction>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = StateSuccessContainer,
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "تراکنش‌های قطعی تسویه:", style = MaterialTheme.typography.labelSmall, color = StateSuccess)
                    Text(text = "۲۲,۷۵۰,۰۰۰ تومان", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = StateSuccess)
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = PeachContainer,
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "مبالغ در انتظار و تعهدی:", style = MaterialTheme.typography.labelSmall, color = PeachPrimary)
                    Text(text = "۱۰,۲۰۰,۰۰۰ تومان", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PeachPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "گردش حساب همکاری و پورسانت سالنیار:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextPrimary)

        partnerships.forEach { p ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = IvorySurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = p.proposalType, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = if (p.isSettled) "تسویه شد" else "در جریان", style = MaterialTheme.typography.labelSmall, color = if (p.isSettled) StateSuccess else PeachPrimary, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "${p.sourcePartner} ↔ ${p.targetPartner}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(text = "پورسانت: ${p.commissionAmountToman / 1000} هزار تومان • مانده: ${p.balanceDueToman / 1000} هزار تومان", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }
}
