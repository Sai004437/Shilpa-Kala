package com.shilpakala.app.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.shilpakala.app.gallery.ImageSaver
import com.shilpakala.app.ui.theme.*
import java.io.File

@Composable
fun GalleryScreen(onBack: () -> Unit, onHelpClick: () -> Unit) {
    val context = LocalContext.current
    val haptic  = LocalHapticFeedback.current
    var photos  by remember { mutableStateOf<List<File>>(emptyList()) }
    var photoToDelete by remember { mutableStateOf<File?>(null) }

    // Load photos whenever screen is shown
    LaunchedEffect(Unit) {
        photos = ImageSaver.loadAllPhotos(context)
    }

    if (photoToDelete != null) {
        AlertDialog(
            onDismissRequest = { photoToDelete = null },
            title = { Text("Delete Photo?", color = Gold) },
            text = { Text("Are you sure you want to remove this photo from your gallery?", color = White80) },
            confirmButton = {
                TextButton(onClick = {
                    photoToDelete?.delete()
                    photos = ImageSaver.loadAllPhotos(context)
                    photoToDelete = null
                }) {
                    Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { photoToDelete = null }) {
                    Text("Cancel", color = Gold)
                }
            },
            containerColor = MaroonCard
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMaroon)
    ) {
        // ── Top bar ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaroonDark)
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Gold)
            }
            Text(
                text = "My Gallery",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onHelpClick) {
                Icon(Icons.Default.SupportAgent, contentDescription = "Help", tint = Gold)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text("${photos.size} photos", color = White80, fontSize = 13.sp)
        }

        // ── Content ─────────────────────────────────────────────────────────
        if (photos.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No photos yet.\nCapture your first product!",
                        color = TextHint,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns      = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 32.dp, start = 8.dp, end = 8.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                items(photos) { photo ->
                    GalleryItem(
                        file = photo, 
                        onShare = { sharePhoto(context, it) },
                        onDelete = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            photoToDelete = it 
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GalleryItem(file: File, onShare: (File) -> Unit, onDelete: (File) -> Unit) {
    Card(
        shape  = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaroonCard)
    ) {
        Column {
            Box {
                AsyncImage(
                    model          = file,
                    contentDescription = "Product photo",
                    contentScale   = ContentScale.Crop,
                    modifier       = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
                
                // Delete button overlay
                IconButton(
                    onClick = { onDelete(file) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        Icons.Default.Delete, 
                        contentDescription = "Delete", 
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Button(
                onClick  = { onShare(file) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(36.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor   = DeepMaroon
                ),
                shape            = RoundedCornerShape(8.dp),
                contentPadding   = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun sharePhoto(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "com.shilpakala.app.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, "✨ Handmade in Karnataka\nShilpa-Kala 🎨")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}
