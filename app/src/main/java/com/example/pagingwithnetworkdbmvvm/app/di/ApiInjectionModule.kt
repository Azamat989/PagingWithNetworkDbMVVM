package com.example.pagingwithnetworkdbmvvm.app.di

import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiInjectionModule {

    private const val BASE_URL = "https://newsapi.org/"

    private const val timeoutDurationMinutes = 1L

    val module = Kodein.Module(ApiInjectionModule.javaClass.name) {

        bind<Gson>() with singleton { Gson() }

        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(instance())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(instance()))
                .build()
        }

        bind<OkHttpClient>() with provider {

            OkHttpClient
                .Builder().apply {
                    callTimeout(timeoutDurationMinutes, TimeUnit.MINUTES)
                    readTimeout(timeoutDurationMinutes, TimeUnit.MINUTES)
                    connectTimeout(timeoutDurationMinutes, TimeUnit.MINUTES)
                    writeTimeout(timeoutDurationMinutes, TimeUnit.MINUTES)
                }
                .build()
        }
    }
}