package com.geeta.weatherapp.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geeta.weatherapp.api.BaseApiManager
import com.geeta.weatherapp.injection.scopes.ViewModelKey
import com.geeta.weatherapp.ui.viewmodel.LocationViewModel
import com.geeta.weatherapp.ui.viewmodel.WeatherViewModel
import com.geeta.weatherapp.ui.viewmodel.WeatherViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppViewModule {
    /*
     * inject this object into a Map using the @IntoMap annotation,
     * with the  WeatherViewModel.class as key,
     * and a Provider that will build a WeatherViewModel
     * object.
     *
     * */
    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun currentWeatherViewModel(weatherViewModel: WeatherViewModel?): ViewModel?

    @Binds
    abstract fun bindWeatherViewModelFactory(factory: WeatherViewModelFactory?): ViewModelProvider.Factory?

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    abstract fun currentLocationViewModel(locationViewModel: LocationViewModel?): ViewModel?



}

