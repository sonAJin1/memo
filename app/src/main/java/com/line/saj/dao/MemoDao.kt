package com.line.saj.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.line.saj.components.model.Memo
import org.joda.time.DateTime

@Dao
interface MemoDao {
    @Query("SELECT * FROM MEMOS")
    fun getAll(): LiveData<List<Memo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(memos: List<Memo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: Memo)

    @Query("DELETE from memos where id = :id")
    fun delete(id: Int)

    @Query("DELETE from memos")
    fun deleteAll()


    @Query("UPDATE memos SET title = :title, contents = :contents, image = :image, modifyDate = :modifyDate where id = :id")
    fun update(id:Int, title: String, contents: String, image: List<String>, modifyDate: DateTime)
}