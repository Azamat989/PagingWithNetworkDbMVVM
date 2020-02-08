package com.example.pagingwithnetworkdbmvvm.util

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pagingwithnetworkdbmvvm.R
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.cat)

    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified TViewModel, TFragment> TFragment.viewModel(
    tag: String? = null
): Lazy<TViewModel>
        where TViewModel : ViewModel,
              TFragment : KodeinAware,
              TFragment : Fragment {

    return lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>) =
                kodein.direct.instance<TViewModel>(tag) as T
        })
            .get(TViewModel::class.java)
    }
}