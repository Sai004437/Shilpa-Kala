package com.shilpakala.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shilpakala.app.ui.screens.*
import com.shilpakala.app.ui.theme.*
import java.net.URLDecoder
import java.net.URLEncoder
import android.app.Activity
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

import com.shilpakala.app.ui.screens.LoginScreen
import com.shilpakala.app.ui.screens.RegisterScreen
import com.shilpakala.app.ui.screens.AssistantScreen

class MainActivity : ComponentActivity() {

    // Track whether camera permission has been granted
    private var cameraPermissionGranted by mutableStateOf(false)

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraPermissionGranted = granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check permission status at start
        cameraPermissionGranted = checkSelfPermission(Manifest.permission.CAMERA) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED

        setContent {
            ShilpaKalaTheme {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        window.statusBarColor = DeepMaroon.toArgb()
                        window.navigationBarColor = DeepMaroon.toArgb()
                        WindowCompat.setDecorFitsSystemWindows(window, false)
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DeepMaroon
                ) {
                    if (!cameraPermissionGranted) {

                        PermissionScreen {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }

                    } else {

                        ShilpaKalaApp()

                    }
                }
            }
        }
    }
}

@Composable
fun ShilpaKalaApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        // Splash screen
        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        // Login screen
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.CAMERA) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // Register screen
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.CAMERA) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Camera screen
        composable(Routes.CAMERA) {
            CameraScreen(
                onPhotoReady = { imagePath, artisanName ->
                    // URL-encode the path and name so they're safe to pass as nav arguments
                    val encodedPath = URLEncoder.encode(imagePath, "UTF-8")
                    val encodedName = URLEncoder.encode(artisanName, "UTF-8")
                    navController.navigate("${Routes.PREVIEW}/$encodedPath/$encodedName")
                },
                onGalleryClick = {
                    navController.navigate(Routes.GALLERY)
                },
                onHelpClick = {
                    navController.navigate(Routes.ASSISTANT)
                }
            )
        }

        // Preview screen
        composable(
            route = "${Routes.PREVIEW}/{imagePath}/{artisanName}",
            arguments = listOf(
                navArgument("imagePath") { type = NavType.StringType },
                navArgument("artisanName") { type = NavType.StringType }
            )
        ) { backStack ->
            val encodedPath = backStack.arguments?.getString("imagePath") ?: ""
            val imagePath = URLDecoder.decode(encodedPath, "UTF-8")

            val encodedName = backStack.arguments?.getString("artisanName") ?: ""
            val artisanName = URLDecoder.decode(encodedName, "UTF-8")

            PreviewScreen(
                imagePath   = imagePath,
                artisanName = artisanName,
                onRetake    = { navController.popBackStack() },
                onSaved     = { }
            )
        }

        // Gallery screen
        composable(Routes.GALLERY) {
            GalleryScreen(
                onBack = { navController.popBackStack() },
                onHelpClick = { navController.navigate(Routes.ASSISTANT) }
            )
        }

        // Assistant screen
        composable(Routes.ASSISTANT) {
            AssistantScreen(onBack = { navController.popBackStack() })
        }
    }
}

// ── Permission denied screen ─────────────────────────────────────────────────
@Composable
fun PermissionScreen(onRequest: () -> Unit) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMaroon)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Decorative icon with a gold glow effect
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Gold.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("📸", fontSize = 56.sp)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Camera Access Required",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Gold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "To help you create beautiful branded photos of your artwork, Shilpa-Kala needs permission to use your camera.",
            fontSize = 15.sp,
            color = White80,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onRequest()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold,
                contentColor   = DeepMaroon
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                "Grant Permission",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Your privacy is important. Photos are only saved to your device.",
            fontSize = 12.sp,
            color = White80.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}
