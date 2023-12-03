package com.painandpanic.blossombuddy.data.local.contentresolver

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.painandpanic.blossombuddy.data.local.model.SelectedImage

fun queryContentResolver(context: Context, result: (List<SelectedImage>) -> Unit) {
    val selectedImages = arrayListOf<SelectedImage>()
    val volume: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val query = context.contentResolver.query(volume, projection, null, null, null)
    query?.use { cursor ->
        while (cursor.moveToNext()) {
            val imageId: Long = cursor.getColumnLong(MediaStore.Images.Media._ID)
            val name: String = cursor.getColumnString(MediaStore.Images.Media.DISPLAY_NAME)
            val title: String = cursor.getColumnString(MediaStore.Images.Media.TITLE)
            val path: String = cursor.getColumnString(MediaStore.Images.Media.DATA)
            val size: Long = cursor.getColumnLong(MediaStore.Images.Media.SIZE)
            val modified: Long = cursor.getColumnLong(MediaStore.Images.Media.DATE_MODIFIED)
            val dateAdded: Long = cursor.getColumnLong(MediaStore.Images.Media.DATE_ADDED)

            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId)
            selectedImages.add(SelectedImage(contentUri, imageId, name, title, path, size, modified, dateAdded))
        }
    }

    result(selectedImages)
}

val projection: Array<String> by lazy {
    arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.TITLE,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    )
}

fun Cursor.getColumnString(mediaColumn: String): String = getString(getColumnIndexOrThrow(mediaColumn)) ?: ""
fun Cursor.getColumnLong(mediaColumn: String): Long = getLong(getColumnIndexOrThrow(mediaColumn))