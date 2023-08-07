package com.manish.cryptoprice.domain.repository

import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.model.description.CoinDetails
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {
    /*    suspend fun getCoinsList(
            vs_currency: String,
            ids: String,
            category: String,
            order: String,
            per_page: Int,
            page: Int,
            sparkLine: Boolean,
            price_change_percentage: String,
            locale: String,
            precision: String
        ): Flow<CoinsList>*/

    suspend fun getCoinsList(): Flow<List<CoinsListItem>>

    suspend fun get24hrsHourlyPrices(id: String): Flow<GraphValues>

    suspend fun getCoinDetails(id: String): Flow<CoinDetails>
}