package com.geeta.weatherapp.injection.module


import com.geeta.weatherapp.injection.scopes.PerActivity
import com.geeta.weatherapp.ui.WeatherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule() {
    @PerActivity
    @ContributesAndroidInjector
    abstract fun weatherActivity(): WeatherActivity


}