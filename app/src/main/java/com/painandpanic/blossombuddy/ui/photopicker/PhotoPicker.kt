package com.painandpanic.blossombuddy.ui.photopicker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun PhotoPicker(
    onBack: () -> Unit,
    onPhotoPicked: (Long) -> Unit,
    state: PhotoPickerViewState
) {
    val interactionSources = List(state.selectedPhotos.size) { remember { MutableInteractionSource() } }

    val indication = LocalIndication.current

    Scaffold(
        topBar = {
            Row {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                userScrollEnabled = true
            ) {
                items(state.selectedPhotos) { selectedImage ->
                    Card(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.large)
                            .clickable(
                                interactionSource = interactionSources[state.selectedPhotos.indexOf(
                                    selectedImage
                                )],
                                indication = indication,
                                onClick = { onPhotoPicked(selectedImage.imageId) }
                            ),
                        elevation = CardDefaults.elevatedCardElevation(),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        val isPressed by interactionSources[state.selectedPhotos.indexOf(
                            selectedImage
                        )].collectIsPressedAsState()
                        val filterAlpha by animateFloatAsState(
                            targetValue = if (!isPressed) 0f else 0.8f,
                            label = ""
                        )

                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = selectedImage.image.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .clip(MaterialTheme.shapes.large)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = filterAlpha))
                                    .clip(MaterialTheme.shapes.large)
                            )
                        }
                    }
                }
            }
        }
    }
}