package com.geeta.weatherapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.geeta.weatherapp.ui.livedata.LocationLiveData
import javax.inject.Inject

class LocationViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData
}