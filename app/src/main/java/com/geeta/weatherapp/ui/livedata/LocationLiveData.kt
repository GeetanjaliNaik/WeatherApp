package com.geeta.weatherapp.ui.livedata

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.geeta.weatherapp.data.weather.LocationModel
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocationLiveData @Inject constructor(val context: Context) : LiveData<LocationModel>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    private fun setLocationData(location: Location) {
        value = LocationModel(
            longitude = location.longitude,
            latitude = location.latitude
        )
        Completable.fromRunnable(Runnable{
            var locationDataDao= WeatherDbRepository.invoke(context).locationDataDao()
            locationDataDao.deleteAll()
            locationDataDao.insertLocation(value!!)
        })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}