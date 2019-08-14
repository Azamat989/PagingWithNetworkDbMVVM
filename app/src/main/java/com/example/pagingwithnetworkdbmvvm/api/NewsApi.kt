package com.example.pagingwithnetworkdbmvvm.api


import com.example.pagingwithnetworkdbmvvm.data.NewsBlock
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything/")
    fun getTop(@Query("q") query: String = "android",
               @Query("from") from: String = "2019-00-00",
               @Query("SortBy") sortBy: String = "published",
               @Query("apiKey") apiKey: String = "26eddb253e7840f988aec61f2ece2907",
               @Query("pageSize") pageSize: Int,
               @Query("page") page: Int = 1
               ): Call<ListingResponse>

    @GET("/v2/everything/")
    fun getAfter(@Query("q") query: String = "android",
                 @Query("from") from: String = "2019-00-00",
                 @Query("SortBy") sortBy: String = "published",
                 @Query("apiKey") apiKey: String = "26eddb253e7840f988aec61f2ece2907",
                 @Query("pageSize") pageSize: Int = 5,
                 @Query("page") page: Int
    ): Call<ListingResponse>

    class ListingResponse(
        val status: String,
        val totalResults: Int,
        val articles: List<NewsBlock>
    )

    companion object {
        fun getNewsApi(): NewsApi {

            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }
}
