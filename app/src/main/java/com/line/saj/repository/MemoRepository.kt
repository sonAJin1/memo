package com.line.saj.repository

import com.line.saj.dao.MemoDao

class MemoRepository private constructor(private val memoDao: MemoDao) {
    fun getAllMemos() = memoDao.getAll()

    companion object {
        @Volatile
        private var instance: MemoRepository? = null

        fun getInstance(memoDao: MemoDao) =
            instance ?: synchronized(this) {
                instance ?: MemoRepository(memoDao).also { instance = it }
            }

    }
}