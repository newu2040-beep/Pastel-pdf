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

fun getThemeColor(hex: String, isDark: Boolean): Color {
    return when(hex) {
        "#FFB3BA" -> if (isDark) PastelDarkPink else PastelPink
        "#BAE1FF" -> if (isDark) PastelDarkBlue else PastelBlue
        "#B5EAD7" -> if (isDark) PastelDarkGreen else PastelGreen
        "#E2B6FF" -> if (isDark) PastelDarkPurple else PastelPurple
        else -> if (isDark) PastelDarkPink else PastelPink
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic colors to enforce pastel
    primaryColorHex: String = "#FFB3BA", // Default to pink
    content: @Composable () -> Unit,
) {
    val primaryColor = getThemeColor(primaryColorHex, darkTheme)
    
    val colorScheme = if (darkTheme) {
        darkColorScheme(
             primary = primaryColor,
             secondary = PastelDarkBlue,
             tertiary = PastelDarkGreen,
             background = BackgroundDark,
             surface = SurfaceDark,
             onPrimary = TextDark,
             onBackground = TextDark,
             onSurface = TextDark
        )
    } else {
        lightColorScheme(
             primary = primaryColor,
             secondary = PastelBlue,
             tertiary = PastelGreen,
             background = BackgroundLight,
             surface = SurfaceLight,
             onPrimary = TextLight,
             onBackground = TextLight,
             onSurface = TextLight
        )
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
