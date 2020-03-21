package com.geeta.weatherapp.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.geeta.weatherapp.BuildConfig
import com.geeta.weatherapp.api.APIRestConstant
import com.geeta.weatherapp.api.BaseApiManager
import com.geeta.weatherapp.utils.AppData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier

/*
 * Created by Geetanjali on 09/01/18.

*/
@Module(includes = arrayOf(AndroidInjectionModule::class))
class NetworkModule @Inject constructor() {
    private val HTTP_CONNECTION_TIMEOUT = 1200 // Seconds
    private val HTTP_READ_TIMEOUT = 1200 // Seconds




    @Provides
    @Singleton
    internal  fun provideHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, session -> true }
    }

    @Provides
    @Singleton
    internal  fun provideOkHttpClient(interceptor: HttpLoggingInterceptor, hostnameVerifier: HostnameVerifier): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.hostnameVerifier(hostnameVerifier)
        builder.connectTimeout(HTTP_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(HTTP_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.addInterceptor(interceptor)
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor());
        }
        builder.addInterceptor(ErrorInterceptor())
        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().setLenient().setDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.sssZ").create()
    }


    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(APIRestConstant.BASE_API_URL).client(okHttpClient).addConverterFactory(
            ScalarsConverterFactory.create()).addConverterFactory(
            GsonConverterFactory.create(gson)).addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()).build()
    }


    /*@Provides
    @Singleton
    internal fun provideRxBus(): RxBus {
        return RxBus()
    }*/

    @Provides
    @Singleton
    internal fun provideAppData(): AppData {
        return AppData()
    }

    @Provides
    @Singleton
    internal fun provideBaseApiManager(retrofit: Retrofit):BaseApiManager
    {
        return BaseApiManager(retrofit)
    }
}
