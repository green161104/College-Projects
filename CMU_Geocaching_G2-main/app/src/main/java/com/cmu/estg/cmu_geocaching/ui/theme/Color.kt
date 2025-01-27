package com.cmu.estg.cmu_geocaching.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme

val LightPrimary = Color(0xFFA8E1C2)
val LightSecondary = Color(0xFFCBB7E9)
val LightTertiary = Color(0xFFF7C7D5)
val LightBackground = Color(0xFFF5F5F5)
val LightSurface = Color(0xFFA986B1)
val LightOnPrimary = Color(0xFF1B1F23)
val LightOnSecondary = Color(0xFF1B1F23)
val LightOnTertiary = Color(0xFF1B1F23)
val LightOnBackground = Color(0xFF000000)
val LightOnSurface = Color(0xFFFFFFFF)

val DarkPrimary = Color(0xFF67B490)
val DarkSecondary = Color(0xFF8A72B6)
val DarkTertiary = Color(0xFFB86B84)
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFFE4D6EE)
val DarkOnPrimary = Color(0xFFFFFFFF)
val DarkOnSecondary = Color(0xFFFFFFFF)
val DarkOnTertiary = Color(0xFFFFFFFF)
val DarkOnBackground = Color(0xFFFFFFFF)
val DarkOnSurface = Color(0xFF000000)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnSecondary,
    onTertiary = LightOnTertiary,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    error = Color.Red
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onTertiary = DarkOnTertiary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    error = Color.Red
)