package com.example.kaijugrupomen.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = KaijuMorado,
    onPrimary = KaijuBlanco,
    primaryContainer = KaijuRosado,
    onPrimaryContainer = KaijuBlanco,
    secondary = KaijuRosado,
    tertiary = KaijuAmarillo,
    background = KaijuGrisFondo,
    surface = KaijuBlanco,
    surfaceContainer = KaijuBlanco,
    surfaceContainerHighest = KaijuBlanco,
    error = KaijuRojo
)

private val DarkColors = darkColorScheme(
    primary = KaijuMoradoLogo,
    onPrimary = KaijuBlanco,
    primaryContainer = KaijuRosado,
    onPrimaryContainer = KaijuBlanco,
    secondary = KaijuRosado,
    tertiary = KaijuAmarillo,
    background = KaijuNegro,
    surface = KaijuGrisOscuro,
    surfaceContainer = KaijuGrisOscuro,
    surfaceContainerHighest = KaijuGrisOscuro,
    error = KaijuRojoClaro
)

@Composable
fun KaijugrupomenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
