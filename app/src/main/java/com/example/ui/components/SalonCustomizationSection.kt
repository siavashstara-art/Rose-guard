package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.SalonProfile
import com.example.model.SalonTemplateLayout
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

@Composable
fun SalonCustomizationSection(
    profiles: List<SalonProfile>,
    activeProfile: SalonProfile,
    onSwitchProfile: (String) -> Unit,
    onSaveProfile: (SalonProfile) -> Unit,
    onCreateNewProfile: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var draftName by remember(activeProfile) { mutableStateOf(activeProfile.salonName) }
    var draftManager by remember(activeProfile) { mutableStateOf(activeProfile.managerName) }
    var draftAddress by remember(activeProfile) { mutableStateOf(activeProfile.address) }
    var draftPhone by remember(activeProfile) { mutableStateOf(activeProfile.phone) }
    var draftHours by remember(activeProfile) { mutableStateOf(activeProfile.workingHours) }
    var draftTemplate by remember(activeProfile) { mutableStateOf(activeProfile.selectedTemplate) }

    val mainPhotoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            onSaveProfile(activeProfile.copy(mainPhotoUri = uri.toString()))
        }
    }

    val bannerPhotoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            onSaveProfile(activeProfile.copy(bannerPhotoUri = uri.toString()))
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("salon_customization_section"),
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
                        color = PeachContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = PeachPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "شخصی‌سازی سریع ویترین سالن (ویزیتور)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "پیش‌نمایش اختصاصی بدون نیاز به کد لایسنس پولی",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = StateSuccessContainer
                ) {
                    Text(
                        text = "پیش‌نمایش رایگان ویزیتور",
                        style = MaterialTheme.typography.labelSmall,
                        color = StateSuccess,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Salon Profile Switcher Pills
            Text(
                text = "انتخاب پروفایل سالن (تغییرات هر سالن کاملاً مجزا ذخیره می‌شود):",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                profiles.forEach { profile ->
                    val isSelected = profile.id == activeProfile.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSwitchProfile(profile.id) },
                        label = { Text(profile.salonName, maxLines = 1) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PeachContainer,
                            selectedLabelColor = PeachPrimary
                        ),
                        modifier = Modifier.testTag("salon_profile_tab_${profile.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Template Layout Picker (3 layouts)
            Text(
                text = "انتخاب قالب چیدمان صفحه اول سالن:",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SalonTemplateLayout.values().forEach { template ->
                    FilterChip(
                        selected = activeProfile.selectedTemplate == template,
                        onClick = {
                            draftTemplate = template
                            onSaveProfile(activeProfile.copy(selectedTemplate = template))
                        },
                        label = { Text(template.titlePersian, style = MaterialTheme.typography.labelSmall, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftPinkContainer,
                            selectedLabelColor = SoftPink
                        ),
                        modifier = Modifier.testTag("template_chip_${template.name}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Form or Summary Card
            if (isEditing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(IvorySurfaceVariant, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "ویرایش مشخصات سالن و مدیر:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = draftName,
                        onValueChange = { draftName = it },
                        label = { Text("نام آرایشگاه / سالن") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = draftManager,
                        onValueChange = { draftManager = it },
                        label = { Text("نام مدیر یا صاحب سالن") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = draftAddress,
                        onValueChange = { draftAddress = it },
                        label = { Text("نشانی سالن") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = draftPhone,
                        onValueChange = { draftPhone = it },
                        label = { Text("شماره تلفن‌های پذیرش") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = draftHours,
                        onValueChange = { draftHours = it },
                        label = { Text("ساعات کاری / نوبت‌دهی") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val updated = activeProfile.copy(
                                salonName = draftName.trim(),
                                managerName = draftManager.trim(),
                                address = draftAddress.trim(),
                                phone = draftPhone.trim(),
                                workingHours = draftHours.trim()
                            )
                            onSaveProfile(updated)
                            isEditing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PeachPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp).testTag("save_salon_details_btn")
                    ) {
                        Text(text = "ذخیره تغییرات سالن", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Live Rendered Front Page Preview Card of the Salon
                SalonShowcaseCard(
                    profile = activeProfile,
                    onEditClick = { isEditing = true },
                    onPickMainPhoto = { mainPhotoPicker.launch("image/*") },
                    onPickBannerPhoto = { bannerPhotoPicker.launch("image/*") }
                )
            }
        }
    }
}

@Composable
private fun SalonShowcaseCard(
    profile: SalonProfile,
    onEditClick: () -> Unit,
    onPickMainPhoto: () -> Unit,
    onPickBannerPhoto: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("salon_showcase_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = IvorySurfaceVariant),
        border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Template 1: Top Hero
            if (profile.selectedTemplate == SalonTemplateLayout.TOP_HERO) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFEDE4DC))
                        .clickable { onPickBannerPhoto() },
                    contentAlignment = Alignment.Center
                ) {
                    if (profile.bannerPhotoUri != null) {
                        AsyncImage(
                            model = profile.bannerPhotoUri,
                            contentDescription = "عکس هدر سالن",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "برای انتخاب عکس هدر بالای صفحه ضربه بزنید", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Salon Name & Manager
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.salonName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "مدیریت: ${profile.managerName}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = PeachPrimary
                    )
                }

                OutlinedButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier.height(34.dp).testTag("edit_salon_profile_btn")
                ) {
                    Text(text = "ویرایش متن", style = MaterialTheme.typography.labelSmall, color = PeachPrimary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Details rows (Address, Phone, Hours)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = SoftPink, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = profile.address, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = PeachPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = profile.phone, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = profile.workingHours, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Image selection action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPickMainPhoto,
                    modifier = Modifier.weight(1f).height(38.dp).testTag("pick_main_photo_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = IvorySurface, contentColor = TextPrimary),
                    border = BorderStroke(1.dp, IvoryCardBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (profile.mainPhotoUri != null) "تغییر عکس سالن" else "افزودن عکس سالن", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = onPickBannerPhoto,
                    modifier = Modifier.weight(1f).height(38.dp).testTag("pick_banner_photo_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = IvorySurface, contentColor = TextPrimary),
                    border = BorderStroke(1.dp, IvoryCardBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "عکس بالای صفحه", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
