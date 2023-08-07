package com.manish.cryptoprice.presentation.di

import com.manish.cryptoprice.domain.repository.CoinsRepository
import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetCoinsListUseCase
import com.manish.cryptoprice.domain.use_case.GetDescriptionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UsecaseModule {
    @Provides
    @Singleton
    fun provideGetCoinsListUseCase(repository: CoinsRepository): GetCoinsListUseCase {
        return GetCoinsListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGet24HoursPricesUseCase(repository: CoinsRepository): Get24HoursHourlyPricesListUseCase {
        return Get24HoursHourlyPricesListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGetDescriptionUseCaseUseCase(repository: CoinsRepository): GetDescriptionUseCase {
        return GetDescriptionUseCase(repository)
    }

}