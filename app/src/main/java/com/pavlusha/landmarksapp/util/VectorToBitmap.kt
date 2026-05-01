package com.pavlusha.landmarksapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat

fun vectorToBitmap(context: Context, resId: Int, sizeDp: Int): Bitmap {
    val px = (sizeDp * context.resources.displayMetrics.density).toInt()
    val drawable = ContextCompat.getDrawable(context, resId) ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    val bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}