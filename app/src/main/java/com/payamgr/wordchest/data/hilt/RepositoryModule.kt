package com.payamgr.wordchest.data.hilt

import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.WordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideWordRepository(impl: WordRepositoryImpl): WordRepository
}
