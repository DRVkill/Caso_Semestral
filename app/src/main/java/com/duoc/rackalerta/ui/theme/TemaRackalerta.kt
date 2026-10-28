package com.duoc.rackalerta.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AzulCielo,
    onPrimary = AzulMarino,
    secondary = AzulProfundo,
    background = AzulMarino
)

private val LightColorScheme = lightColorScheme(
    primary = AzulProfundo,
    onPrimary = Color.White,
    secondary = AzulMarino,
    background = AzulSuave,
    surface = Color.White
)

@Composable
fun TemaRackalerta(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
