package com.example.medprotocols.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// An original palette for this app: a clinical teal (not the generic SaaS blue)
// paired with a prescription-stamp wine/burgundy accent, on a cool paper
// background rather than a warm cream. Not derived from any existing app.
private val Teal90 = Color(0xFF0B4A3F)
private val Teal60 = Color(0xFF0F6E5E)
private val Teal30 = Color(0xFFBFE3DA)
private val Wine60 = Color(0xFF8E3B42)
private val Wine30 = Color(0xFFEBD3D3)
private val Paper = Color(0xFFF1F3F0)
private val Ink90 = Color(0xFF1E2420)

private val LightColors = lightColorScheme(
    primary = Teal60,
    onPrimary = Color.White,
    primaryContainer = Teal30,
    onPrimaryContainer = Teal90,
    secondary = Wine60,
    onSecondary = Color.White,
    secondaryContainer = Wine30,
    onSecondaryContainer = Color(0xFF3D1518),
    background = Paper,
    onBackground = Ink90,
    surface = Paper,
    onSurface = Ink90,
    outlineVariant = Color(0xFFD8DDD8)
)

private val DarkColors = darkColorScheme(
    primary = Teal30,
    onPrimary = Teal90,
    primaryContainer = Teal60,
    onPrimaryContainer = Color.White,
    secondary = Wine30,
    onSecondary = Color(0xFF3D1518),
    secondaryContainer = Wine60,
    onSecondaryContainer = Color.White,
    background = Color(0xFF14201D),
    onBackground = Color(0xFFE8E3DC),
    surface = Color(0xFF1B2926),
    onSurface = Color(0xFFE8E3DC),
    outlineVariant = Color(0xFF3A4340)
)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(14.dp)
)

val AppTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold,
        fontSize = 23.sp, lineHeight = 29.sp, letterSpacing = (-0.3).sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium,
        fontSize = 17.sp, lineHeight = 23.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium,
        fontSize = 13.sp, lineHeight = 18.sp
    )
)

@Composable
fun MedProtocolsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
