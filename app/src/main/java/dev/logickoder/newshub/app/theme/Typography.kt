package dev.logickoder.newshub.app.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.logickoder.newshub.R

private val PrimaryFontFamily = FontFamily(
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_bold, FontWeight.Bold)
)

val MontserratFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_bold, FontWeight.Bold),
)

val AppTypography: Typography
    get() {
        val typography = Typography()
        return Typography(
            // Large display text for branding/titles
            displayLarge = typography.displayLarge.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp
            ),
            displayMedium = typography.displayMedium.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            ),
            displaySmall = typography.displaySmall.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 32.sp
            ),

            // Headlines for section headers and important text
            headlineLarge = typography.headlineLarge.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            ),
            headlineMedium = typography.headlineMedium.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 26.sp
            ),
            headlineSmall = typography.headlineSmall.copy(
                fontFamily = PrimaryFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            ),

            // Titles for cards, dialogs, and UI components
            titleLarge = typography.titleLarge.copy(
                fontFamily = PrimaryFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp
            ),
            titleMedium = typography.titleMedium.copy(
                fontFamily = PrimaryFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            ),
            titleSmall = typography.titleSmall.copy(
                fontFamily = PrimaryFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp
            ),

            // Body text for content and descriptions
            bodyLarge = typography.bodyLarge.copy(
                fontFamily = PrimaryFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp
            ),
            bodyMedium = typography.bodyMedium.copy(
                fontFamily = PrimaryFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp
            ),
            bodySmall = typography.bodySmall.copy(
                fontFamily = PrimaryFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp
            ),

            // Labels for buttons, tabs, and interactive elements
            labelLarge = typography.labelLarge.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp
            ),
            labelMedium = typography.labelMedium.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            ),
            labelSmall = typography.labelSmall.copy(
                fontFamily = MontserratFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp
            ),
        )
    }