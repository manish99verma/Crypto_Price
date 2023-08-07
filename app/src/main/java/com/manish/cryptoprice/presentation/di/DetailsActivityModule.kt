package com.manish.cryptoprice.presentation.di

import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetDescriptionUseCase
import com.manish.cryptoprice.presentation.ui.view_models.DetailsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class DetailsActivityModule {
    @Provides
    @ActivityScoped
    fun provideDetailsActivityFactory(
        getDescriptionUseCase: GetDescriptionUseCase,
        get24HoursHourlyPricesListUseCase: Get24HoursHourlyPricesListUseCase
    ): DetailsViewModelFactory {
        return DetailsViewModelFactory(getDescriptionUseCase, get24HoursHourlyPricesListUseCase)
    }
}