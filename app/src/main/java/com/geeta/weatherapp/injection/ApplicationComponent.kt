package com.geeta.weatherapp.injection

import android.app.Application
import com.geeta.weatherapp.WeatherApplication
import com.geeta.weatherapp.database.WeatherDataBase
import com.geeta.weatherapp.database.repositry.WeatherDbRepositoryLis
import com.geeta.weatherapp.injection.module.ActivityBindingModule
import com.geeta.weatherapp.injection.module.AppViewModule
import com.geeta.weatherapp.injection.module.DataBaseModule
import com.geeta.weatherapp.injection.module.WorkerModule
import com.geeta.weatherapp.injection.scopes.PerApplication
import com.geeta.weatherapp.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@PerApplication
@Singleton
@Component(modules = arrayOf(
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    /*WeatherApplicationModule::class,*/
    NetworkModule::class,
    /*DataBaseModule::class,*/
    WorkerModule::class,
    AppViewModule::class))
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
       /* @BindsInstance
        fun dataModule(dataBaseModule: DataBaseModule):Builder*/
        fun build(): ApplicationComponent

    }

    fun inject(app: WeatherApplication)

}
