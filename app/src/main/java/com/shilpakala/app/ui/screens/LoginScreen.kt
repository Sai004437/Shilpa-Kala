package com.shilpakala.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shilpakala.app.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onCreateAccountClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error    by remember { mutableStateOf("") }
    val haptic   = LocalHapticFeedback.current

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
        // Logo / Title Section
        Text(
            text = "ಶಿಲ್ಪ-ಕಲಾ",
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Gold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Artisan Portal",
            fontSize = 18.sp,
            color = GoldLight,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Error message
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        // Username/Email Field
        ShilpaTextField(
            value = username,
            onValueChange = { username = it; error = "" },
            label = "Email Address",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Gold.copy(alpha = 0.6f)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        ShilpaTextField(
            value = password,
            onValueChange = { password = it; error = "" },
            label = "Password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Gold.copy(alpha = 0.6f)) },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                when {
                    !username.matches(emailRegex) -> {
                        error = "Please enter a valid email address"
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    !password.matches(passwordRegex) -> {
                        error = "Password must be 8+ chars with 1 Uppercase and 1 Number"
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    else -> {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLoginSuccess()
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
                text = "Sign In",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Optional: Forgot Password / Sign Up
        TextButton(onClick = onCreateAccountClick) {
            Text(
                text = "Don't have an account? Create one",
                color = White80,
                fontSize = 14.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShilpaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 14.sp) },
        leadingIcon = leadingIcon,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Gold,
            unfocusedBorderColor = Gold.copy(alpha = 0.3f),
            focusedLabelColor = Gold,
            unfocusedLabelColor = Gold.copy(alpha = 0.6f),
            cursorColor = Gold,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    )
}
