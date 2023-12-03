package com.painandpanic.blossombuddy.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.painandpanic.blossombuddy.data.local.converters.MapStringFloatConverter
import com.painandpanic.blossombuddy.data.local.converters.LocalDateTimeConverter
import com.painandpanic.blossombuddy.data.local.converters.UriConverter
import com.painandpanic.blossombuddy.data.local.dao.HistoryDao
import com.painandpanic.blossombuddy.data.local.dao.SelectedImageDao
import com.painandpanic.blossombuddy.data.local.model.HistoryEntity
import com.painandpanic.blossombuddy.data.local.model.SelectedImage


@Database(entities = [HistoryEntity::class, SelectedImage::class], version = 1,)
@TypeConverters(UriConverter::class, LocalDateTimeConverter::class, MapStringFloatConverter::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun selectedImageDao(): SelectedImageDao
}