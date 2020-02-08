package com.example.pagingwithnetworkdbmvvm.newslist.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingwithnetworkdbmvvm.R
import com.example.pagingwithnetworkdbmvvm.newslist.domain.NewsBlock
import com.example.pagingwithnetworkdbmvvm.util.getProgressDrawable
import com.example.pagingwithnetworkdbmvvm.util.loadImage
import kotlinx.android.synthetic.main.news_block_item.view.*

class NewsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    init {
        itemView.setOnClickListener {
            newsBlock?.url?.let { uri ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_block_item, parent, false)
            return NewsViewHolder(view)
        }
    }

    private var newsBlock: NewsBlock? = null
    private val progressDrawable = getProgressDrawable(itemView.context)

    fun bindLayout(newsBlock: NewsBlock) {
        this.newsBlock = newsBlock

        itemView.text_view_news_title.text = newsBlock.title
        itemView.text_view_news_date.text = newsBlock.publishedAt
        itemView.text_view_news_description.text = newsBlock.description
        itemView.image_view_news_item.loadImage(newsBlock.urlToImage, progressDrawable)
    }
}