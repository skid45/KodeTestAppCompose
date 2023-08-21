package com.skid.kodetestappcompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.skid.kodetestappcompose.R


val interFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

val Typography = Typography(
    bodySmall = myTypography.textRegular,
    bodyMedium = myTypography.textMedium,
    bodyLarge = myTypography.textSemibold,
    headlineSmall = myTypography.headlineRegular,
    headlineMedium = myTypography.headlineMedium,
    headlineLarge = myTypography.headlineSemibold,
    labelSmall = myTypography.caption1Regular,
    labelMedium = myTypography.subheadMedium,
    labelLarge = myTypography.subheadSemibold,
    titleMedium = myTypography.title2Semibold,
    titleLarge = myTypography.title1Bold,
    displaySmall = myTypography.title3Regular,
    displayMedium = myTypography.title3Semibold
)

val myTypography get() = MyTypography(
    textRegular = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    textMedium = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    textSemibold = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    headlineRegular = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    headlineSemibold = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    subheadMedium = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    subheadSemibold = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    title1Bold = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    title2Semibold = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    title3Regular = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    title3Semibold = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    caption1Regular = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

@Immutable
data class MyTypography(
    val textRegular: TextStyle,
    val textMedium: TextStyle,
    val textSemibold: TextStyle,
    val headlineRegular: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSemibold: TextStyle,
    val subheadMedium: TextStyle,
    val subheadSemibold: TextStyle,
    val title1Bold: TextStyle,
    val title2Semibold: TextStyle,
    val title3Regular: TextStyle,
    val title3Semibold: TextStyle,
    val caption1Regular: TextStyle,
)