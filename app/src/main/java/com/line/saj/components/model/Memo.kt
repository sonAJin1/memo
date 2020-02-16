package com.line.saj.components.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.line.saj.dao.Converts
import org.joda.time.DateTime
import java.util.*


@Entity(tableName = "memos")
@TypeConverters(Converts::class)
data class Memo(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var memoId: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = "",

    @ColumnInfo(name = "contents")
    var contents: String? = "",

    @ColumnInfo(name = "image")
    var image: List<String>,

    @ColumnInfo(name = "creationDate")
    var creationDate: DateTime,

    @ColumnInfo(name = "modifyDate")
    var modifyDate: DateTime?

) : Parcelable {

    constructor(parcel: Parcel) :
            this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                listOf<String>().apply {
                    parcel.readList(this, String::class.java.classLoader)
                },
                parcel.readSerializable() as DateTime,
                parcel.readSerializable() as DateTime)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(memoId)
        parcel.writeString(title)
        parcel.writeString(contents)
        parcel.writeStringList(image)
        parcel.writeSerializable(creationDate)
        parcel.writeSerializable(modifyDate)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Memo> {
        override fun createFromParcel(parcel: Parcel): Memo {
            return Memo(parcel)
        }

        override fun newArray(size: Int): Array<Memo?> {
            return arrayOfNulls(size)
        }
    }

}