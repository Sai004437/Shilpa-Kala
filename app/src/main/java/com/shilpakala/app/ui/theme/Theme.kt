package com.shilpakala.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ShilpaKalaColors = darkColorScheme(
    primary          = Gold,
    onPrimary        = DeepMaroon,
    secondary        = GoldLight,
    onSecondary      = DeepMaroon,
    background       = DeepMaroon,
    onBackground     = White,
    surface          = MaroonCard,
    onSurface        = White,
    surfaceVariant   = MaroonDark,
    onSurfaceVariant = White80,
    outline          = Gold,
    error            = androidx.compose.ui.graphics.Color(0xFFCF6679),
)

@Composable
fun ShilpaKalaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
