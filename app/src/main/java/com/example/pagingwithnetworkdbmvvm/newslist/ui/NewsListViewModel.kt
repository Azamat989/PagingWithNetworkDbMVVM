package com.example.pagingwithnetworkdbmvvm.newslist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.pagingwithnetworkdbmvvm.newslist.domain.Listing
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsBlock
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsInteractor
import com.example.pagingwithnetworkdbmvvm.util.NetworkState

class NewsListViewModel(
    private val newsInteractor: NewsInteractor
) : ViewModel() {

    private val queryName = MutableLiveData<String>()

    private val repoResult: LiveData<Listing<NewsBlock>> =
        Transformations.map(queryName) {
            newsInteractor.postsOfNews(it, 10)
        }

    val pagedList: LiveData<PagedList<NewsBlock>> =
        Transformations.switchMap(repoResult) { it.pagedList }

    val networkState: LiveData<NetworkState> =
        Transformations.switchMap(repoResult) { it.networkState }

    val refreshState: LiveData<NetworkState> =
        Transformations.switchMap(repoResult) { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        repoResult.value?.retry?.invoke()
    }

    fun setQueryName(queryName: String) {
        this.queryName.value = queryName
    }
}
