package dev.logickoder.newshub.app.domain

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

/**
 * Represents a color in HSL (Hue, Saturation, Lightness) format
 * @param h Hue (0.0-360.0 degrees)
 * @param s Saturation (0.0-1.0)
 * @param l Lightness (0.0-1.0)
 * @param alpha Alpha value (0.0-1.0)
 */
data class HSLColor(
    val h: Float, // 0-360 degrees
    @FloatRange(from = 0.0, to = 1.0) val s: Float, // 0-1
    @FloatRange(from = 0.0, to = 1.0) val l: Float, // 0-1
    @FloatRange(from = 0.0, to = 1.0) val alpha: Float = 1f // 0-1
)

/**
 * Converts a Compose Color to HSL format
 * @return HSLColor representation of the input Color
 */
fun Color.toHSL(): HSLColor {
    // Normalize RGB values to 0-1 range
    val r = red
    val g = green
    val b = blue

    val max = max(max(r, g), b)
    val min = min(min(r, g), b)

    // Calculate lightness
    val l = (max + min) / 2f

    // If max equals min, it's a shade of gray (no hue or saturation)
    if (max == min) {
        return HSLColor(0f, 0f, l, alpha)
    }

    // Calculate delta for saturation
    val delta = max - min

    // Calculate saturation
    val s = if (l > 0.5f) {
        delta / (2f - max - min)
    } else {
        delta / (max + min)
    }

    // Calculate hue
    val h = when (max) {
        r -> (g - b) / delta + (if (g < b) 6f else 0f)
        g -> (b - r) / delta + 2f
        else -> (r - g) / delta + 4f
    } * 60f

    return HSLColor(h, s, l, alpha)
}

/**
 * Converts an HSLColor to Compose Color
 * @return Compose Color representation of the HSL color
 */
fun HSLColor.toColor(): Color {
    // Handle grayscale case
    if (s == 0f) {
        return Color(l, l, l, alpha)
    }

    // Helper function for hue to RGB conversion
    fun hueToRgb(p: Float, q: Float, t: Float): Float {
        var tAdjusted = t
        if (tAdjusted < 0f) tAdjusted += 1f
        if (tAdjusted > 1f) tAdjusted -= 1f

        return when {
            tAdjusted < 1f / 6f -> p + (q - p) * 6f * tAdjusted
            tAdjusted < 1f / 2f -> q
            tAdjusted < 2f / 3f -> p + (q - p) * (2f / 3f - tAdjusted) * 6f
            else -> p
        }
    }

    val q = if (l < 0.5f) {
        l * (1 + s)
    } else {
        l + s - l * s
    }

    val p = 2f * l - q
    val normalizedHue = h / 360f

    val r = hueToRgb(p, q, normalizedHue + 1f / 3f)
    val g = hueToRgb(p, q, normalizedHue)
    val b = hueToRgb(p, q, normalizedHue - 1f / 3f)

    return Color(r, g, b, alpha)
}

/**
 * Adjusts lightness of a color by a specified percentage
 * @param percentage Percentage to adjust lightness by (-1.0 to 1.0)
 * @return A new color with adjusted lightness
 */
fun Color.adjustLightness(@FloatRange(from = 0.0, to = 1.0) percentage: Float): Color {
    val hsl = this.toHSL()
    val newLightness = (hsl.l + percentage).coerceIn(0f, 1f)
    return HSLColor(hsl.h, hsl.s, newLightness, hsl.alpha).toColor()
}

/**
 * Adjusts saturation of a color by a specified percentage
 * @param percentage Percentage to adjust saturation by (-1.0 to 1.0)
 * @return A new color with adjusted saturation
 */
fun Color.adjustSaturation(@FloatRange(from = 0.0, to = 1.0) percentage: Float): Color {
    val hsl = this.toHSL()
    val newSaturation = (hsl.s + percentage).coerceIn(0f, 1f)
    return HSLColor(hsl.h, newSaturation, hsl.l, hsl.alpha).toColor()
}

/**
 * Convenience function to lighten a color by a percentage
 * @param percentage Percentage to lighten (0.0-1.0)
 * @return A new, lightened color
 */
fun Color.lighten(@FloatRange(from = 0.0, to = 1.0) percentage: Float): Color {
    return this.adjustLightness(percentage)
}

/**
 * Convenience function to darken a color by a percentage
 * @param percentage Percentage to darken (0.0-1.0)
 * @return A new, darkened color
 */
fun Color.darken(@FloatRange(from = 0.0, to = 1.0) percentage: Float): Color {
    return this.adjustLightness(-percentage)
}

/**
 * Create a Compose Color from a hex string with format "#RRGGBB" or "#AARRGGBB"
 * @param hexString Hex string representation of the color
 * @return Compose Color
 */
fun colorFromHex(hexString: String): Color {
    val hex = hexString.removePrefix("#")

    return when (hex.length) {
        6 -> {
            Color(
                red = hex.take(2).toInt(16) / 255f,
                green = hex.substring(2, 4).toInt(16) / 255f,
                blue = hex.substring(4, 6).toInt(16) / 255f
            )
        }

        8 -> {
            Color(
                alpha = hex.take(2).toInt(16) / 255f,
                red = hex.substring(2, 4).toInt(16) / 255f,
                green = hex.substring(4, 6).toInt(16) / 255f,
                blue = hex.substring(6, 8).toInt(16) / 255f
            )
        }

        else -> throw IllegalArgumentException("Invalid hex color format: $hexString. Use #RRGGBB or #AARRGGBB")
    }
}

/**
 * Extension function to convert a Compose Color to a hex string
 * @param includeAlpha Whether to include alpha in the hex string
 * @return Hex string representation of the color
 */
fun Color.toHexString(includeAlpha: Boolean = false): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    val a = (alpha * 255).toInt()

    return if (includeAlpha) {
        String.format("#%02X%02X%02X%02X", a, r, g, b)
    } else {
        String.format("#%02X%02X%02X", r, g, b)
    }
}

/**
 * Example usage to lighten a color for dark mode
 * @param hexColor Hex string representation of the color to lighten
 * @param percentage Percentage to lighten (0.0-1.0)
 * @return Lightened hex color string
 */
fun lightenForDarkMode(hexColor: String, percentage: Float = 0.3f): String {
    val color = colorFromHex(hexColor)
    return color.lighten(percentage).toHexString()
}

/**
 * Inverts a color by subtracting each RGB component from 1
 */
fun Color.inverse(): Color = Color(
    red = 1f - red,
    green = 1f - green,
    blue = 1f - blue,
    alpha = alpha
)
