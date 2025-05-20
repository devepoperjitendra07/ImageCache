package com.jitendra.kumar.imagecache.ui

import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jitendra.kumar.imagecache.R
import com.jitendra.kumar.imagecache.model.ImageData


@Composable
fun CustomImageView(
    imageData: ImageData,
    modifier: Modifier = Modifier,
    blurRadius: Float = 0f,
    placeholderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    placeholderDrawable: Int? = R.drawable.placeholder,
    showPlaceholderIcon: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .background(placeholderColor),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = imageData, animationSpec = tween(300)) { data ->
            when {
                data.isLoading -> {
                    if (placeholderDrawable != null) {
                        Image(
                            painter = painterResource(placeholderDrawable),
                            contentDescription = "Placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (showPlaceholderIcon) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    }
                }
                data.error != null -> {
                    if (placeholderDrawable != null) {
                        Image(
                            painter = painterResource(placeholderDrawable),
                            contentDescription = "Error",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (showPlaceholderIcon) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                data.bitmap != null -> {
                    Image(
                        bitmap = data.bitmap.asImageBitmap(),
                        contentDescription = "Image from ${data.url}",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && blurRadius > 0f) {
                                    renderEffect = android.graphics.RenderEffect.createBlurEffect(
                                        blurRadius,
                                        blurRadius,
                                        android.graphics.Shader.TileMode.CLAMP
                                    ).asComposeRenderEffect()
                                }
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}