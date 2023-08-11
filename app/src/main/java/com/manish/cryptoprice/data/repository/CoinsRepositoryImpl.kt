package com.manish.cryptoprice.data.repository

import android.util.Log
import com.manish.cryptoprice.data.model.ApiResponse
import com.manish.cryptoprice.data.repository.datasource.interfaces.CoinsWebDataSource
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails
import com.manish.cryptoprice.data.model.description.Description
import com.manish.cryptoprice.data.model.description.Image
import com.manish.cryptoprice.domain.repository.CoinsRepository
import com.manish.cryptoprice.presentation.utils.Utility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.log

class CoinsRepositoryImpl(
    private val coinsWebDataSource: CoinsWebDataSource
) : CoinsRepository {
    override suspend fun getCoinsList(sortBy: String): Flow<ApiResponse> {
        return flow {
            emit(getCoinListFromWeb(sortBy))
        }
    }

    private suspend fun getCoinListFromWeb(sortBy: String): ApiResponse {
        val response: Response<CoinsList>
        try {
            response = coinsWebDataSource.getCoinsList(
                "usd",
                sortBy,
                100,
                1,
                true,
                "en"
            )
        } catch (e: SocketTimeoutException) {
            return ApiResponse(null, "No Internet!", false)
        } catch (e: UnknownHostException) {
            return ApiResponse(null, "No Internet!", false)
        } catch (e: Exception) {
            var msg = e.message
            if (msg == null)
                msg = "Unknown error occurred!"
            return ApiResponse(null, msg, false)
        }

        val body = response.body()
        if (body != null) {
            responseFromServerOrCache(response)
            return ApiResponse(body, "", true)
        }

        // 429 -> Server limit exceeded
        Log.d("TAG", "getCoinListFromWeb: ${response.errorBody()?.string()}")
        return ApiResponse(
            null,
            response.message(),
            false
        )
    }

    private fun responseFromServerOrCache(response: Response<CoinsList>) {
        if (response.raw().cacheResponse != null && response.raw().networkResponse != null) {
            Log.d("TAG", "Mixed Response")
        } else if (response.raw().cacheResponse == null) {
            Log.d("TAG", "Network Response")
        } else {
            Log.d("TAG", "Cache Response")
        }
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