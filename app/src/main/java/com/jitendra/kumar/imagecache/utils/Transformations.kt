package com.jitendra.kumar.imagecache.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF

object Transformations {
    sealed class Type {
        object None : Type()
        object CircleCrop : Type()
        data class RoundedCorners(val radius: Float) : Type()
    }

    fun applyTransformation(bitmap: Bitmap, type: Type): Bitmap {
        return when (type) {
            is Type.None -> bitmap
            is Type.CircleCrop -> circleCrop(bitmap)
            is Type.RoundedCorners -> roundedCorners(bitmap, type.radius)
        }
    }

    private fun circleCrop(source: Bitmap): Bitmap {
        val size = minOf(source.width, source.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply { isAntiAlias = true }
        val path = Path().apply { addCircle(size / 2f, size / 2f, size / 2f, Path.Direction.CW) }
        canvas.clipPath(path)
        canvas.drawBitmap(source, (size - source.width) / 2f, (size - source.height) / 2f, paint)
        if (!source.isRecycled && source != output) source.recycle()
        return output
    }

    private fun roundedCorners(source: Bitmap, radius: Float): Bitmap {
        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply { isAntiAlias = true }
        val rect = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        val path = Path().apply { addRoundRect(rect, radius, radius, Path.Direction.CW) }
        canvas.clipPath(path)
        canvas.drawBitmap(source, 0f, 0f, paint)
        if (!source.isRecycled && source != output) source.recycle()
        return output
    }
}