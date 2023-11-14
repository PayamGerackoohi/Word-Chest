package com.payamgr.wordchest.data.hilt

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.payamgr.wordchest.ui.page.home.HomeViewModel
import com.payamgr.wordchest.ui.page.home.HomeViewModelImpl
import com.payamgr.wordchest.ui.page.worddetail.WordDetailViewModel
import com.payamgr.wordchest.ui.page.worddetail.WordDetailViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun provideHomeViewModel(factory: HomeViewModelImpl.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(WordDetailViewModel::class)
    fun provideWordDetailViewModel(factory: WordDetailViewModelImpl.Factory): AssistedViewModelFactory<*, *>
}
