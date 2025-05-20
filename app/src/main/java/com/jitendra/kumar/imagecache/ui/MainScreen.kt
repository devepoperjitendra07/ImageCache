package com.jitendra.kumar.imagecache.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jitendra.kumar.imagecache.R
import com.jitendra.kumar.imagecache.model.ImageData
import com.jitendra.kumar.imagecache.viewmodel.ImageViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(viewModel: ImageViewModel = viewModel()) {
    val imageListState = viewModel.imageListState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.clearCaches() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Cache")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(imageListState) { _, imageData ->
                ImageItem(imageData)
            }
        }
    }
}


@Composable
fun ImageItem(imageData: ImageData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        CustomImageView(
            imageData = imageData,
            blurRadius = 10f,
            placeholderColor = MaterialTheme.colorScheme.surfaceVariant,
            placeholderDrawable = R.drawable.placeholder,
            showPlaceholderIcon = true
        )
    }
}