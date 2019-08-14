package com.example.pagingwithnetworkdbmvvm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newsBlock")
data class NewsBlock(
    @PrimaryKey
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    var pageNumber: Int,
    var query: String
) {
    var indexedResponse: Int = -1
}
