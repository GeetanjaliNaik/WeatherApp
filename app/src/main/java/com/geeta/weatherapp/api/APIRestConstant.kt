package com.geeta.weatherapp.api

import com.geeta.weatherapp.BuildConfig

/**
 * Created by Geetanjali.Naik on 02-02-2018.
 */
class APIRestConstant {
    companion object {
        const val BASE_API_URL = BuildConfig.BASE_URL/*"http://api.openweathermap.org/data/2.5/"*/
//        http://api.openweathermap.org/data/2.5/weather?id=7778677&appid=5ad7218f2e11df834b0eaf3a33a39d2a
        const val GET_WEATHER = "weather?"
    }
}