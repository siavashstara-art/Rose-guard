package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PeachPrimary,
    onPrimary = Color.White,
    primaryContainer = PeachContainer,
    onPrimaryContainer = OnPeachContainer,
    secondary = SoftPink,
    onSecondary = Color.White,
    secondaryContainer = SoftPinkContainer,
    onSecondaryContainer = OnSoftPinkContainer,
    tertiary = WarmPeachAccent,
    onTertiary = Color.White,
    background = IvoryBackground,
    onBackground = TextPrimary,
    surface = IvorySurface,
    onSurface = TextPrimary,
    surfaceVariant = IvorySurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = IvoryCardBorder,
    outlineVariant = IvoryCardBorder.copy(alpha = 0.5f),
    error = StateError,
    onError = Color.White,
    errorContainer = StateErrorContainer,
    onErrorContainer = StateError
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
