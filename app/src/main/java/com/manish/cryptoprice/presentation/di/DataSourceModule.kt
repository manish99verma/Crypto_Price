package com.manish.cryptoprice.presentation.di

import com.manish.cryptoprice.data.api.CoinGeckoService
import com.manish.cryptoprice.data.datasource.implementations.CoinsWebDataSourceImpl
import com.manish.cryptoprice.data.datasource.interfaces.CoinsWebDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    fun provideWebDataSource(coinGeckoService: CoinGeckoService): CoinsWebDataSource {
        return CoinsWebDataSourceImpl(coinGeckoService)
    }
}