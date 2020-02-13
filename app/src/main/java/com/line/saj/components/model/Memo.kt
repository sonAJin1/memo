package com.line.saj.components.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI


//TODO: image type ArrayList로 변경할 것

@Entity(tableName = "memos")
data class Memo(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var memoId: Int = 0
    , @ColumnInfo(name = "title") var title: String = ""
    , @ColumnInfo(name = "contents") var contents: String = ""
    , @ColumnInfo(name = "image") var image: String = ""
    , @ColumnInfo(name = "imageType") var imageType: String = "" // array로 변경할
    , @ColumnInfo(name = "date") var date: String = ""
)