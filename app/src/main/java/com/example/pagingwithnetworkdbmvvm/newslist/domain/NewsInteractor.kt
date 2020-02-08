package com.example.pagingwithnetworkdbmvvm.newslist.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pagingwithnetworkdbmvvm.PagingRequestHelper
import com.example.pagingwithnetworkdbmvvm.newslist.api.ListingResponse
import com.example.pagingwithnetworkdbmvvm.newslist.gateway.NewsGateway
import com.example.pagingwithnetworkdbmvvm.newslist.repository.NewsRepository
import com.example.pagingwithnetworkdbmvvm.util.NetworkState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class NewsInteractor(
    private val newsGateway: NewsGateway,
    private val newsRepository: NewsRepository,
    private val pagingRequestHelper: PagingRequestHelper,
    private val ioExecutor: Executor
) {

    private lateinit var boundaryCallback: NewsBoundaryCallback

    private val compositeDisposable = CompositeDisposable()

    private fun insertResultIntoDb(
        queryName: String,
        pageNumber: Int,
        body: ListingResponse?
    ): Completable =
        body?.articles?.let {
            Log.d(TAG, "insertResultIntoDb() is called")
            newsRepository
                .getNextIndexInNews()
                .defaultIfEmpty(1)
                .map { startIndex ->
                    it.mapIndexed { index, newsBlock ->
                        Log.d(TAG, "startIndex=$startIndex, index=$index")

                        newsBlock.indexedResponse = startIndex + index
                        newsBlock.query = queryName
                        newsBlock.pageNumber = pageNumber
                        newsBlock
                    }
                }
                .flatMapCompletable {
                    Log.d(TAG, "insert news.size=${it.size}")
                    newsRepository.insertNews(it)
                }
    } ?: Completable.complete()

    private fun refresh(queryName: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        boundaryCallback.pageNumber = 1

        newsGateway.getTop(
            queryName = queryName,
            pageSize = DEFAULT_PAGE_SIZE,
            pageNumber = boundaryCallback.pageNumber
        )
            .flatMapCompletable {
                newsRepository.deleteNews(queryName)
                    .andThen(insertResultIntoDb(queryName, 1, it))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    networkState.postValue(NetworkState.LOADED)
                },
                { networkState.value = NetworkState.error(it) }
            )
            .let { compositeDisposable.add(it) }

        return networkState
    }

    fun postsOfNews(queryName: String, pageSize: Int): Listing<NewsBlock> {
        //create BoundaryCallback
        val boundaryCallback =
            NewsBoundaryCallback(
                newsGateway = newsGateway,
                helper = pagingRequestHelper,
                responseHandler = this::insertResultIntoDb,
                queryName = queryName,
                pageSize = pageSize
            )
        this.boundaryCallback = boundaryCallback

        val refreshTrigger = MutableLiveData<Unit>()
        val refresh = Transformations.switchMap(refreshTrigger) { refresh(queryName) }

        val dataSourceFactory = newsRepository.getNewsDataSource(queryName)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(DEFAULT_PAGE_SIZE)
            .setMaxSize(Int.MAX_VALUE)
            .setPageSize(DEFAULT_PAGE_SIZE)
            .setPrefetchDistance(5)
            .build()

        val pagedList = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .setBoundaryCallback(boundaryCallback)
            .setFetchExecutor(ioExecutor)
            .setInitialLoadKey(null)
            .build()

        return Listing<NewsBlock>(
            pagedList = pagedList,
            networkState = boundaryCallback.networkState,
            refreshState = refresh,
            refresh = { refreshTrigger.value = null },
            retry = { boundaryCallback.helper.retryAllFailed() }
        )
    }

    companion object {
        private val TAG = "NewsInteractor"

        private const val DEFAULT_PAGE_SIZE = 10
    }
}