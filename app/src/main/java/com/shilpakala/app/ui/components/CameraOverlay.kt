package com.shilpakala.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun CameraOverlay() {

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        drawRoundRect(
            color = Color.White,
            topLeft = Offset(120f, 350f),
            size = Size(850f, 900f),
            style = Stroke(width = 8f)
        )
    }
}