package com.manish.cryptoprice.data.datasource.interfaces

import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails
import kotlinx.coroutines.flow.Flow

interface CoinLocalDatasource {
    suspend fun saveCoinsList(list: List<CoinsListItem>)

    suspend fun getCoinsList(): List<CoinsListItem>?

    suspend fun saveHourlyPrices24Hours(id: String, graphValues: GraphValues)

    suspend fun getHourlyPrices24Hours(id: String): GraphValues?

    suspend fun saveCoinDetails(coinDetails: CoinDetails)

    suspend fun getCoinDetails(id: String): CoinDetails?
}