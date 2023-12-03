package com.painandpanic.blossombuddy.domain.usecase

import com.painandpanic.blossombuddy.data.local.dao.SelectedImageDao
import com.painandpanic.blossombuddy.ui.model.SelectedImageUi
import kotlinx.coroutines.flow.first

class LoadSelectedPhotosUseCase(
    private val loadImageFromSelectedGallery: LoadImageFromSelectedGalleryUseCase,
    private val selectedPhotosDao: SelectedImageDao
) {
    suspend operator fun invoke() = selectedPhotosDao.getSelectedImages().first().map {
        SelectedImageUi(
            imageId = it.imageId,
            image = loadImageFromSelectedGallery(it.imageId)
        )
    }
}