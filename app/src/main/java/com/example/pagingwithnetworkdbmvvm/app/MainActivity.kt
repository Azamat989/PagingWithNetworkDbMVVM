package com.example.pagingwithnetworkdbmvvm.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.pagingwithnetworkdbmvvm.R
import com.example.pagingwithnetworkdbmvvm.newslist.ui.NewsListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate() is called")

        supportFragmentManager.commit {
            Log.d(TAG, "fragment transaction")
            addToBackStack(null)
            replace(R.id.fragmentContainerView, NewsListFragment())
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
