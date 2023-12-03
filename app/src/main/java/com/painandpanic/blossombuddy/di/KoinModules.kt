package com.painandpanic.blossombuddy.di

import androidx.room.Room
import com.painandpanic.blossombuddy.data.local.database.HistoryDatabase
import com.painandpanic.blossombuddy.domain.usecase.ClassifyImageUseCase
import com.painandpanic.blossombuddy.domain.usecase.LoadHistoryUseCase
import com.painandpanic.blossombuddy.domain.usecase.LoadImageFromHistoryGalleryUseCase
import com.painandpanic.blossombuddy.domain.usecase.LoadImageFromSelectedGalleryUseCase
import com.painandpanic.blossombuddy.domain.usecase.LoadSelectedPhotosUseCase
import com.painandpanic.blossombuddy.domain.usecase.SavePhotoToGalleryUseCase
import com.painandpanic.blossombuddy.domain.usecase.SaveSelectedPhotosUseCase
import com.painandpanic.blossombuddy.domain.usecase.SaveToHistoryUseCase
import com.painandpanic.blossombuddy.domain.usecase.TakePhotoUseCase
import com.painandpanic.blossombuddy.ui.camera.CameraViewModel
import com.painandpanic.blossombuddy.ui.classificationresult.ClassificationResultViewModel
import com.painandpanic.blossombuddy.ui.history.HistoryItemViewModel
import com.painandpanic.blossombuddy.ui.home.HomeViewModel
import com.painandpanic.blossombuddy.ui.photopicker.PhotoPickerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.pytorch.Device
import org.pytorch.LiteModuleLoader

val useCaseModule = module {
    single { TakePhotoUseCase(androidContext()) }
    single { SavePhotoToGalleryUseCase(androidContext()) }
    single { ClassifyImageUseCase(androidContext(),get()) }
    single { LoadHistoryUseCase(get(),get()) }
    single { SaveToHistoryUseCase(get()) }
    single { LoadImageFromHistoryGalleryUseCase(androidContext(), get()) }
    single { LoadSelectedPhotosUseCase(get(),get())  }
    single { SaveSelectedPhotosUseCase(get()) }
    single { LoadImageFromSelectedGalleryUseCase(get(),get())  }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(),get()) }
    viewModel { CameraViewModel(get(),get()) }
    viewModel { ClassificationResultViewModel(get(),get(),get()) }
    viewModel { HistoryItemViewModel(get(),get()) }
    viewModel { PhotoPickerViewModel(get()) }
}

val roomModule = module {
    single { Room.databaseBuilder(androidContext(), HistoryDatabase::class.java, "history_db").build() }
    single { get<HistoryDatabase>().historyDao() }
    single { get<HistoryDatabase>().selectedImageDao() }
    single { get<HistoryDatabase>().selectedImageDao() }
}

val pytorchModelModule = module {
    single {
        LiteModuleLoader.loadModuleFromAsset(
            androidContext().assets,
            ClassifyImageUseCase.ASSET_NAME,
            Device.VULKAN
        )
    }
}