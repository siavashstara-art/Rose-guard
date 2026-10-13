package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Blur & Lock wrapper component for VIP features until license activation.
 */
@Composable
fun LicenseLockedContainer(
    isLocked: Boolean,
    featureTitle: String,
    onActivateCode: (String) -> Boolean,
    onNavigateToLicenseTab: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        // Main content (blurred if locked)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (isLocked) Modifier.blur(16.dp) else Modifier)
        ) {
            content()
        }

        // Lock & Blur overlay
        if (isLocked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xE60D0814))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                var inputCode by remember { mutableStateOf("") }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1D1226)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(Color(0xFFD4AF37), Color(0xFF6B21A8))
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .testTag("lock_card_$featureTitle")
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF3B1D54), RoundedCornerShape(50))
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "قفل امنیتی",
                                tint = Color(0xFFD4AF37)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "بخش تخصصی قفل است: $featureTitle",
                            color = Color(0xFFF7E7CE),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "این بخش با لایسنس تجاری ۱ ساله (به ارزش ۹,۰۰۰,۰۰۰ تومان) بازگشایی می‌گردد. جهت فعال‌سازی، کد اختصاصی ارائه شده توسط ویزیتور را وارد کنید.",
                            color = Color(0xFFD1C4E9),
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputCode,
                            onValueChange = { inputCode = it },
                            placeholder = { Text("مثال: SYAR-9M01-A48F-72BC", color = Color.Gray, fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("license_input_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFFD4AF37),
                                unfocusedBorderColor = Color(0xFF553C7B),
                                focusedContainerColor = Color(0xFF120B1A),
                                unfocusedContainerColor = Color(0xFF120B1A)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (inputCode.isNotBlank()) {
                                    onActivateCode(inputCode)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD4AF37)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("activate_license_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VpnKey,
                                contentDescription = null,
                                tint = Color(0xFF1A0F26)
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            Text(
                                text = "فعال‌سازی آنی لایسنس سالن",
                                color = Color(0xFF1A0F26),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onNavigateToLicenseTab,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF381F54)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "مشاهده پنل ویزیتور و ۱۰۰ کد فعال‌سازی",
                                color = Color(0xFFE9D5FF),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
