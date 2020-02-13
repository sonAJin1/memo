package com.line.saj.dao

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson

class Converts {

    @TypeConverter
    fun listToJson(value: List<Uri>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Uri>? {

        val objects = Gson().fromJson(value, Array<Uri>::class.java) as Array<Uri>
        return objects.toList()
    }
}