package com.example.pagingwithnetworkdbmvvm.newslist.repository

import androidx.paging.DataSource
import com.example.pagingwithnetworkdbmvvm.app.db.DatabaseNews
import com.example.pagingwithnetworkdbmvvm.newslist.db.NewsDao
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsBlock
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class NewsRepository(
    private val newsDao: NewsDao,
    private val db: DatabaseNews
) {

    fun insertNews(news: List<NewsBlock>): Completable =
        newsDao.insert(news)

    fun getNewsDataSource(queryName: String): DataSource.Factory<Int, NewsBlock> =
        newsDao.selectByQueryName(queryName)

    fun deleteNews(queryName: String): Completable =
        newsDao.deleteByQueryName(queryName)

    fun getNextIndexInNews(): Maybe<Int> =
        newsDao.getNextIndexInNews("android")

}