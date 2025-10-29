package dev.logickoder.newshub.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun AppTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = getColorScheme(darkMode)
    val scheme = if (darkMode) darkColorScheme() else lightColorScheme()

    val colorScheme = remember(scheme) {
        scheme.copy(
            primary = colors.primary,
            onPrimary = colors.white,
            primaryContainer = colors.primary.copy(alpha = 0.1f),
            onPrimaryContainer = colors.primary,

            secondary = colors.secondary,
            onSecondary = colors.white,
            secondaryContainer = colors.secondary.copy(alpha = 0.1f),
            onSecondaryContainer = colors.secondary,

            tertiary = colors.primary,
            onTertiary = colors.white,
            tertiaryContainer = colors.primary.copy(alpha = 0.1f),
            onTertiaryContainer = colors.primary,

            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surface,
            onSurfaceVariant = colors.placeholder,

            background = colors.background,
            onBackground = colors.onBackground,

            error = colors.error,
            onError = colors.white,
            errorContainer = colors.error.copy(alpha = 0.1f),
            onErrorContainer = colors.error,

            outline = colors.border,
            outlineVariant = colors.border.copy(alpha = 0.5f),

            surfaceTint = colors.primary
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = {
            CompositionLocalProvider(
                LocalAppColors provides colors,
                LocalContentColor provides colors.onBackground, // Default content color
                content = content
            )
        }
    )
}