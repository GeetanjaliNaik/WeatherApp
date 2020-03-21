package com.geeta.weatherapp.worker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.geeta.weatherapp.api.datamanager.WeatherDataManager
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.geeta.weatherapp.utils.CommonResponseParser
import com.geeta.weatherapp.utils.KEY_WEATHER
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherWork(context: Context,workerParameters: WorkerParameters) : Worker(context,workerParameters) {

    @Inject
    lateinit var weatherDataManager: WeatherDataManager

    override fun doWork(): Result {

            getCurrentlocathion()
            return Result.success()

    }
    fun getCurrentlocathion()
    {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.applicationContext/*WeatherApplication.appContext!!*/)
        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                location?.also {
                    //                    location.altitude

                    var locationModel=LocationModel(
                        longitude = location.longitude,
                        latitude = location.latitude
                    )

                    getCurrentweather(location)
                }
            }
    }
    @SuppressLint("CheckResult")
    fun getCurrentweather(location: Location)
    {
            weatherDataManager.updateWeather(location.latitude,location.longitude).subscribe(
                {result->
                    WeatherDbRepository.invoke(this.applicationContext).weatherDataDao().insert(result)
                }
                , {error->

                })

    }

}