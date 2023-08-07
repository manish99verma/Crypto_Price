package com.manish.cryptoprice.presentation.di

import com.manish.cryptoprice.data.datasource.interfaces.CoinLocalDatasource
import com.manish.cryptoprice.data.datasource.interfaces.CoinsCacheDataSource
import com.manish.cryptoprice.data.datasource.interfaces.CoinsWebDataSource
import com.manish.cryptoprice.data.repository.CoinsRepositoryImpl
import com.manish.cryptoprice.domain.repository.CoinsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepositoryModule(
        coinsWebDataSource: CoinsWebDataSource,
        coinsLocalDatasource: CoinLocalDatasource,
        coinsCacheDataSource: CoinsCacheDataSource
    ): CoinsRepository {
        return CoinsRepositoryImpl(coinsCacheDataSource, coinsLocalDatasource, coinsWebDataSource)
    }
}