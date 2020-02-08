package com.example.pagingwithnetworkdbmvvm.newslist.api

import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsBlock

class ListingResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsBlock>
)