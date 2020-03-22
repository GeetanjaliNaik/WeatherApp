package com.geeta.weatherapp.injection.module


import com.geeta.weatherapp.injection.scopes.PerActivity
import com.geeta.weatherapp.ui.WeatherActivity
import com.geeta.weatherapp.ui.WeatherPermissionActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule() {
    @PerActivity
    @ContributesAndroidInjector
    abstract fun weatherPermissionActivity(): WeatherPermissionActivity

    @PerActivity
    @ContributesAndroidInjector
    abstract fun weatherActivity(): WeatherActivity


}