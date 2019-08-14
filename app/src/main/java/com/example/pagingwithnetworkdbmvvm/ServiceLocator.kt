package com.example.pagingwithnetworkdbmvvm

import android.app.Application
import android.content.Context
import com.example.pagingwithnetworkdbmvvm.api.NewsApi
import com.example.pagingwithnetworkdbmvvm.db.DatabaseNews
import com.example.pagingwithnetworkdbmvvm.repository.DbNewsPostRepository
import java.util.concurrent.Executors

interface ServiceLocator {

    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun createServiceLocator(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultLocator(context.applicationContext as Application)
                }
                return instance as ServiceLocator
            }
        }
    }

    fun getRepository(): DbNewsPostRepository

}

class DefaultLocator(app: Application): ServiceLocator {

    private val DISK_IO =Executors.newSingleThreadExecutor()

    private val api: NewsApi by lazy {
        NewsApi.getNewsApi()
    }

    private val db: DatabaseNews by lazy {
        DatabaseNews.create(app)
    }

    override fun getRepository(): DbNewsPostRepository =
        DbNewsPostRepository.instance(api, db, DISK_IO)

}