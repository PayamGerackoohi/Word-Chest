package com.payamgr.wordchest.data.hilt

import android.app.Activity
import com.payamgr.wordchest.data.controller.SplashScreenController
import com.payamgr.wordchest.data.controller.SplashScreenControllerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
abstract class ConstantsModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchDelay

    @Binds
    abstract fun splashScreenController(impl: SplashScreenControllerImpl): SplashScreenController

    companion object {
        @Provides
        @SearchDelay
        fun searchDelay(): Long = 500L
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppModulesProvider {
    fun splashScreenController(): SplashScreenController
}

fun Activity.provideAppModule() = EntryPoints.get(application, AppModulesProvider::class.java)
