package com.manish.cryptoprice.data.api

import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.description.CoinDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoService {
    @GET("coins/markets")
    suspend fun getCoinsList(
        @Query("vs_currency")
        vs_currency: String,
        @Query("order")
        order: String,
        @Query("per_page")
        per_page: Int,
        @Query("page")
        page: Int,
        @Query("locale")
        locale: String
    ): Response<CoinsList>

    @GET("coins/{id}/market_chart")
    suspend fun getHistoryChart(
        @Path("id")
        id: String,
        @Query("vs_currency")
        vs_currency: String,
        @Query("days")
        days: String,
        @Query("interval")
        interval: String
    ): Response<GraphValues>

    @GET("coins/{id}")
    suspend fun coinDetails(
        @Path("id")
        id: String,
        @Query("localization")
        localization: String,
        @Query("tickers")
        tickers: Boolean,
        @Query("market_data")
        marketData: Boolean,
        @Query("community_data")
        communityData: Boolean,
        @Query("developer_data")
        developer_data: Boolean,
        @Query("sparkline")
        sparkLine: Boolean
    ): Response<CoinDetails>
}