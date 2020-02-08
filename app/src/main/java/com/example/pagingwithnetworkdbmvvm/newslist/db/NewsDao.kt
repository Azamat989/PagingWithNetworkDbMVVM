package com.example.pagingwithnetworkdbmvvm.newslist.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsBlock
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<NewsBlock>): Completable

    @Query("SELECT * FROM newsBlock WHERE `query` = :queryName")
    fun selectByQueryName(queryName: String): DataSource.Factory<Int, NewsBlock>

    @Query("DELETE FROM newsBlock WHERE `query` = :queryName")
    fun deleteByQueryName(queryName: String): Completable

    @Query("SELECT MAX(indexedResponse) + 1 FROM newsBlock WHERE `query` = :queryName")
    fun getNextIndexInNews(queryName: String): Maybe<Int>
}