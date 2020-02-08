package com.example.pagingwithnetworkdbmvvm.newslist.di

import com.example.pagingwithnetworkdbmvvm.PagingRequestHelper
import com.example.pagingwithnetworkdbmvvm.app.db.DatabaseNews
import com.example.pagingwithnetworkdbmvvm.newslist.api.NewsApi
import com.example.pagingwithnetworkdbmvvm.newslist.db.NewsDao
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsInteractor
import com.example.pagingwithnetworkdbmvvm.newslist.gateway.NewsGateway
import com.example.pagingwithnetworkdbmvvm.newslist.repository.NewsRepository
import com.example.pagingwithnetworkdbmvvm.newslist.ui.NewsListViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object NewsListInjectionModule {

    val module = Kodein.Module(NewsListInjectionModule.javaClass.name) {

        bind<NewsListViewModel>() with provider { NewsListViewModel(instance()) }

        bind<Executor>() with singleton {
            Executors.newSingleThreadExecutor()
        }

        bind<PagingRequestHelper>() with singleton {
            PagingRequestHelper(instance())
        }

        bind<NewsDao>() with singleton {
            instance<DatabaseNews>().newsDao()
        }

        bind<NewsApi>() with singleton {
            instance<Retrofit>().create(NewsApi::class.java)
        }

        bind<NewsGateway>() with singleton {
            NewsGateway(instance())
        }

        bind<NewsRepository>() with singleton {
            NewsRepository(instance(), instance())
        }

        bind<NewsInteractor>() with singleton {
            NewsInteractor(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }
}