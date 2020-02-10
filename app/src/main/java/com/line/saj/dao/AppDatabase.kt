package com.line.saj.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.line.saj.components.model.Memo

@Database(entities = [Memo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catDao(): MemoDao


    companion object {
        val DB_NAME = "databinding-db"

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
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
                DB_NAME
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

//                        var request = OneTimeWorkRequest.Builder(CatDBWoker::class.java).build()
//                        WorkManager.getInstance().enqueue(request)
                    }
                }).build()
        }

    }
}