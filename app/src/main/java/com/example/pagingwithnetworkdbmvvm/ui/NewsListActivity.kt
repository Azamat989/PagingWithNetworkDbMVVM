package com.example.pagingwithnetworkdbmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingwithnetworkdbmvvm.R
import com.example.pagingwithnetworkdbmvvm.ServiceLocator
import com.example.pagingwithnetworkdbmvvm.adapter.NewsAdapter
import com.example.pagingwithnetworkdbmvvm.util.NetworkState
import kotlinx.android.synthetic.main.activity_news_list.*

class NewsListActivity : AppCompatActivity() {

    companion object {
        private const val QUERY_NAME = "android"
    }

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var mViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        mViewModel = getViewModel()
        initAdapter()
        initSwipeToRefresh()
        mViewModel.setQueryName(QUERY_NAME)
    }

    private fun getViewModel(): NewsViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = ServiceLocator.createServiceLocator(this@NewsListActivity).getRepository()
                return NewsViewModel(repo) as T
            }
        })[NewsViewModel::class.java]
    }

    private fun initAdapter() {
        //layout manager
        newsAdapter = NewsAdapter {mViewModel.retry()}
        recycler_view_news.adapter = newsAdapter
        recycler_view_news.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        mViewModel.pagedList.observe(this, Observer {
            newsAdapter.submitList(it)
        })

        mViewModel.networkState.observe(this, Observer {
            newsAdapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {

        mViewModel.refreshState.observe(this, Observer {
            swipe_refresh_news.isRefreshing = NetworkState.LOADING == it
        })

        swipe_refresh_news.setOnRefreshListener {
            mViewModel.refresh()
        }
    }
}
