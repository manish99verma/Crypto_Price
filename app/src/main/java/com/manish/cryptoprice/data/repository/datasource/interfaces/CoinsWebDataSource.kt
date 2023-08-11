package com.manish.cryptoprice.data.repository.datasource.interfaces

import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.description.CoinDetails
import retrofit2.Response


interface CoinsWebDataSource {
    suspend fun getCoinsList(
        vs_currency: String,
        order: String,
        per_page: Int,
        page: Int,
        sparkLine: Boolean,
        locale: String
    ): Response<CoinsList>

    suspend fun getHistoryChart(
        id: String,
        vs_currency: String,
        days: String,
        interval: String,
    ): Response<GraphValues>

    suspend fun getCoinDetails(
        id: String,
        localization: String,
        tickers: Boolean,
        marketData: Boolean,
        communityData: Boolean,
        developer_data: Boolean,
        sparkLine: Boolean
    ): Response<CoinDetails>
}