package com.example.pagingwithnetworkdbmvvm.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagingwithnetworkdbmvvm.api.NewsApi
import com.example.pagingwithnetworkdbmvvm.data.NewsBlock

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<NewsBlock>)

    @Query("SELECT * FROM newsBlock WHERE `query` = :queryName")
    fun selectByQueryName(queryName: String): DataSource.Factory<Int, NewsBlock>

    @Query("DELETE FROM newsBlock WHERE `query` = :queryName")
    fun deleteByQueryName(queryName: String)

    @Query("SELECT MAX(indexedResponse) + 1 FROM newsBlock WHERE `query` = :queryName")
    fun getNextIndexInNews(queryName: String): Int
}