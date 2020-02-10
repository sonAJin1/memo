package com.line.saj.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.line.saj.components.model.Memo

@Dao
interface MemoDao {
    @Query("SELECT * FROM MEMOS")
    fun getAll(): LiveData<List<Memo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cats: List<Memo>)
}