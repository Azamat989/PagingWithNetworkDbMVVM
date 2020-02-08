package com.example.pagingwithnetworkdbmvvm.newslist.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingwithnetworkdbmvvm.R
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsBlock
import com.example.pagingwithnetworkdbmvvm.util.NetworkState

class NewsAdapter(
    private val retryCallback: () -> Unit
) : PagedListAdapter<NewsBlock, RecyclerView.ViewHolder>(diffCallback) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.news_block_item -> NewsViewHolder.create(parent)
            R.layout.network_state_item -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.news_block_item -> {
                getItem(position)?.let {
                    (holder as NewsViewHolder).bindLayout(it)
                }
            }
            R.layout.network_state_item -> (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int =
        super.getItemCount() + if (hasExtraRow()) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == (itemCount - 1)) {
            R.layout.network_state_item
        } else {
            R.layout.news_block_item
        }
    }

    private fun hasExtraRow(): Boolean =
        (networkState != null && networkState != NetworkState.LOADED)

    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNetworkState the new network state
     */

    //Needed to create a NetworkStateViewHolder
    fun setNetworkState(newNetworkState: NetworkState) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val hadExtraRow = hasExtraRow()
                val previousState = this.networkState
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hasExtraRow != hadExtraRow) {
                    if (hadExtraRow) {
                        //LOADING -> LOADED
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        //LOADED -> LOADING
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hadExtraRow && previousState != newNetworkState) {
                    //LOADING -> FAILED
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<NewsBlock>() {

            override fun areItemsTheSame(oldItem: NewsBlock, newItem: NewsBlock): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: NewsBlock, newItem: NewsBlock): Boolean {
                return oldItem == newItem
            }
        }
    }
}