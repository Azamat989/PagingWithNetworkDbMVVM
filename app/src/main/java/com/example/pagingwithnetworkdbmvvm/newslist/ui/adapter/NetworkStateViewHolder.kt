package com.example.pagingwithnetworkdbmvvm.newslist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingwithnetworkdbmvvm.R
import com.example.pagingwithnetworkdbmvvm.util.NetworkState
import com.example.pagingwithnetworkdbmvvm.util.Status
import kotlinx.android.synthetic.main.network_state_item.view.*

class NetworkStateViewHolder(
    view: View,
    retryCallback: () -> Unit
): RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }
    }

    init {
        itemView.button_retry_network.setOnClickListener { retryCallback.invoke() }
    }

    fun bind(networkState: NetworkState?) {

        itemView.apply {
            text_view_msg_network.visibility = if (networkState?.message != null) View.VISIBLE else View.GONE
            text_view_msg_network.text = networkState?.message
            button_retry_network.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
            progress_network.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
        }
    }
}