package com.manish.cryptoprice.data.datasource.implementations

import com.manish.cryptoprice.data.datasource.interfaces.CoinLocalDatasource
import com.manish.cryptoprice.data.db.CoinsDao
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails

class CoinsLocalDataSourceImpl(private val coinsDao: CoinsDao) : CoinLocalDatasource {
    override suspend fun saveCoinsList(list: List<CoinsListItem>) {
        coinsDao.saveCoinsList(list)
    }

    override suspend fun getCoinsList(): List<CoinsListItem>? {
        return coinsDao.getCoinsList()
    }

    override suspend fun saveHourlyPrices24Hours(id: String, graphValues: GraphValues) {
        graphValues.id = id
        coinsDao.funSaveGraphValues(graphValues)
    }

    override suspend fun getHourlyPrices24Hours(id: String): GraphValues? {
        return coinsDao.getGraphValues(id)
    }

    override suspend fun saveCoinDetails(coinDetails: CoinDetails) {
        coinsDao.funSaveCoinDetails(coinDetails)
    }

    override suspend fun getCoinDetails(id: String): CoinDetails? {
        return coinsDao.getCoinDetails(id)
    }
}