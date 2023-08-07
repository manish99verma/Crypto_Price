package com.manish.cryptoprice.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails

@Database(
    entities = [GraphValues::class, CoinsListItem::class, CoinDetails::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MyTypeConverter::class)
abstract class CoinsDatabase : RoomDatabase() {
    abstract fun getCoinsDao(): CoinsDao
}