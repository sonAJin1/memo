package com.line.saj.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.line.saj.components.model.Memo
import com.line.saj.utils.Constants

@Database(entities = [Memo::class], version = 3, exportSchema = false)
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
            ).fallbackToDestructiveMigration()
                .build()
        }

    }
}