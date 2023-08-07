package com.manish.cryptoprice.presentation.di


import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetCoinsListUseCase
import com.manish.cryptoprice.presentation.ui.adapters.CryptoListAdapter
import com.manish.cryptoprice.presentation.ui.view_models.MainViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class MainActivityModule {
    @Provides
    @ActivityScoped
    fun provideMainFactory(
        getCoinsListUseCase: GetCoinsListUseCase,
        get24HoursHourlyPricesListUseCase: Get24HoursHourlyPricesListUseCase
    ): MainViewModelFactory {
        return MainViewModelFactory(getCoinsListUseCase, get24HoursHourlyPricesListUseCase)
    }

    @Provides
    @ActivityScoped
    fun provideAdapter():CryptoListAdapter{
        return CryptoListAdapter()
    }
}