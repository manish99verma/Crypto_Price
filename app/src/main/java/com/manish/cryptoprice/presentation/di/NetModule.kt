package com.manish.cryptoprice.presentation.di

import com.manish.cryptoprice.BuildConfig
import com.manish.cryptoprice.data.api.CoinGeckoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideCryptoServiceApi(retrofit: Retrofit): CoinGeckoService {
        return retrofit.create(CoinGeckoService::class.java)
    }

}