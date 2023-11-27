package com.payamgr.wordchest.data.hilt

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.payamgr.wordchest.ui.page.home.HomeVM
import com.payamgr.wordchest.ui.page.home.HomeVMImpl
import com.payamgr.wordchest.ui.page.worddetail.WordDetailVM
import com.payamgr.wordchest.ui.page.worddetail.WordDetailVMImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeVM::class)
    fun provideHomeViewModel(factory: HomeVMImpl.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(WordDetailVM::class)
    fun provideWordDetailViewModel(factory: WordDetailVMImpl.Factory): AssistedViewModelFactory<*, *>
}
