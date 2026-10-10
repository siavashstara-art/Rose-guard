package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LicenseStatus
import com.example.model.SalonLicense
import com.example.service.LicenseValidationService
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
fun LicenseStatusCard(
    activeLicense: SalonLicense?,
    licenseFeedback: String?,
    onSubmitLicenseCode: (String) -> Unit,
    onDeactivateLicense: () -> Unit,
    onClearFeedback: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showInputDialog by remember { mutableStateOf(false) }
    var codeDraft by remember { mutableStateOf("") }

    val isLicensed = activeLicense != null && activeLicense.status == LicenseStatus.ACTIVE

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("license_status_card"),
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
                        color = if (isLicensed) StateSuccessContainer else PeachContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isLicensed) Icons.Default.VerifiedUser else Icons.Default.Key,
                                contentDescription = null,
                                tint = if (isLicensed) StateSuccess else PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "وضعیت مجوز و لایسنس برنامه",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "سیستم تخصیص لایسنس به ۲۰۰ مشتری اولیه سالنیار",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isLicensed) StateSuccessContainer else IvorySurfaceVariant
                ) {
                    Text(
                        text = if (isLicensed) "لایسنس فعال (${activeLicense?.formattedIndex})" else "نسخه رایگان سالن",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isLicensed) StateSuccess else TextSecondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // License Status Details
            if (isLicensed) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = StateSuccessContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "شناسه مجوز: ${activeLicense?.formattedIndex}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = StateSuccess
                            )
                            Text(
                                text = activeLicense?.tier?.titlePersian ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "کد کلید: ${activeLicense?.clientSecretKey}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "تاریخ اعتبار: ${activeLicense?.activationDate} الی ${activeLicense?.expirationDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDeactivateLicense,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, StateError.copy(alpha = 0.4f)),
                        modifier = Modifier.height(36.dp).testTag("deactivate_license_btn")
                    ) {
                        Text(text = "حذف لایسنس از این دستگاه", color = StateError, style = MaterialTheme.typography.labelSmall)
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = IvorySurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "امکانات پایه (انتخاب عکس، پخش صدا و شخصی‌سازی سالن) به صورت رایگان فعال است.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "برای باز کردن خروجی ویدیویی با کیفیت بالا و ابزارهای هوش مصنوعی پیشرفته، کد ۱۶ رقمی لایسنس اختصاصی خود را وارد نمایید.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 19.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { showInputDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("open_license_input_btn")
                ) {
                    Icon(imageVector = Icons.Default.Key, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "ثبت و فعال‌سازی کد لایسنس مشتری", fontWeight = FontWeight.Bold)
                }
            }

            // Feedback Banner (e.g. error, expired, or success)
            if (licenseFeedback != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isLicensed) StateSuccessContainer else StateErrorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = licenseFeedback,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isLicensed) StateSuccess else StateError,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onClearFeedback) {
                            Text(text = "تایید", color = if (isLicensed) StateSuccess else StateError)
                        }
                    }
                }
            }
        }
    }

    // Input Key Dialog
    if (showInputDialog) {
        val sampleCode = LicenseValidationService.generateSecretKeyForIndex(1)
        AlertDialog(
            onDismissRequest = { showInputDialog = false },
            modifier = Modifier.testTag("license_input_dialog"),
            shape = RoundedCornerShape(22.dp),
            containerColor = IvorySurface,
            title = {
                Text(
                    text = "فعال‌سازی کد اختصاصی لایسنس سالنیار",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "کد لایسنس دریافتی از نماینده توانا را وارد فرمایید:\n(فرمت کد: TAV-SALON-XXX-YYYY-ZZZZ)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = codeDraft,
                        onValueChange = { codeDraft = it },
                        placeholder = { Text(sampleCode, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("license_code_input_field"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // Sample code quick paste helper for testing
                    OutlinedButton(
                        onClick = { codeDraft = sampleCode },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp).testTag("quick_paste_license_sample_btn")
                    ) {
                        Text(text = "درج کد نمونه مشتری ۰۰۱ جهت آزمون", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSubmitLicenseCode(codeDraft)
                        showInputDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("submit_license_confirm_btn")
                ) {
                    Text(text = "بررسی و فعال‌سازی", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showInputDialog = false }) {
                    Text(text = "انصراف", color = TextSecondary)
                }
            }
        )
    }
}
