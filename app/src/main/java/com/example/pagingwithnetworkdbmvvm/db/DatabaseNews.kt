package com.example.pagingwithnetworkdbmvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pagingwithnetworkdbmvvm.data.NewsBlock

@Database(entities = [NewsBlock::class], version = 1)
abstract class DatabaseNews: RoomDatabase() {

    companion object {
        fun create(context: Context): DatabaseNews {
            val instance: DatabaseNews
            return Room.databaseBuilder(context, DatabaseNews::class.java, "db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun news(): NewsDao
}