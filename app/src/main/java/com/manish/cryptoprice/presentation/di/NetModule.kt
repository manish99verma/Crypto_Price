package com.manish.cryptoprice.presentation.di

import android.content.Context
import com.manish.cryptoprice.BuildConfig
import com.manish.cryptoprice.data.api.CoinGeckoService
import com.manish.cryptoprice.presentation.utils.CacheInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient().newBuilder()
            .cache(Cache(File(context.cacheDir, "http-cache"), 10L * 1024L * 1024L))//10 MiB
            .addNetworkInterceptor(CacheInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideCryptoServiceApi(retrofit: Retrofit): CoinGeckoService {
        return retrofit.create(CoinGeckoService::class.java)
    }

}