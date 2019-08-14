package com.example.pagingwithnetworkdbmvvm.repository

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.pagingwithnetworkdbmvvm.PagingRequestHelper
import com.example.pagingwithnetworkdbmvvm.api.NewsApi
import com.example.pagingwithnetworkdbmvvm.data.NewsBlock
import com.example.pagingwithnetworkdbmvvm.util.createLiveDataStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class NewsBoundaryCallback(
    private val api: NewsApi,
    private val ioExecutor: Executor,
    private val responseHandler: (String, Int, NewsApi.ListingResponse?) -> Unit,
    private val queryName: String,
    private val pageSize: Int
): PagedList.BoundaryCallback<NewsBlock>() {

    var pageNumber: Int = 0
    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createLiveDataStatus()

    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            pageNumber = 1
            api.getTop(query = queryName,
                pageSize = pageSize,
                page = pageNumber)
                .enqueue(createWebService(it, pageNumber))
        }
    }

    override fun onItemAtEndLoaded(itemAtFront: NewsBlock) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            pageNumber++
            api.getAfter(query = queryName,
                pageSize = pageSize,
                page = pageNumber)
                .enqueue(createWebService(it, pageNumber))
        }
    }

    private fun createWebService(requestCallback: PagingRequestHelper.Request.Callback, pageNumber: Int): Callback<NewsApi.ListingResponse> {
        return object : Callback<NewsApi.ListingResponse> {
            override fun onFailure(call: Call<NewsApi.ListingResponse>, t: Throwable) {
                requestCallback.recordFailure(t)
            }

            override fun onResponse(call: Call<NewsApi.ListingResponse>, response: Response<NewsApi.ListingResponse>) {
                ioExecutor.execute {
                    responseHandler(queryName, pageNumber, response.body())
                    requestCallback.recordSuccess()
                }
            }

        }
    }
}