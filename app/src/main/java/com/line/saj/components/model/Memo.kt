package com.line.saj.components.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "memos")
data class Memo(

    @PrimaryKey @ColumnInfo(name = "id") var memoId: Int = 0
    , @ColumnInfo(name = "title") var title: String = ""
    , @ColumnInfo(name = "contents") var contents: String = ""
    , @ColumnInfo(name = "image") var image: String = ""
    , @ColumnInfo(name = "imageType") var imageType: String = ""
)