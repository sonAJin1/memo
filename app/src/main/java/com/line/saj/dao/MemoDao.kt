package com.line.saj.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.line.saj.components.model.Memo

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
}