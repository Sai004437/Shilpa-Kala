package com.shilpakala.app.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.shilpakala.app.branding.BrandingEngine
import com.shilpakala.app.gallery.ImageSaver
import com.shilpakala.app.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    onPhotoReady: (String, String) -> Unit,   // passes temp file path AND artisan name to preview
    onGalleryClick: () -> Unit,
    onHelpClick: () -> Unit,
) {
    val context       = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope         = rememberCoroutineScope()
    val haptic        = LocalHapticFeedback.current

    var artisanName by remember { mutableStateOf("") }
    var woodType    by remember { mutableStateOf("") }
    var price       by remember { mutableStateOf("") }
    var isCapturing by remember { mutableStateOf(false) }
    var errorMsg    by remember { mutableStateOf("") }

    // Hold imageCapture reference
    val imageCaptureRef = remember { mutableStateOf<ImageCapture?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // ── Top bar ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaroonDark)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ಶಿಲ್ಪ-ಕಲಾ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Gold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onHelpClick) {
                Icon(Icons.Default.SupportAgent, contentDescription = "Help", tint = Gold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onGalleryClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold),
                border = androidx.compose.foundation.BorderStroke(1.dp, Gold),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("My Gallery", fontSize = 12.sp)
            }
        }

        // ── Camera preview + overlay ────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // CameraX Preview
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val imageCapture = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build()
                        imageCaptureRef.value = imageCapture

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            Log.e("ShilpaKala", "Camera bind failed: ${e.message}")
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Gold frame guide overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawGuideOverlay()
            }

            // Guide text
            Text(
                text = "Place your product inside the frame",
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .background(Color(0x88000000), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // ── Bottom input panel ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DeepMaroon)
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (errorMsg.isNotEmpty()) {
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }

            ShilpaTextField(
                value = artisanName,
                onValueChange = { artisanName = it },
                label = "Artisan Name  (e.g. Raju)"
            )
            ShilpaTextField(
                value = woodType,
                onValueChange = { woodType = it },
                label = "Wood Type  (e.g. Rosewood)"
            )
            ShilpaTextField(
                value = price,
                onValueChange = { price = it },
                label = "Price  (e.g. ₹450)"
            )

            Button(
                onClick = {
                    if (artisanName.isBlank() || woodType.isBlank() || price.isBlank()) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        errorMsg = "Please fill in all fields before capturing."
                        return@Button
                    }
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    errorMsg = ""
                    isCapturing = true
            scope.launch {
                captureAndBrand(
                    context = context,
                    imageCapture = imageCaptureRef.value,
                    artisanName = artisanName.trim(),
                    woodType = woodType.trim(),
                    price = price.trim(),
                    onSuccess = { path ->
                        isCapturing = false
                        onPhotoReady(path, artisanName.trim())
                    },
                    onError = { msg ->
                        isCapturing = false
                        errorMsg = msg
                    }
                )
            }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor   = DeepMaroon
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isCapturing
            ) {
                Text(
                    text = if (isCapturing) "Capturing..." else "📸   Capture Product",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Guide overlay drawing ────────────────────────────────────────────────────
fun DrawScope.drawGuideOverlay() {
    val w = size.width
    val h = size.height

    val frameW = w * 0.78f
    val frameH = h * 0.58f
    val left   = (w - frameW) / 2f
    val top    = (h - frameH) / 2f
    val right  = left + frameW
    val bottom = top + frameH

    // Dark scrim outside the frame
    drawRect(color = Color(0x77000000))

    // Clear the frame area (transparent)
    drawRoundRect(
        color       = Color.Transparent,
        topLeft     = Offset(left, top),
        size        = Size(frameW, frameH),
        cornerRadius = CornerRadius(16f, 16f),
        blendMode   = BlendMode.Clear
    )

    // Gold corner brackets
    val cLen    = 52f
    val stroke  = 6f
    val gold    = Color(0xFFD4A017)

    // Top-left
    drawLine(gold, Offset(left, top + cLen), Offset(left, top), strokeWidth = stroke)
    drawLine(gold, Offset(left, top), Offset(left + cLen, top), strokeWidth = stroke)
    // Top-right
    drawLine(gold, Offset(right - cLen, top), Offset(right, top), strokeWidth = stroke)
    drawLine(gold, Offset(right, top), Offset(right, top + cLen), strokeWidth = stroke)
    // Bottom-left
    drawLine(gold, Offset(left, bottom - cLen), Offset(left, bottom), strokeWidth = stroke)
    drawLine(gold, Offset(left, bottom), Offset(left + cLen, bottom), strokeWidth = stroke)
    // Bottom-right
    drawLine(gold, Offset(right - cLen, bottom), Offset(right, bottom), strokeWidth = stroke)
    drawLine(gold, Offset(right, bottom), Offset(right, bottom - cLen), strokeWidth = stroke)
}

// ── Capture + brand logic (runs on IO thread) ────────────────────────────────
private suspend fun captureAndBrand(
    context: Context,
    imageCapture: ImageCapture?,
    artisanName: String,
    woodType: String,
    price: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    if (imageCapture == null) {
        onError("Camera not ready. Please wait.")
        return
    }

    suspendCancellableCoroutine { cont ->
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    // Convert to bitmap off the main thread then apply branding
                    val bitmap = imageProxyToBitmap(imageProxy)
                    imageProxy.close()

                    if (bitmap == null) {
                        onError("Image conversion failed. Try again.")
                        cont.resume(Unit)
                        return
                    }

                    val branded = BrandingEngine.applyBranding(bitmap, artisanName, woodType, price)
                    val tempFile = ImageSaver.saveTempPreview(context, branded)
                    onSuccess(tempFile.absolutePath)
                    cont.resume(Unit)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError("Capture failed: ${exception.message}")
                    cont.resume(Unit)
                }
            }
        )
    }
}

private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
    return try {
        val buffer: ByteBuffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        var bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null
        val rotation = imageProxy.imageInfo.rotationDegrees
        if (rotation != 0) {
            val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        }
        bmp
    } catch (e: Exception) {
        Log.e("ShilpaKala", "Bitmap error: ${e.message}")
        null
    }
}

// ── Reusable styled text field ───────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShilpaTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label, fontSize = 13.sp) },
        singleLine    = true,
        modifier      = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = Gold,
            unfocusedBorderColor = Gold.copy(alpha = 0.4f),
            focusedLabelColor    = Gold,
            unfocusedLabelColor  = Gold.copy(alpha = 0.6f),
            cursorColor          = Gold,
            focusedTextColor     = Color.White,
            unfocusedTextColor   = Color.White
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        shape = RoundedCornerShape(10.dp)
    )
}
