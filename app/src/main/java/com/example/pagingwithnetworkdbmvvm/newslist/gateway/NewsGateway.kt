package com.example.pagingwithnetworkdbmvvm.newslist.gateway

import com.example.pagingwithnetworkdbmvvm.newslist.api.ListingResponse
import com.example.pagingwithnetworkdbmvvm.newslist.api.NewsApi
import io.reactivex.Single

class NewsGateway(
    private val newsApi: NewsApi
) {

    fun getTop(
        queryName: String,
        pageSize: Int,
        pageNumber: Int
    ): Single<ListingResponse> =
        newsApi.getTop(query = queryName, pageSize = pageSize)

    fun getAfter(
        queryName: String,
        pageSize: Int,
        pageNumber: Int
    ): Single<ListingResponse> =
        newsApi.getAfter(pageSize = pageSize, page = pageNumber)

}