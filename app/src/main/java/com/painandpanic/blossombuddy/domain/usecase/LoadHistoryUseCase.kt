package com.painandpanic.blossombuddy.domain.usecase

import com.painandpanic.blossombuddy.data.local.converters.LocalDateTimeConverter
import com.painandpanic.blossombuddy.data.local.dao.HistoryDao
import com.painandpanic.blossombuddy.domain.model.toDomain
import com.painandpanic.blossombuddy.ui.model.HistoryUi
import kotlinx.coroutines.flow.first

class LoadHistoryUseCase(
    private val loadImageFromHistoryGalleryUseCase: LoadImageFromHistoryGalleryUseCase,
    private val dao: HistoryDao
) {
    suspend operator fun invoke(): List<HistoryUi> =
        dao.getHistoryItems().first().map { it.toDomain() }.map {
            HistoryUi(
                image = loadImageFromHistoryGalleryUseCase(it.id),
                imageID = it.imageId,
                labels = it.predictedLabels,
                timestamp = LocalDateTimeConverter().fromLocalDateTime(it.timestamp)
            )
        }

    suspend operator fun invoke(id: Long): HistoryUi =
        dao.getHistoryItem(id).first().toDomain().let {
            HistoryUi(
                image = loadImageFromHistoryGalleryUseCase(it.id),
                imageID = it.imageId,
                labels = it.predictedLabels,
                timestamp = LocalDateTimeConverter().fromLocalDateTime(it.timestamp)
            )
        }
}