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
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import com.example.model.BusinessOffer
import com.example.model.BusinessOfferCategory
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
import com.example.viewmodel.AudioStudioViewModel

@Composable
fun BusinessOffersSection(
    offers: List<BusinessOffer>,
    onSeekToTimestamp: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<BusinessOfferCategory?>(null) }

    val filteredOffers = if (selectedFilter == null) {
        offers
    } else {
        offers.filter { it.category == selectedFilter }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("business_offers_section"),
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
                                imageVector = Icons.Default.BusinessCenter,
                                contentDescription = null,
                                tint = SoftPink,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "پیشنهادهای تجاری سالنیار و توانا",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "تحلیل مستندات سخنرانی در سه دسته مجزا",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = IvorySurfaceVariant
                ) {
                    Text(
                        text = "${offers.size} مورد استخراج‌شده",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3-Category Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("همه موارد (${offers.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PeachContainer,
                        selectedLabelColor = PeachPrimary
                    )
                )

                FilterChip(
                    selected = selectedFilter == BusinessOfferCategory.CONFIRMED_FEATURE,
                    onClick = {
                        selectedFilter = if (selectedFilter == BusinessOfferCategory.CONFIRMED_FEATURE) null else BusinessOfferCategory.CONFIRMED_FEATURE
                    },
                    label = { Text("قابلیت محصول") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StateSuccessContainer,
                        selectedLabelColor = StateSuccess
                    )
                )

                FilterChip(
                    selected = selectedFilter == BusinessOfferCategory.SPEECH_OFFER,
                    onClick = {
                        selectedFilter = if (selectedFilter == BusinessOfferCategory.SPEECH_OFFER) null else BusinessOfferCategory.SPEECH_OFFER
                    },
                    label = { Text("پیشنهاد سخنرانی") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PeachContainer,
                        selectedLabelColor = PeachPrimary
                    )
                )

                FilterChip(
                    selected = selectedFilter == BusinessOfferCategory.FUTURE_IDEA,
                    onClick = {
                        selectedFilter = if (selectedFilter == BusinessOfferCategory.FUTURE_IDEA) null else BusinessOfferCategory.FUTURE_IDEA
                    },
                    label = { Text("ایده آینده") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SoftPinkContainer,
                        selectedLabelColor = SoftPink
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of Extracted Offers
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredOffers.isEmpty()) {
                    Text(
                        text = "هیچ پیشنهادی در این دسته یافت نشد.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                filteredOffers.forEach { offer ->
                    BusinessOfferItemCard(
                        offer = offer,
                        onSeekToTimestamp = {
                            offer.timestampSec?.let { onSeekToTimestamp(it) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BusinessOfferItemCard(
    offer: BusinessOffer,
    onSeekToTimestamp: () -> Unit
) {
    val (badgeBg, badgeText, badgeColor) = when (offer.category) {
        BusinessOfferCategory.CONFIRMED_FEATURE -> Triple(StateSuccessContainer, "الف) قابلیت موجود در سالنیار", StateSuccess)
        BusinessOfferCategory.SPEECH_OFFER -> Triple(PeachContainer, "ب) پیشنهاد تجاری در سخنرانی", PeachPrimary)
        BusinessOfferCategory.FUTURE_IDEA -> Triple(SoftPinkContainer, "ج) ایده آینده (در حال توسعه)", SoftPink)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("business_offer_card_${offer.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = IvorySurfaceVariant),
        border = BorderStroke(1.dp, IvoryCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Category Badge & Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                if (offer.timestampSec != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IvorySurface
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AudioStudioViewModel.formatSeconds(offer.timestampSec),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = offer.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = offer.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Verbatim Quote from the speech
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = IvorySurface,
                border = BorderStroke(1.dp, IvoryCardBorder.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "نقل قول از سخنرانی",
                        tint = PeachPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "«${offer.sourceQuote}»",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 19.sp
                    )
                }
            }

            if (offer.timestampSec != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onSeekToTimestamp,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("play_offer_timestamp_${offer.id}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IvorySurface,
                        contentColor = PeachPrimary
                    ),
                    border = BorderStroke(1.dp, PeachPrimary.copy(alpha = 0.5f)),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "شنیدن این بخش در صوت اصلی",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
