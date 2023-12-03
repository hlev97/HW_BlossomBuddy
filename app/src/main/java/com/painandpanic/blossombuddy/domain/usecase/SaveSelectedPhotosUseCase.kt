package com.painandpanic.blossombuddy.domain.usecase

import com.painandpanic.blossombuddy.data.local.dao.SelectedImageDao
import com.painandpanic.blossombuddy.data.local.model.SelectedImage

class SaveSelectedPhotosUseCase(
    private val selectedImageDao: SelectedImageDao
) {
    suspend operator fun invoke(selectedImages: List<SelectedImage>) {
        selectedImageDao.saveSelectedImages(selectedImages)
    }
}