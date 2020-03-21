package com.geeta.weatherapp.api

import com.geeta.weatherapp.api.services.WeatherService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Geetanjali on 16/01/18.
 */
@Singleton
class BaseApiManager @Inject constructor(private var retrofit: Retrofit) {
    lateinit var weatherService: WeatherService



    private fun initApiManager() {
        weatherService = create(WeatherService::class.java)

    }

    fun <T> create(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    init {
        initApiManager()
    }

}