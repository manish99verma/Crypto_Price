package com.manish.cryptoprice.data.datasource.implementations

import android.util.Log
import com.manish.cryptoprice.data.datasource.interfaces.CoinsCacheDataSource
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails

class CoinsCacheDataSourceImpl : CoinsCacheDataSource {
    private var coinsList: List<CoinsListItem>? = null
    private var hourlyPrices24Hours: MutableMap<String, GraphValues> = mutableMapOf()
    private var coinDetailsMap: MutableMap<String, CoinDetails> = mutableMapOf()

    override fun saveCoinsList(list: List<CoinsListItem>) {
        coinsList = list
    }

    override fun getCoinsList(): List<CoinsListItem>? {
        return coinsList
    }

    override fun saveHourlyPrices24Hours(id: String, graphValues: GraphValues) {
        hourlyPrices24Hours[id] = graphValues
    }

    override fun getHourlyPrices24Hours(id: String): GraphValues? {
        return hourlyPrices24Hours[id]
    }

    override fun saveCoinDetails(id: String, coinDetails: CoinDetails) {
        coinDetailsMap[id] = coinDetails
    }

    override fun getCoinDetails(id: String): CoinDetails? {
        return coinDetailsMap[id]
    }


}