package com.geeta.weatherapp.worker

import com.geeta.weatherapp.BuildConfig
import com.geeta.weatherapp.api.BaseApiManager
import com.geeta.weatherapp.network.ErrorInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class WorkmanagerNetwork {
    companion object {

        fun getClient(): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client =
                OkHttpClient.Builder()
                    .connectTimeout(1200.toLong(), TimeUnit.SECONDS)
                    .readTimeout(1200.toLong(), TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addInterceptor(ErrorInterceptor())
                    .build()
            var retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
            return retrofit
        }

    }


}