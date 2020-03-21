package com.geeta.weatherapp.injection.scopes

import javax.inject.Qualifier
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerApplication

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext