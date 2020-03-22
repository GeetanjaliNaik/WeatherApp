package com.geeta.weatherapp.injection.module

import com.geeta.weatherapp.injection.scopes.WorkerModelKey
import com.geeta.weatherapp.worker.WeatherWork
import com.geeta.weatherapp.worker.WeatherWorkerFactory
import com.geeta.weatherapp.worker.WorkerSFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerModelKey(WeatherWork::class)
    abstract fun bindHelloWorldWorker(factory: WorkerSFactory): WeatherWorkerFactory

}