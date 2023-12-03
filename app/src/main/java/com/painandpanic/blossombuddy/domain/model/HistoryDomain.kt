package com.painandpanic.blossombuddy.domain.model

import com.painandpanic.blossombuddy.data.local.model.HistoryEntity
import java.time.LocalDateTime

data class HistoryDomain(
    val id: Long = 0,
    val imageId: Long,
    val predictedLabels: Map<String,Float>,
    val timestamp: LocalDateTime,
)

fun HistoryDomain.toEntity() = HistoryEntity(
    id = id,
    predictedLabels = predictedLabels,
    imageId = imageId,
    timestamp = timestamp,
)

fun HistoryEntity.toDomain() = HistoryDomain(
    id = id,
    imageId = imageId,
    predictedLabels = predictedLabels,
    timestamp = timestamp,
)