package com.geeta.weatherapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class WeatherViewModelFactory @Inject constructor(private val context: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java!!)) {
            WeatherViewModel(context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}