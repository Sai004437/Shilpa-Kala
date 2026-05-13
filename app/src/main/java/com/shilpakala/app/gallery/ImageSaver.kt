package com.shilpakala.app.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageSaver {

    private const val FOLDER = "ShilpaKala"

    /** Save branded bitmap permanently. Returns the saved File. */
    fun saveBrandedPhoto(context: Context, bitmap: Bitmap, artisanName: String): File {
        val dir = File(context.getExternalFilesDir(null), FOLDER)
        if (!dir.exists()) dir.mkdirs()

        val timestamp  = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val safeName   = artisanName.replace(Regex("[^a-zA-Z0-9]"), "").take(20)
        val fileName   = "ShilpaKala_${safeName}_$timestamp.jpg"
        val outFile    = File(dir, fileName)

        FileOutputStream(outFile).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos)
        }
        return outFile
    }

    /** Save to cache for temp preview (overwritten on next capture). */
    fun saveTempPreview(context: Context, bitmap: Bitmap): File {
        val tempFile = File(context.cacheDir, "temp_preview.jpg")
        FileOutputStream(tempFile).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        }
        return tempFile
    }

    /** Load all saved photos, newest first. */
    fun loadAllPhotos(context: Context): List<File> {
        val dir = File(context.getExternalFilesDir(null), FOLDER)
        if (!dir.exists()) return emptyList()
        return dir.listFiles { f -> f.name.endsWith(".jpg") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    /** Load bitmap from file path. */
    fun loadBitmap(path: String): Bitmap? = BitmapFactory.decodeFile(path)
}
