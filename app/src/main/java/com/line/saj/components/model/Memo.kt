package com.line.saj.components.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.line.saj.dao.Converts


@Entity(tableName = "memos")
@TypeConverters(Converts::class)
data class Memo(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var memoId: Int = 0,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "contents")
    var contents: String = "",

    @ColumnInfo(name = "image")
    var image: List<String>,

    @ColumnInfo(name = "imageType")
    var imageType: String = "",

    @ColumnInfo(name = "date")
    var date: String = ""
)