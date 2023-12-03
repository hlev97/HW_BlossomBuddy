package com.painandpanic.blossombuddy.data.local.model

import android.net.Uri
import androidx.room.Entity

@Entity(tableName = "selected_images", primaryKeys = ["imageId"])
data class SelectedImage(
    var uri: Uri,
    val imageId: Long,
    val name: String,
    val title: String,
    val path: String,
    val size: Long,
    val modified: Long,
    val dateAdded: Long,
)