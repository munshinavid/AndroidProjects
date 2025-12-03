package com.example.ezy.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun EzyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        content = content
    )
}