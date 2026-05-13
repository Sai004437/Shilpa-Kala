package com.shilpakala.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.shilpakala.app.ui.components.CameraOverlay

@Composable
fun ShilpaKalaApp(navController: NavController) {

    var currentPath by remember { mutableStateOf<String?>(null) }
    var artisanNameState by remember { mutableStateOf("") }

    val path = currentPath

    if (path == null) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            CameraScreen(
                onPhotoReady = { newPath, name ->
                    artisanNameState = name
                    currentPath = newPath
                },
                onGalleryClick = {
                    navController.navigate("gallery")
                },
                onHelpClick = {
                    navController.navigate("assistant")
                }
            )

            CameraOverlay()
        }

    } else {

        PreviewScreen(
            imagePath = path,
            artisanName = artisanNameState,
            onRetake = {
                currentPath = null
            },
            onSaved = {
                currentPath = null
                navController.navigate("gallery")
            },
        )
    }
}