package com.shilpakala.app.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.shilpakala.app.gallery.ImageSaver
import com.shilpakala.app.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun PreviewScreen(
    imagePath: String,
    artisanName: String,
    onRetake: () -> Unit,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    var saving  by remember { mutableStateOf(false) }
    var saved   by remember { mutableStateOf(false) }

    // Load bitmap
    val bitmap = remember(imagePath) {
        BitmapFactory.decodeFile(imagePath)?.asImageBitmap()
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
            IconButton(onClick = onRetake) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Gold)
            }
            Text(
                text = "Preview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gold,
                modifier = Modifier.weight(1f)
            )
            Text("ಶಿಲ್ಪ-ಕಲಾ", color = White80, fontSize = 13.sp)
        }

        // ── Branded image ───────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap      = bitmap,
                    contentDescription = "Branded product photo",
                    contentScale = ContentScale.Fit,
                    modifier    = Modifier.fillMaxSize()
                )
            } else {
                Text("Loading image...", color = White80)
            }
        }

        // ── Success message ─────────────────────────────────────────────────
        if (saved) {
            Text(
                text     = "✅ Photo saved to ShilpaKala folder!",
                color    = GoldLight,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }

        // ── Buttons ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DeepMaroon)
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Retake
            OutlinedButton(
                onClick = onRetake,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold),
                border = androidx.compose.foundation.BorderStroke(1.dp, Gold),
                shape  = RoundedCornerShape(12.dp)
            ) {
                Text("↩  Retake", fontWeight = FontWeight.Bold)
            }

            // Save & Share
            Button(
                onClick = {
                    if (!saving) {
                        saving = true
                        scope.launch {
                            val file = saveAndShare(context, imagePath, artisanName)
                            saving = false
                            saved  = true
                            if (file != null) sharePhoto(context, file, artisanName)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor   = DeepMaroon
                ),
                shape   = RoundedCornerShape(12.dp),
                enabled = !saving
            ) {
                Text(
                    text = if (saving) "Saving..." else "💾  Save & Share",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private suspend fun saveAndShare(
    context: Context,
    imagePath: String,
    artisanName: String
): File? = withContext(Dispatchers.IO) {
    val bitmap = BitmapFactory.decodeFile(imagePath) ?: return@withContext null
    ImageSaver.saveBrandedPhoto(context, bitmap, artisanName)
}

private fun sharePhoto(context: Context, file: File, artisanName: String) {
    val uri = FileProvider.getUriForFile(context, "com.shilpakala.app.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(
            Intent.EXTRA_TEXT,
            "✨ Handmade in Karnataka by $artisanName\nBrought to you by Shilpa-Kala 🎨"
        )
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share your product via"))
}
