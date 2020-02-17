package com.line.saj.repository

import com.line.saj.components.model.Memo
import com.line.saj.dao.MemoDao

class MemoRepository private constructor(private val memoDao: MemoDao) {
    fun getAllMemos() = memoDao.getAll()
    fun addMemos(memo: Memo) = memoDao.insert(memo)
    fun deleteMemo(id: Int) = memoDao.delete(id)
    fun getMemo(id: Int) = memoDao.get(id)
    fun deleteAll() = memoDao.deleteAll()
    fun update(id: Int, title: String, contents: String) =
        memoDao.update(id, title, contents)

    companion object {
        @Volatile
        private var instance: MemoRepository? = null

        fun getInstance(memoDao: MemoDao) =
            instance ?: synchronized(this) {
                instance ?: MemoRepository(memoDao).also { instance = it }
            }

    }
}