package com.payamgr.wordchest.data.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object ConstantsModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchDelay

    @Provides
    @SearchDelay
    fun provideSearchDelay(): Long = 500L
}
