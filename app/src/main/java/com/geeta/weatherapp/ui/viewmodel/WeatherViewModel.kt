package com.geeta.weatherapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geeta.weatherapp.WeatherApplication
import com.geeta.weatherapp.api.datamanager.WeatherDataManager
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.data.weather.WeatherModel
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.geeta.weatherapp.utils.AppData
import com.geeta.weatherapp.utils.CommonResponseParser
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WeatherViewModel @Inject constructor(val application: Application) : ViewModel() {
    @Inject
    lateinit var weatherDataManager: WeatherDataManager
    @Inject
    lateinit var appData: AppData

    val progressBarLiveData = MutableLiveData<Boolean>()
    var weatherData= MutableLiveData<WeatherModel>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()
    var timerInterval:Number=0

   private var fusedLocationClient :FusedLocationProviderClient?=null
    var locationModel:LocationModel?=null
    fun getCurrentlocathion()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application/*WeatherApplication.appContext!!*/)
        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                location?.also {
//                    location.altitude

                    locationModel=LocationModel(
                        longitude = location.longitude,
                        latitude = location.latitude
                    )

                    appData.locationModel=locationModel
                    getCurrentweather()
                }
            }
    }
    fun getWeather()
    {
        progressBarLiveData.postValue(true)
        getCurrentlocathion()
    }
    fun getCurrentweather()
    {

        locationModel?.let { weatherDataManager.updateWeather(it.latitude,it.longitude)
            .subscribeOn(Schedulers.newThread())
            .map { result->
                WeatherDbRepository.invoke(application).weatherDataDao().insert(result)
                return@map result
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
//                    WeatherDbRepository.invoke(application).weatherDataDao().insert(result)
                    weatherData.postValue(result)
                    progressBarLiveData.postValue(false)
                }
                , {error->
                    weatherInfoFailureLiveData.value=
                        CommonResponseParser.ErrorParser.parseError(error, true)
                    progressBarLiveData.postValue(false)
                })  }
    }

}