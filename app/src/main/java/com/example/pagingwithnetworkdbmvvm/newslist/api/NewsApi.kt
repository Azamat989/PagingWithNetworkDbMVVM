package com.example.pagingwithnetworkdbmvvm.newslist.api


import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything/")
    fun getTop(
        @Query("q") query: String = "android",
        @Query("from") from: String = "2019-00-00",
        @Query("SortBy") sortBy: String = "published",
        @Query("apiKey") apiKey: String = "26eddb253e7840f988aec61f2ece2907",
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int = 1
    ): Single<ListingResponse>

    @GET("/v2/everything/")
    fun getAfter(
        @Query("q") query: String = "android",
        @Query("from") from: String = "2019-00-00",
        @Query("SortBy") sortBy: String = "published",
        @Query("apiKey") apiKey: String = "26eddb253e7840f988aec61f2ece2907",
        @Query("pageSize") pageSize: Int = 5,
        @Query("page") page: Int
    ): Single<ListingResponse>
}
