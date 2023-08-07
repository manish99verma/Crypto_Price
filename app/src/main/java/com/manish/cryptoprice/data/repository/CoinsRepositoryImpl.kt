package com.manish.cryptoprice.data.repository

import android.util.Log
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
    private val coinsWebDataSource: CoinsWebDataSource
) : CoinsRepository {
    override suspend fun getCoinsList(): Flow<List<CoinsListItem>> {
        return flow {
            emit(getCoinListFromWeb())
        }
    }


    private suspend fun getCoinListFromWeb(): List<CoinsListItem> {
        val response = coinsWebDataSource.getCoinsList(
            "usd",
            "market_cap_desc",
            100,
            1,
            true,
            "en"
        )

        val body = response.body()
        if (body != null) {
            Log.d("TAG", "getCoinListFromWeb: ")
            return body
        }
        return emptyList()
    }

    override suspend fun getDailyPriceChart(id: String): Flow<GraphValues> {
        return flow {
            emit(getHistoryChartFromWeb(id))
        }
    }

    private suspend fun getHistoryChartFromWeb(id: String): GraphValues {
        val response = coinsWebDataSource.getHistoryChart(
            id,
            "usd",
            "365",
            "daily",
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
            emit(getCoinsDetailsFromWeb(id))
        }
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