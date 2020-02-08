package com.example.pagingwithnetworkdbmvvm.newslist.domain

import android.util.Log
import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.pagingwithnetworkdbmvvm.PagingRequestHelper
import com.example.pagingwithnetworkdbmvvm.PagingRequestHelper.RequestType.*
import com.example.pagingwithnetworkdbmvvm.newslist.api.ListingResponse
import com.example.pagingwithnetworkdbmvvm.newslist.gateway.NewsGateway
import com.example.pagingwithnetworkdbmvvm.util.createLiveDataStatus
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class NewsBoundaryCallback(
    private val newsGateway: NewsGateway,
    val helper: PagingRequestHelper,
    private val responseHandler: (String, Int, ListingResponse?) -> Completable,
    private val queryName: String,
    private val pageSize: Int
) : PagedList.BoundaryCallback<NewsBlock>() {

    var pageNumber: Int = 0
    val networkState = helper.createLiveDataStatus()

    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(INITIAL) { callback: PagingRequestHelper.Request.Callback? ->
            pageNumber = 1
            newsGateway.getTop(
                queryName = queryName,
                pageSize = pageSize,
                pageNumber = pageNumber
            )
                .flatMapCompletable { response ->
                    Log.d(TAG, "response.size=${response.articles.size}")
                    responseHandler(queryName, pageNumber, response)
                }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback?.recordSuccess()
                        Log.d(TAG, "SUCCESS")
                    },
                    {
                        callback?.recordFailure(it)
                        Log.e(TAG, it.message.orEmpty())
                    }
                )
        }
    }

    override fun onItemAtEndLoaded(itemAtFront: NewsBlock) {
        helper.runIfNotRunning(AFTER) { callback: PagingRequestHelper.Request.Callback? ->
            pageNumber++
            newsGateway.getAfter(
                queryName = queryName,
                pageSize = pageSize,
                pageNumber = pageNumber
            )
                .flatMapCompletable { response ->
                    Log.d(TAG, "response.size=${response.articles.size}")
                    responseHandler(queryName, pageNumber, response)
                }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback?.recordSuccess()
                        Log.d(TAG, "SUCCESS")
                    },
                    {
                        callback?.recordFailure(it)
                        Log.e(TAG, it.message.orEmpty())
                    }
                )
        }
    }

    companion object {
        private const val TAG = "NewsBoundaryCallback"
    }
}