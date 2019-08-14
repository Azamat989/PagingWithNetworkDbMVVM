package com.example.pagingwithnetworkdbmvvm.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pagingwithnetworkdbmvvm.PagingRequestHelper

private fun getThrowable(report: PagingRequestHelper.StatusReport): Throwable {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)
    }.first()
}

fun PagingRequestHelper.createLiveDataStatus(): LiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener {
        when {
            it.hasRunning() -> liveData.postValue(NetworkState.LOADING)
            it.hasError() -> liveData.postValue(NetworkState.error(getThrowable(it)))
        }
    }
    return liveData
}