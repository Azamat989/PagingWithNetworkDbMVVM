package com.example.pagingwithnetworkdbmvvm.app.di

import androidx.room.Room
import com.example.pagingwithnetworkdbmvvm.app.db.DatabaseNews
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object DatabaseInjectionModule {

    val module = Kodein.Module(DatabaseInjectionModule.javaClass.name) {

        bind<DatabaseNews>() with singleton {
            Room
                .databaseBuilder( instance(), DatabaseNews::class.java, DatabaseNews.DATABASE_NAME )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}