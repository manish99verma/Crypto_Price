package com.manish.cryptoprice.data.repository.datasource.implementations

import com.manish.cryptoprice.data.api.CoinGeckoService
import com.manish.cryptoprice.data.repository.datasource.interfaces.CoinsWebDataSource
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.description.CoinDetails
import retrofit2.Response

class CoinsWebDataSourceImpl(private val coinGeckoService: CoinGeckoService) : CoinsWebDataSource {
    override suspend fun getCoinsList(
        vs_currency: String,
        order: String,
        per_page: Int,
        page: Int,
        sparkLine: Boolean,
        locale: String,
    ): Response<CoinsList> {
        return coinGeckoService.getCoinsList(
            vs_currency = vs_currency,
            order = order,
            per_page = per_page,
            page = page,
            locale = locale,
            sparkLine = true
        )
    }

    override suspend fun getHistoryChart(
        id: String,
        vs_currency: String,
        days: String,
        interval: String,
    ): Response<GraphValues> {
        return coinGeckoService.getHistoryChart(
            id,
            vs_currency,
            days,
            interval
        )
    }

    override suspend fun getCoinDetails(
        id: String,
        localization: String,
        tickers: Boolean,
        marketData: Boolean,
        communityData: Boolean,
        developer_data: Boolean,
        sparkLine: Boolean
    ): Response<CoinDetails> {
        return coinGeckoService.coinDetails(
            id,
            localization,
            tickers,
            marketData,
            communityData,
            developer_data,
            sparkLine
        )
    }
}