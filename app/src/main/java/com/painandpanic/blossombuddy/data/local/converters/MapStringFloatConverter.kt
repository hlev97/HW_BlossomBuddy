package com.painandpanic.blossombuddy.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
object MapStringFloatConverter {
    @TypeConverter
    fun fromListString(value: Map<String,Float>): String {
        return Json.encodeToString(MapSerializer(String.serializer(),Float.serializer()), value)
    }

    @TypeConverter
    fun toListString(value: String): Map<String,Float> {
        return Json.decodeFromString(MapSerializer(String.serializer(),Float.serializer()), value)
    }
}