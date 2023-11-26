package com.painandpanic.blossombuddy.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    @ColumnInfo(name = "predicted_label") val predictedLabel: String,
    @ColumnInfo(name = "timestamp") val timestamp: LocalDateTime,
)
