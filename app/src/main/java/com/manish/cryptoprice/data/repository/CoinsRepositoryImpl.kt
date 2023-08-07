package com.manish.cryptoprice.data.repository

import android.util.Log
import com.manish.cryptoprice.data.datasource.interfaces.CoinLocalDatasource
import com.manish.cryptoprice.data.datasource.interfaces.CoinsCacheDataSource
import com.manish.cryptoprice.data.datasource.interfaces.CoinsWebDataSource
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails
import com.manish.cryptoprice.data.model.description.Description
import com.manish.cryptoprice.data.model.description.Image
import com.manish.cryptoprice.domain.repository.CoinsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoinsRepositoryImpl(
    private val coinsCacheDataSource: CoinsCacheDataSource,
    private val coinsLocalDataSource: CoinLocalDatasource,
    private val coinsWebDataSource: CoinsWebDataSource
) : CoinsRepository {
    override suspend fun getCoinsList(): Flow<List<CoinsListItem>> {
        return flow {
            emit(getCoinListFromCache())
        }
    }

    private suspend fun getCoinListFromCache(): List<CoinsListItem> {
        var cacheList: List<CoinsListItem>? = coinsCacheDataSource.getCoinsList()
        if (cacheList.isNullOrEmpty()) {
            cacheList = getCoinListFromDatabase()
            coinsCacheDataSource.saveCoinsList(cacheList)
        } else {
            Log.d("TAG", "getCoinListFromCache: ")
        }
        return cacheList
    }

    private suspend fun getCoinListFromDatabase(): List<CoinsListItem> {
        var lis: List<CoinsListItem>? = coinsLocalDataSource.getCoinsList()
        if (lis.isNullOrEmpty()) {
            lis = getCoinListFromWeb()
            coinsLocalDataSource.saveCoinsList(lis)
        } else {
            Log.d("TAG", "getCoinListFromDatabase: ")
        }
        return lis
    }

    private suspend fun getCoinListFromWeb(): List<CoinsListItem> {
        val response = coinsWebDataSource.getCoinsList(
            "usd",
            "market_cap_desc",
            100,
            1,
            "en"
        )

        val body = response.body()
        if (body != null) {
            Log.d("TAG", "getCoinListFromWeb: ")
            return body
        }
        return emptyList()
    }

    override suspend fun get24hrsHourlyPrices(id: String): Flow<GraphValues> {
        return flow {
            emit(getHistoryChartFromCache(id))
        }
    }

    private suspend fun getHistoryChartFromCache(id: String): GraphValues {
        var lis = coinsCacheDataSource.getHourlyPrices24Hours(id)
        if (lis == null || lis.prices.isEmpty()) {
            lis = getHistoryChartFromDatabase(id)
            coinsCacheDataSource.saveHourlyPrices24Hours(id, lis)
        } else {
            Log.d("Chart", "getHistoryChartFromCache: $id")
        }
        return lis
    }

    private suspend fun getHistoryChartFromDatabase(id: String): GraphValues {
        var lis = coinsLocalDataSource.getHourlyPrices24Hours(id)
        if (lis == null || lis.prices.isEmpty()) {
            lis = getHistoryChartFromWeb(id)
            coinsLocalDataSource.saveHourlyPrices24Hours(id, lis)
        } else {
            Log.d("Chart", "getHistoryChartFromDatabase: $id")
        }
        return lis
    }

    private suspend fun getHistoryChartFromWeb(id: String): GraphValues {
        val response = coinsWebDataSource.getHistoryChart(
            id,
            "usd",
            "1",
            "hourly",
        )

        val body = response.body()
        if (body != null) {
            Log.d("TAG", "getHistoryChartFromWeb: $id")
            return body
        } else {
            Log.d("TAG", "getHistoryChartFromWeb: Null")
        }

        return GraphValues(emptyList(), emptyList(), emptyList())
    }

    override suspend fun getCoinDetails(id: String): Flow<CoinDetails> {
        return flow {
            emit(getCoinDetailsFromCache(id))
        }
    }

    private suspend fun getCoinDetailsFromCache(id: String): CoinDetails {
        var details = coinsCacheDataSource.getCoinDetails(id)
        if (details == null || details.market_cap_rank == -1) {
            details = getCoinDetailsFromDatabase(id)
            coinsCacheDataSource.saveCoinDetails(id, details)
        }else{
            Log.d("TAG", "getCoinDetailsFromCache: ")
        }
        return details
    }

    private suspend fun getCoinDetailsFromDatabase(id: String): CoinDetails {
        var details = coinsLocalDataSource.getCoinDetails(id)
        if (details == null || details.market_cap_rank == -1) {
            details = getCoinsDetailsFromWeb(id)
            coinsLocalDataSource.saveCoinDetails(details)
        }else{
            Log.d("TAG", "getCoinDetailsFromDatabase: ")
        }
        return details
    }

    private suspend fun getCoinsDetailsFromWeb(id: String): CoinDetails {
        val response = coinsWebDataSource.getCoinDetails(
            id,
            "false",
            false,
            false,
            false,
            false,
            false
        )

        val body = response.body()
        if (body != null) {
            Log.d("TAG", "getCoinsDetailsFromWeb: ")
            return response.body()!!
        }
        return CoinDetails(
            Description("Unable to fetch data from server, Please get PREMIUM SERVER!"),
            id,
            Image("large.jpg", "small.jpg", "thumb.jpg"),
            "Never",
            -1,
            "Unknown"
        )
    }
}