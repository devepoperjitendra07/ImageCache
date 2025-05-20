package com.jitendra.kumar.imagecache.model
import android.graphics.Bitmap

data class ImageData(
    val url: String,
    val bitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGif: Boolean = false
)