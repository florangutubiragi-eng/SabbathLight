package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val SabbathDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = NavyDarkBackground,
    primaryContainer = NavyDarkCard,
    onPrimaryContainer = GoldLight,
    secondary = RoyalBlueLight,
    onSecondary = Color.White,
    secondaryContainer = RoyalBlueContainer,
    onSecondaryContainer = Color(0xFFDBEAFE),
    tertiary = GoldLight,
    onTertiary = NavyDarkBackground,
    background = NavyDarkBackground,
    onBackground = TextLightPrimary,
    surface = NavyDarkSurface,
    onSurface = TextLightPrimary,
    surfaceVariant = NavyDarkSurfaceVariant,
    onSurfaceVariant = TextLightSecondary,
    outline = Color(0xFF2B476F),
    outlineVariant = Color(0xFF1E3555)
)

private val SabbathLightColorScheme = lightColorScheme(
    primary = RoyalBluePrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalBlueSoft,
    onPrimaryContainer = RoyalBlueDark,
    secondary = GoldDark,
    onSecondary = Color.White,
    secondaryContainer = GoldContainerLight,
    onSecondaryContainer = GoldDark,
    tertiary = GoldPrimary,
    onTertiary = NavyDarkBackground,
    background = LightBackground,
    onBackground = TextDarkPrimary,
    surface = LightSurface,
    onSurface = TextDarkPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextDarkSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun SabbathLifeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded navy & gold aesthetics
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> SabbathDarkColorScheme
        else -> SabbathLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
