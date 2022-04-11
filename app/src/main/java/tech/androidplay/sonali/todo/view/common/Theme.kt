package tech.androidplay.sonali.todo.view.common

import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.runtime.*

private val DarkColorPalette = darkColors(
    primary = AppBlue,
    primaryVariant = Black100,
    surface = Black100,
    secondary = White100
)

private val LightColorPalette = lightColors(
    primary = AppBlue,
    primaryVariant = White100,
    surface = White100,
    secondary = GreyDark
)

@Composable
fun AheadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette
    else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content,
        shapes = Shapes
    )
}