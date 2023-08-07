package com.manish.cryptoprice.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface CoinsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoinsList(coinsList: List<CoinsListItem>)

    @Query("SELECT * FROM coins_list_item_table")
   suspend fun getCoinsList(): List<CoinsListItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun funSaveGraphValues(graphValues: GraphValues)

    @Query("SELECT * FROM graph_values_table WHERE id =:id")
   suspend fun getGraphValues(id: String): GraphValues?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun funSaveCoinDetails(coinDetails: CoinDetails)

    @Query("SELECT * FROM coin_details_table WHERE id =:id")
    suspend fun getCoinDetails(id: String): CoinDetails?
}