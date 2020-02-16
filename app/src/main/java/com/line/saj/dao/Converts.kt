package com.line.saj.dao

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class Converts {

    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }

    @TypeConverter
    fun dateToString(value: DateTime): String{
        return value.toString()
    }

    @TypeConverter
    fun stringToDate(value: String): DateTime{
        return DateTime.parse(value)
    }
}