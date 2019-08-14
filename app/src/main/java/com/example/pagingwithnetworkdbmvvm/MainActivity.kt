package com.example.pagingwithnetworkdbmvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pagingwithnetworkdbmvvm.ui.NewsListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, NewsListActivity::class.java)
        startActivity(intent)
        finish()
    }
}
