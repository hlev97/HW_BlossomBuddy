package com.painandpanic.blossombuddy.ui.photopicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.painandpanic.blossombuddy.domain.usecase.LoadSelectedPhotosUseCase
import com.painandpanic.blossombuddy.ui.model.SelectedImageUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoPickerViewModel(
    private val loadSelectedPhotos: LoadSelectedPhotosUseCase
) : ViewModel() {

    private val _uiStateStream = MutableStateFlow(PhotoPickerViewState())
    val uiStateStream = _uiStateStream.asStateFlow()

    private var uiState: PhotoPickerViewState
        get() = _uiStateStream.value
        set(newState) {
            _uiStateStream.update { newState }
        }

    init {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            uiState = uiState.copy(
                isLoading = false,
                selectedPhotos = loadSelectedPhotos()
            )
        }
    }
}

data class PhotoPickerViewState(
    val isLoading: Boolean = false,
    val selectedPhotos: List<SelectedImageUi> = emptyList(),
) {
    val isReady: Boolean
        get() = !isLoading && selectedPhotos.isNotEmpty()
}