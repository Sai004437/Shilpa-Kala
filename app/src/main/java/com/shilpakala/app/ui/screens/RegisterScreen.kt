package com.shilpakala.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shilpakala.app.ui.theme.*

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val haptic = LocalHapticFeedback.current

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}\$".toRegex()
    val passwordRegex = "^(?=.*[A-Z])(?=.*[0-9]).{8,}\$".toRegex()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMaroon)
            .padding(24.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Gold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Join the Shilpa-Kala Community",
            fontSize = 16.sp,
            color = GoldLight,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Validation Criteria Hints
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            CriteriaHint(text = "• Name: At least 3 characters", isValid = fullName.length >= 3)
            CriteriaHint(text = "• Email: Valid email address", isValid = email.matches(emailRegex))
            CriteriaHint(text = "• Password: 8+ chars, 1 Uppercase, 1 Number", isValid = password.matches(passwordRegex))
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
        }

        ShilpaTextField(
            value = fullName,
            onValueChange = { fullName = it; error = "" },
            label = "Full Name",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Gold.copy(alpha = 0.6f)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ShilpaTextField(
            value = email,
            onValueChange = { email = it; error = "" },
            label = "Email Address",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Gold.copy(alpha = 0.6f)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ShilpaTextField(
            value = password,
            onValueChange = { password = it; error = "" },
            label = "Password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Gold.copy(alpha = 0.6f)) },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                when {
                    fullName.length < 3 -> {
                        error = "Name must be at least 3 characters"
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    !email.matches(emailRegex) -> {
                        error = "Please enter a valid email address"
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    !password.matches(passwordRegex) -> {
                        error = "Password must be 8+ chars with 1 Uppercase and 1 Number"
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    else -> {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onRegisterSuccess()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold,
                contentColor = DeepMaroon
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onBackToLogin) {
            Text(
                text = "Already have an account? Sign In",
                color = White80,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CriteriaHint(text: String, isValid: Boolean) {
    Text(
        text = text,
        color = if (isValid) Color(0xFF4CAF50) else White80.copy(alpha = 0.6f),
        fontSize = 12.sp,
        modifier = Modifier.padding(vertical = 1.dp)
    )
}
