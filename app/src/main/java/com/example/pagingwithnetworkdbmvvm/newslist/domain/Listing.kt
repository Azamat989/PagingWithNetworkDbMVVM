package com.example.pagingwithnetworkdbmvvm.newslist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.pagingwithnetworkdbmvvm.util.NetworkState
import io.reactivex.Flowable

class Listing<T> (
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)