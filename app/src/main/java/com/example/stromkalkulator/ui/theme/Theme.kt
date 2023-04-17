package com.example.stromkalkulator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun StromKalkulatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


@Immutable
data class ExtendedColorsClass(
    val red: Color,
    val onRed: Color,
    val redContainer: Color,
    val onRedContainer: Color,
    val green: Color,
    val onGreen: Color,
    val greenContainer: Color,
    val onGreenContainer: Color,
    val yellow: Color,
    val onYellow: Color,
    val yellowContainer: Color,
    val onYellowContainer: Color,
)


@Composable
fun extendedColors(): ExtendedColorsClass {
        return when(isSystemInDarkTheme()) {
            true -> ExtendedColorsClass(
                red = dark_Red,
                onRed = dark_onRed,
                redContainer = dark_RedContainer,
                onRedContainer = dark_onRedContainer,
                green = dark_Green,
                greenContainer = dark_GreenContainer,
                onGreen = dark_onGreen,
                onGreenContainer = dark_onGreenContainer,
                yellow = dark_Yellow,
                onYellow = dark_onYellow,
                yellowContainer = dark_YellowContainer,
                onYellowContainer = dark_onYellowContainer,
            )
            false -> ExtendedColorsClass(
                red = light_Red,
                onRed = light_onRed,
                redContainer = light_RedContainer,
                onRedContainer = light_onRedContainer,
                green = light_Green,
                greenContainer = light_GreenContainer,
                onGreen = light_onGreen,
                onGreenContainer = light_onGreenContainer,
                yellow = light_Yellow,
                onYellow = light_onYellow,
                yellowContainer = light_YellowContainer,
                onYellowContainer = light_onYellowContainer,
            )
    }
}
