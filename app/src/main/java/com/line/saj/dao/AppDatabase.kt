package com.line.saj.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.line.saj.components.model.Memo
import com.line.saj.utils.Constants

@Database(entities = [Memo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao


    companion object {
        val DB_NAME = Constants.DB_NAME

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java,
                DB_NAME
            ).build()
//                .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//
//                    val memo =  ArrayList<Memo>()
//                    memo.add(Memo(0,"test1","test"))
//                    memo.add(Memo(1,"test2","test"))
//                    memo.add(Memo(2,"test3","test"))
//
//                    val database = AppDatabase.getInstance(context)
//                    database.memoDao().insertAll(memo)
//
//                }
//            }).build()
        }

    }
}