package com.manish.cryptoprice.presentation.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.manish.cryptoprice.data.db.CoinsDao
import com.manish.cryptoprice.data.db.CoinsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providesCoinsDatabase(@ApplicationContext context: Context): CoinsDatabase {
        return Room.databaseBuilder(context, CoinsDatabase::class.java, "my_coin_database")
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinsDao(coinsDatabase: CoinsDatabase): CoinsDao {
        return coinsDatabase.getCoinsDao()
    }
}