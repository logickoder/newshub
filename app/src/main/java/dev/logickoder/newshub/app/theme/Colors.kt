package dev.logickoder.newshub.app.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppColors = compositionLocalOf<AppColors> {
    LightColors
}

sealed interface AppColors {
    // Base colors
    val white: Color
    val black: Color

    // Brand colors
    val primary: Color           // Main brand color (blue-based)
    val secondary: Color         // Secondary brand color (darker blue)

    // Backgrounds
    val background: Color        // Main app background
    val surface: Color          // Card and surface background

    // Content colors
    val onBackground: Color     // Text on background
    val onSurface: Color        // Text on surfaces
    val placeholder: Color      // Placeholder text

    // Semantic colors
    val error: Color            // Error states
    val success: Color          // Success states

    // UI elements
    val border: Color           // Borders and dividers
}

private data object LightColors : AppColors {
    override val white = Color.White
    override val black = Color.Black

    override val primary = Color(0xFF308DC8)
    override val secondary = Color(0xFF1E5F8C)

    override val background = Color(0xFFF8F9FA)
    override val surface = Color.White

    override val onBackground = Color(0xFF1A1C20)
    override val onSurface = Color(0xFF1A1C20)
    override val placeholder = Color(0xFF6B7280)

    override val error = Color(0xFFDC2626)
    override val success = Color(0xFF059669)

    override val border = Color(0xFFE5E7EB)
}

private data object DarkColors : AppColors {
    override val white = Color.White
    override val black = Color.Black

    override val primary = Color(0xFF60A5F2)
    override val secondary = Color(0xFF4F8FD9)

    override val background = Color(0xFF0F1419)
    override val surface = Color(0xFF1A1F2E)

    override val onBackground = Color(0xFFF3F4F6)
    override val onSurface = Color(0xFFF3F4F6)
    override val placeholder = Color(0xFF9CA3AF)

    override val error = Color(0xFFEF4444)
    override val success = Color(0xFF10B981)

    override val border = Color(0xFF374151)
}

fun getColorScheme(darkTheme: Boolean): AppColors {
    return if (darkTheme) DarkColors else LightColors
}