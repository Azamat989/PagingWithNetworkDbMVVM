package com.example.pagingwithnetworkdbmvvm.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.pagingwithnetworkdbmvvm.data.NewsBlock
import com.example.pagingwithnetworkdbmvvm.util.NetworkState

class Listing<T> (
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)