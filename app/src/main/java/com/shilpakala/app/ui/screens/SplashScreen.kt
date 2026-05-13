package com.shilpakala.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shilpakala.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    // Animate the logo scaling in
    val scale = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(MaroonDark, DeepMaroon))
            ),
        contentAlignment = Alignment.Center
    ) {
        // Top gold line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Gold)
                .align(Alignment.TopCenter)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .background(MaroonCard)
                    .border(2.dp, Gold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🪵", fontSize = 56.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kannada title
            Text(
                text = "ಶಿಲ್ಪ-ಕಲಾ",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Gold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Shilpa-Kala",
                fontSize = 20.sp,
                color = White80,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Divider
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(1.dp)
                    .background(Gold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ಕರ್ನಾಟಕದ ಕಲೆ  ·  Art of Karnataka",
                fontSize = 13.sp,
                color = GoldLight,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Digital Portfolio for Artisans",
                fontSize = 12.sp,
                color = White80,
                textAlign = TextAlign.Center
            )
        }

        // Bottom gold line + label
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Handmade in Karnataka 🇮🇳",
                fontSize = 12.sp,
                color = Gold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Gold)
            )
        }
    }
}
