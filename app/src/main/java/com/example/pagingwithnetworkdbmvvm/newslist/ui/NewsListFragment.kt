package com.example.pagingwithnetworkdbmvvm.newslist.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingwithnetworkdbmvvm.R
import com.example.pagingwithnetworkdbmvvm.newslist.ui.adapter.NewsAdapter
import com.example.pagingwithnetworkdbmvvm.util.NetworkState
import com.example.pagingwithnetworkdbmvvm.util.viewModel
import kotlinx.android.synthetic.main.news_list_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class NewsListFragment : Fragment(R.layout.news_list_fragment), KodeinAware {

    override val kodein: Kodein by kodein()

    private val viewModel: NewsListViewModel by viewModel()

    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated() is called")

        viewModel.setQueryName(QUERY_NAME)

        initAdapter()

        initSwipeToRefresh()
    }

    private fun initAdapter() {
        Log.d(TAG, "initAdapter() is called")

        newsAdapter = NewsAdapter { viewModel.retry() }
        newsRecyclerView.adapter = newsAdapter
        newsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        viewModel.pagedList
            .observe(this, Observer {
                newsAdapter.submitList(it)
            })

        viewModel.networkState
            .observe(this, Observer {
                newsAdapter.setNetworkState(it)
            })
    }

    private fun initSwipeToRefresh() {

        viewModel.refreshState
            .observe(this, Observer {
                newsSwipeRefreshLayout.isRefreshing = NetworkState.LOADING == it
            })

        newsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    companion object {

        private const val TAG = "NewsListFragment"

        private const val QUERY_NAME = "android"
    }
}
