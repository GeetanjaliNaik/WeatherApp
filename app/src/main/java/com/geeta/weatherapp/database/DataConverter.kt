package com.geeta.weatherapp.database

import androidx.room.TypeConverter
import com.geeta.weatherapp.data.weather.WeatherItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {

    @TypeConverter
    fun fromWeatherItemList(value: List<WeatherItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeatherItemList(value: String): List<WeatherItem> {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherItem>>() {}.type
        return gson.fromJson(value, type)
    }
}