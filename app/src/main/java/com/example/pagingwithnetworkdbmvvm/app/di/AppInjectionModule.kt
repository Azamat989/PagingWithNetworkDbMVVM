package com.example.pagingwithnetworkdbmvvm.app.di

import com.example.pagingwithnetworkdbmvvm.newslist.di.NewsListInjectionModule
import org.kodein.di.Kodein

object AppInjectionModule {

    val module = Kodein.Module(AppInjectionModule.javaClass.name) {

        import(ApiInjectionModule.module)

        import(DatabaseInjectionModule.module)

        import(NewsListInjectionModule.module)
    }
}