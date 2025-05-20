package com.jitendra.kumar.imagecache.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import com.jitendra.kumar.imagecache.model.ImageData
import com.jitendra.kumar.imagecache.utils.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class ImageRepository(private val context: Context) {
    private val memoryCache = object : LruCache<String, Bitmap>(4 * 1024 * 1024) {
        override fun sizeOf(key: String, value: Bitmap): Int = value.byteCount
        override fun entryRemoved(evicted: Boolean, key: String, oldValue: Bitmap, newValue: Bitmap?) {
            if (evicted && !oldValue.isRecycled) oldValue.recycle()
        }
    }

    private val cacheVersion = "v1_"

    suspend fun loadImage(
        url: String,
        transformation: Transformations.Type = Transformations.Type.None,
        cacheKey: String = url
    ): Result<ImageData> = withContext(Dispatchers.IO) {
        try {
            val fullCacheKey = cacheVersion + cacheKey
            synchronized(memoryCache) {
                memoryCache.get(fullCacheKey)?.let { bitmap ->
                    return@withContext Result.success(ImageData(url, bitmap, isGif = url.endsWith(".gif", ignoreCase = true)))
                }
            }

            val diskCacheFile = getDiskCacheFile(fullCacheKey)
            if (diskCacheFile.exists() && !isCacheExpired(diskCacheFile)) {
                val bitmap = decodeBitmapFromFile(diskCacheFile)
                if (bitmap != null) {
                    val transformed = Transformations.applyTransformation(bitmap, transformation)
                    synchronized(memoryCache) { memoryCache.put(fullCacheKey, transformed) }
                    return@withContext Result.success(ImageData(url, transformed, isGif = url.endsWith(".gif", ignoreCase = true)))
                }
            }

            val bitmap = downloadImageWithRetry(url)
            if (bitmap != null) {
                val isGif = url.endsWith(".gif", ignoreCase = true)
                val transformed = Transformations.applyTransformation(bitmap, transformation)
                synchronized(memoryCache) { memoryCache.put(fullCacheKey, transformed) }
                saveToDiskCache(fullCacheKey, transformed)
                Result.success(ImageData(url, transformed, isGif = isGif))
            } else {
                Result.failure(Exception("Failed to download image"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun downloadImageWithRetry(url: String): Bitmap? {
        repeat(3) { attempt ->
            try {
                var connection: HttpURLConnection? = null
                try {
                    connection = URL(url).openConnection() as HttpURLConnection
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000
                    connection.connect()
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val options = BitmapFactory.Options().apply {
                            inJustDecodeBounds = true
                            BitmapFactory.decodeStream(connection!!.inputStream, null, this)
                            inSampleSize = calculateInSampleSize(this, 300, 300)
                            inJustDecodeBounds = false
                            inPreferredConfig = Bitmap.Config.RGB_565
                        }
                        connection = URL(url).openConnection() as HttpURLConnection
                        connection.connect()
                        return BitmapFactory.decodeStream(connection.inputStream, null, options)
                    }
                } finally {
                    connection?.disconnect()
                }
            } catch (e: Exception) {
                if (attempt < 2) delay((attempt + 1) * 1000L)
            }
        }
        return null
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun decodeBitmapFromFile(file: File): Bitmap? {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(file.absolutePath, this)
                inSampleSize = calculateInSampleSize(this, 300, 300)
                inJustDecodeBounds = false
                inPreferredConfig = Bitmap.Config.RGB_565
            }
            BitmapFactory.decodeFile(file.absolutePath, options)
        } catch (e: Exception) {
            null
        }
    }

    private fun saveToDiskCache(cacheKey: String, bitmap: Bitmap) {
        val file = getDiskCacheFile(cacheKey)
        try {
            FileOutputStream(file).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)
            }
        } catch (e: Exception) {
            file.delete()
        }
    }

    private fun getDiskCacheFile(cacheKey: String): File {
        val fileName = cacheKey.hashCode().toString() + ".jpg"
        return File(context.cacheDir, fileName)
    }

    private fun isCacheExpired(file: File): Boolean {
        val ttl = TimeUnit.DAYS.toMillis(7)
        return System.currentTimeMillis() - file.lastModified() > ttl
    }

    suspend fun clearCaches(url: String? = null) = withContext(Dispatchers.IO) {
        synchronized(memoryCache) {
            if (url != null) {
                memoryCache.remove(cacheVersion + url)
                getDiskCacheFile(cacheVersion + url).delete()
            } else {
                memoryCache.evictAll()
                context.cacheDir.listFiles()?.forEach { it.delete() }
            }
        }
    }
}