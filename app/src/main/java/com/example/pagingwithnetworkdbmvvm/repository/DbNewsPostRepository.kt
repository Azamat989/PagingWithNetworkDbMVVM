package com.example.pagingwithnetworkdbmvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pagingwithnetworkdbmvvm.api.NewsApi
import com.example.pagingwithnetworkdbmvvm.data.NewsBlock
import com.example.pagingwithnetworkdbmvvm.db.DatabaseNews
import com.example.pagingwithnetworkdbmvvm.util.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class DbNewsPostRepository(
    private val api: NewsApi,
    private val db: DatabaseNews,
    private val ioExecutor: Executor,
    private val networkPageSize: Int = DEFAULT_PAGE_SIZE
) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
        fun instance(api: NewsApi,
                     db: DatabaseNews,
                     ioExecutor: Executor,
                     pageSize: Int = DEFAULT_PAGE_SIZE): DbNewsPostRepository {
            return DbNewsPostRepository(api, db, ioExecutor)
        }
    }

    private lateinit var boundaryCallback: NewsBoundaryCallback

    private fun insertResultIntoDb(queryName: String, pageNumber: Int, body: NewsApi.ListingResponse?) {
        body?.articles.let {
            db.runInTransaction {
                val startIndex = db.news().getNextIndexInNews(queryName)
                val newsList = it?.mapIndexed { index, newsBlock ->
                    newsBlock.indexedResponse = startIndex + index
                    newsBlock.query = queryName
                    newsBlock.pageNumber = pageNumber
                    newsBlock
                }
                if (!newsList.isNullOrEmpty())
                    db.news().insert(newsList)
            }
        }
    }

    fun refresh(queryName: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        boundaryCallback.pageNumber = 1

        api.getTop(
            query = queryName,
            pageSize = networkPageSize,
            page = boundaryCallback.pageNumber
        ).enqueue(object: Callback<NewsApi.ListingResponse> {
            override fun onFailure(call: Call<NewsApi.ListingResponse>, t: Throwable) {
                networkState.value = NetworkState.error(t)
            }

            override fun onResponse(call: Call<NewsApi.ListingResponse>, response: Response<NewsApi.ListingResponse>) {
                ioExecutor.execute {
                    db.news().deleteByQueryName(queryName)
                    insertResultIntoDb(queryName, 1, response.body())
                }
                networkState.postValue(NetworkState.LOADED)
            }
        })
        return networkState
    }

    fun postsOfNews(queryName: String, pageSize: Int): Listing<NewsBlock> {
        //create BoundaryCallback
        val boundaryCallback = NewsBoundaryCallback(
            api = api,
            ioExecutor = ioExecutor,
            responseHandler = this::insertResultIntoDb,
            queryName = queryName,
            pageSize = pageSize
        )
        this.boundaryCallback = boundaryCallback

        val refreshTrigger = MutableLiveData<Unit>()
        val refresh = Transformations.switchMap(refreshTrigger) { refresh(queryName) }

        val dataSourceFactory = db.news().selectByQueryName(queryName)

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

        return Listing<NewsBlock> (
            pagedList = pagedList,
            networkState = boundaryCallback.networkState,
            refreshState = refresh,
            refresh = { refreshTrigger.value = null },
            retry = { boundaryCallback.helper.retryAllFailed() }
        )
    }

}