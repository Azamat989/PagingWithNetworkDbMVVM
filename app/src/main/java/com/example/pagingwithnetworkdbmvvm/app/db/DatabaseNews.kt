package com.example.pagingwithnetworkdbmvvm.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pagingwithnetworkdbmvvm.newslist.db.NewsDao
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsBlock

@Database(entities = [NewsBlock::class], version = 1)
abstract class DatabaseNews : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        const val DATABASE_NAME = "news_db"
    }
}