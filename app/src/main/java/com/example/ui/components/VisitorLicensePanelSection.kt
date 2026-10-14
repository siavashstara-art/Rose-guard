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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LicenseCodeGenerator
import com.example.model.LicenseState
import com.example.model.SalonProfile

@Composable
fun VisitorLicensePanelSection(
    licenseState: LicenseState,
    salonProfile: SalonProfile,
    onActivateLicense: (String) -> Boolean,
    onCopyLicenseCode: (String) -> Unit,
    onSaveProfile: (SalonProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var editSalonName by remember(salonProfile) { mutableStateOf(salonProfile.salonName) }
    var editManagerName by remember(salonProfile) { mutableStateOf(salonProfile.managerName) }
    var editPhone by remember(salonProfile) { mutableStateOf(salonProfile.phoneNumber) }
    var editLandline by remember(salonProfile) { mutableStateOf(salonProfile.landlinePhone) }
    var editAddress by remember(salonProfile) { mutableStateOf(salonProfile.address) }
    var editInstagram by remember(salonProfile) { mutableStateOf(salonProfile.instagramId) }
    var editTelegram by remember(salonProfile) { mutableStateOf(salonProfile.telegramId) }
    var editBookingLink by remember(salonProfile) { mutableStateOf(salonProfile.websiteOrBookingLink) }

    var manualCodeInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Hero Header
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF241136)),
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
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD4AF37), CircleShape)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = Color(0xFF1B0B26)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "پنل ویزیتور و مدیریت لایسنس سالن",
                        color = Color(0xFFF7E7CE),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "شخصی‌سازی در محل سالن + ۱۰۰ کد فعال‌سازی ۱ ساله (ارزش ۹ میلیون تومان)",
                        color = Color(0xFFD1C4E9),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        var activePanelMode by remember { mutableStateOf(0) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E0E2C))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activePanelMode == 0) Color(0xFFD4AF37) else Color.Transparent)
                    .clickable { activePanelMode = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "دفتر مدیریت فروش، تیکت و ارجاع عروس",
                    color = if (activePanelMode == 0) Color(0xFF1B0B26) else Color(0xFFE9D5FF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activePanelMode == 1) Color(0xFFD4AF37) else Color.Transparent)
                    .clickable { activePanelMode = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "شخصی‌سازی در محل و ۱۰۰ لایسنس",
                    color = if (activePanelMode == 1) Color(0xFF1B0B26) else Color(0xFFE9D5FF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activePanelMode == 0) {
            VisitorManagementLedgerSection()
        } else {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (licenseState.isActivated) Color(0xFF14291B) else Color(0xFF2B131D)
            ),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(
                1.5.dp,
                if (licenseState.isActivated) Color(0xFF4ADE80) else Color(0xFFF87171)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            if (licenseState.isActivated) Color(0xFF4ADE80) else Color(0xFFF87171),
                            CircleShape
                        )
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = if (licenseState.isActivated) Icons.Default.CheckCircle else Icons.Default.VpnKey,
                        contentDescription = null,
                        tint = Color(0xFF101B14)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (licenseState.isActivated) "لایسنس ۱ ساله فعال است" else "نسخه دمو (بخش‌های ویژه قفل است)",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (licenseState.isActivated)
                            "کد فعال: ${licenseState.activeCode} (اعتبار: ${licenseState.validDaysRemaining} روز)"
                        else
                            "ارزش لایسنس یک‌ساله: ۹,۰۰۰,۰۰۰ تومان - جهت بازگشایی از کدهای زیر فعال نمایید.",
                        color = if (licenseState.isActivated) Color(0xFF86EFAC) else Color(0xFFFCA5A5),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Activation Input Box
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF190F24)),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "فعال‌سازی با کد لایسنس ویزیتور:",
                    color = Color(0xFFECC34C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = manualCodeInput,
                    onValueChange = { manualCodeInput = it },
                    placeholder = { Text("کد فعال‌سازی (مثال: SYAR-9M01-A48F-72BC)", color = Color.Gray, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
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
                        if (manualCodeInput.isNotBlank()) {
                            onActivateLicense(manualCodeInput)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, tint = Color(0xFF1B0B26))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "فعال‌سازی رسمی لایسنس سالن",
                        color = Color(0xFF1B0B26),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Visitor On-site Customization Form (Core Feature 6)
        Text(
            text = "مشخصات آرایشگاه (شخصی‌سازی توسط ویزیتور در محل):",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "ویزیتور این اطلاعات را در حضور مدیر سالن ثبت می‌کند تا برند اختصاصی آرایشگاه روی کل سیستم اعمال شود.",
            color = Color(0xFFD1C4E9),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = editSalonName,
            onValueChange = { editSalonName = it },
            label = { Text("نام سالن زیبایی و عروس", color = Color(0xFFC4B5FD)) },
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
            value = editManagerName,
            onValueChange = { editManagerName = it },
            label = { Text("نام مدیر سالن", color = Color(0xFFC4B5FD)) },
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
                value = editPhone,
                onValueChange = { editPhone = it },
                label = { Text("شماره همراه مشاوره", color = Color(0xFFC4B5FD)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color(0xFF4A2F6E)
                )
            )
            OutlinedTextField(
                value = editLandline,
                onValueChange = { editLandline = it },
                label = { Text("تلفن ثابت سالن", color = Color(0xFFC4B5FD)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color(0xFF4A2F6E)
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = editAddress,
            onValueChange = { editAddress = it },
            label = { Text("آدرس سالن و لوکیشن", color = Color(0xFFC4B5FD)) },
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
                value = editInstagram,
                onValueChange = { editInstagram = it },
                label = { Text("پیج اینستاگرام (@)", color = Color(0xFFC4B5FD)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color(0xFF4A2F6E)
                )
            )
            OutlinedTextField(
                value = editTelegram,
                onValueChange = { editTelegram = it },
                label = { Text("کانال تلگرام (@)", color = Color(0xFFC4B5FD)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color(0xFF4A2F6E)
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = editBookingLink,
            onValueChange = { editBookingLink = it },
            label = { Text("لینک اینترنتی رزرو نوبت", color = Color(0xFFC4B5FD)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFD4AF37),
                unfocusedBorderColor = Color(0xFF4A2F6E)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                onSaveProfile(
                    salonProfile.copy(
                        salonName = editSalonName,
                        managerName = editManagerName,
                        phoneNumber = editPhone,
                        landlinePhone = editLandline,
                        address = editAddress,
                        instagramId = editInstagram,
                        telegramId = editTelegram,
                        websiteOrBookingLink = editBookingLink
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "ذخیره و اعمال برندینگ در کل برنامه و استوری‌ها",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        // 100 VIP License Codes Catalog for Visitor Sales (Core Feature 8)
        Text(
            text = "فهرست ۱۰۰ کد فعال‌سازی ۱ ساله (ارزش ۹ میلیون تومان):",
            color = Color(0xFFECC34C),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "برای فعال‌سازی در سالن مشتری، روی هر کد کلیک کنید تا کپی و اعمال شود:",
            color = Color(0xFFD1C4E9),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Display first 25 sample codes directly, and show total 100 available
        val displayCodes = LicenseCodeGenerator.ALL_100_LICENSE_CODES.take(30)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            displayCodes.forEachIndexed { idx, code ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF160D20)),
                    border = BorderStroke(1.dp, Color(0xFF381F54)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCopyLicenseCode(code)
                            manualCodeInput = code
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "#${idx + 1}",
                                color = Color(0xFFD4AF37),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(36.dp)
                            )
                            Text(
                                text = code,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "کپی",
                            tint = Color(0xFFD4AF37)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "... به همراه ۷۰ کد دیگر در بانک کدهای ویزیتور اختصاصی توانا",
            color = Color.Gray,
            fontSize = 11.sp
        )
        }
    }
}
