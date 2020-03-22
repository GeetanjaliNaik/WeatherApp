package com.geeta.weatherapp.injection.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.geeta.weatherapp.database.WeatherDataBase
import com.geeta.weatherapp.database.WeatherDataDao
import com.geeta.weatherapp.database.repositry.LocationDataDao
import com.geeta.weatherapp.database.repositry.WeatherDbRepository
import com.geeta.weatherapp.database.repositry.WeatherDbRepositoryLis
import com.geeta.weatherapp.injection.scopes.ApplicationContext
import com.geeta.weatherapp.injection.scopes.PerApplication
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(includes = arrayOf(AndroidInjectionModule::class))
class DataBaseModule() {
//    val dataBase:WeatherDataBase=

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext application: Application): WeatherDataBase? {
        return Room.databaseBuilder(
            application,
            WeatherDataBase::class.java, "weather_db"
        ).build()
    }
    @Provides
    @Singleton
    internal fun provideWeatherDataDao(weatherDataBase: WeatherDataBase): WeatherDataDao? {
        return weatherDataBase.weatherDataDao()
    }
    @Provides
    @Singleton
    internal fun provideLocationDataDao(weatherDataBase: WeatherDataBase): LocationDataDao? {
        return weatherDataBase.locationDataDao()
    }

    @Provides
    @Singleton
    internal fun provideWeatherRepository(weatherDataDao: WeatherDataDao,locationDataDao: LocationDataDao): WeatherDbRepositoryLis? {
        return WeatherDbRepository(weatherDataDao,locationDataDao)
    }

}