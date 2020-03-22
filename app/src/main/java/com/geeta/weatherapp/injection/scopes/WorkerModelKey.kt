package com.geeta.weatherapp.injection.scopes

import androidx.lifecycle.ViewModel
import androidx.work.Worker
import dagger.MapKey
import java.lang.annotation.*
import java.lang.annotation.Retention
import java.lang.annotation.Target
import kotlin.reflect.KClass

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@MapKey
internal annotation class WorkerModelKey(val value: KClass<out Worker>)