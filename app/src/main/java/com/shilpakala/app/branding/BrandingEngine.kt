package com.shilpakala.app.branding

import android.graphics.*

/**
 * BrandingEngine
 * Takes the raw captured Bitmap and paints:
 *   1. Gold border around the whole image
 *   2. Heritage pill badge top-left: "Handmade in Karnataka"
 *   3. Gradient footer with artisan name, wood type, price
 */
object BrandingEngine {

    fun applyBranding(
        original: Bitmap,
        artisanName: String,
        woodType: String,
        price: String
    ): Bitmap {
        val branded = original.copy(Bitmap.Config.ARGB_8888, true)
        val canvas  = Canvas(branded)
        val W = branded.width.toFloat()
        val H = branded.height.toFloat()

        // ── 1. Gradient footer ───────────────────────────────────────────────
        val footerH   = H * 0.22f
        val footerTop = H - footerH

        val gradPaint = Paint()
        gradPaint.shader = LinearGradient(
            0f, footerTop, 0f, H,
            intArrayOf(Color.TRANSPARENT, Color.parseColor("#CC5C0E0E"), Color.parseColor("#EE1A0A0A")),
            floatArrayOf(0f, 0.4f, 1f),
            Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, footerTop, W, H, gradPaint)

        // ── 2. Heritage badge (top-left pill) ────────────────────────────────
        val pad    = W * 0.03f
        val bH     = H * 0.058f
        val bW     = W * 0.68f
        val badge  = RectF(pad, pad, pad + bW, pad + bH)

        val badgeFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CC8B1A1A")
        }
        canvas.drawRoundRect(badge, bH / 2, bH / 2, badgeFill)

        val badgeBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style       = Paint.Style.STROKE
            color       = Color.parseColor("#D4A017")
            strokeWidth = 3f
        }
        canvas.drawRoundRect(badge, bH / 2, bH / 2, badgeBorder)

        val herPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color     = Color.parseColor("#F0C040")
            textSize  = bH * 0.5f
            typeface  = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            letterSpacing = 0.05f
        }
        canvas.drawText(
            "  Handmade in Karnataka",
            badge.left + bH * 0.3f,
            badge.centerY() + herPaint.textSize * 0.35f,
            herPaint
        )

        // ── 3. Artisan details in footer ─────────────────────────────────────
        val textY   = footerTop + footerH * 0.28f
        val lineGap = footerH * 0.30f

        // Name — large gold serif
        val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color    = Color.parseColor("#F0C040")
            textSize = H * 0.046f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }
        val displayName = artisanName.ifBlank { "Karnataka Artisan" }
        canvas.drawText(displayName, W * 0.05f, textY, namePaint)

        // Wood type — smaller white
        val detailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color    = Color.parseColor("#CCFFFFFF")
            textSize = H * 0.032f
        }
        if (woodType.isNotBlank()) {
            canvas.drawText("Wood: $woodType", W * 0.05f, textY + lineGap, detailPaint)
        }

        // Price — right-aligned bold gold
        val pricePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color     = Color.parseColor("#F0C040")
            textSize  = H * 0.054f
            typeface  = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.RIGHT
        }
        if (price.isNotBlank()) {
            canvas.drawText(price, W * 0.95f, textY + lineGap * 0.6f, pricePaint)
        }

        // ── 4. Gold border around entire image ───────────────────────────────
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style       = Paint.Style.STROKE
            color       = Color.parseColor("#D4A017")
            strokeWidth = 7f
        }
        canvas.drawRect(3.5f, 3.5f, W - 3.5f, H - 3.5f, borderPaint)

        borderPaint.strokeWidth = 2f
        borderPaint.color       = Color.parseColor("#88D4A017")
        canvas.drawRect(16f, 16f, W - 16f, H - 16f, borderPaint)

        return branded
    }
}
