package com.manish.cryptoprice.data.datasource.interfaces

import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails

interface CoinsCacheDataSource {
    fun saveCoinsList(list: List<CoinsListItem>)

    fun getCoinsList(): List<CoinsListItem>?

    fun saveHourlyPrices24Hours(id: String, graphValues: GraphValues)

    fun getHourlyPrices24Hours(id: String): GraphValues?

    fun saveCoinDetails(id: String, coinDetails: CoinDetails)

    fun getCoinDetails(id: String): CoinDetails?
}
