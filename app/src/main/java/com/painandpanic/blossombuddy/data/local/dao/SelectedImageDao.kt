package com.painandpanic.blossombuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.painandpanic.blossombuddy.data.local.model.SelectedImage
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSelectedImages(selectedImages: List<SelectedImage>)
    @Query("SELECT * FROM selected_images")
    fun getSelectedImages(): Flow<List<SelectedImage>>

    @Query("SELECT * FROM selected_images WHERE imageId = :id")
    fun getSelectedImage(id: Long): Flow<SelectedImage>
}