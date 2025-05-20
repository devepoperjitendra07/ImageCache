package com.jitendra.kumar.imagecache.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jitendra.kumar.imagecache.model.ImageData
import com.jitendra.kumar.imagecache.repository.ImageRepository
import com.jitendra.kumar.imagecache.utils.Transformations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ImageRepository(application)
    private val _imageListState = MutableStateFlow<List<ImageData>>(emptyList())
    val imageListState: StateFlow<List<ImageData>> = _imageListState.asStateFlow()

    private val initialUrls = listOf(
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYE396tYNAixUUjOEC_mSjcAcjoJ-QEQzNwg&s"

    )

    init {
        loadImages(initialUrls)
    }

    fun loadImages(urls: List<String>) {
        _imageListState.value = urls.map { ImageData(url = it, isLoading = true) }
        viewModelScope.launch {
            val results = flow {
                urls.forEach { url ->
                    emit(
                        repository.loadImage(
                            url = url,
                            transformation = Transformations.Type.RoundedCorners(16f),
                            cacheKey = url
                        )
                    )
                }
            }.toList()

            val newList = results.mapIndexed { index, result ->
                result.getOrNull() ?: ImageData(
                    url = urls[index],
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
            _imageListState.value = newList
        }
    }

    fun clearCaches(url: String? = null) {
        viewModelScope.launch {
            repository.clearCaches(url)
            if (url == null) _imageListState.value = emptyList()
        }
    }
}