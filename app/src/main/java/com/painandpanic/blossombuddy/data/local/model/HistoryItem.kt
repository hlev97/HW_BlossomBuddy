package com.painandpanic.blossombuddy.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "image_uri") val imageId: Long,
    @ColumnInfo(name = "predicted_labels") val predictedLabels: Map<String,Float>,
    @ColumnInfo(name = "timestamp") val timestamp: LocalDateTime,
)
