package com.manish.cryptoprice.data.model.coinsList

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "coins_list_item_table")
data class CoinsListItem(
    val ath: Double,
    val ath_change_percentage: Double,
    val ath_date: String,
    val atl: Double,
    val atl_change_percentage: Double,
    val atl_date: String,
    val circulating_supply: Double,
    val current_price: Double,
    val fully_diluted_valuation: Long,
    val high_24h: Double,

    @PrimaryKey
    val id: String,

    val image: String,
    val last_updated: String,
    val low_24h: Double,
    val market_cap: Long,
    val market_cap_change_24h: Double,
    val market_cap_change_percentage_24h: Double,
    val market_cap_rank: Int,
    val max_supply: Double,
    val name: String,
    val price_change_24h: Double,
    val price_change_percentage_24h: Double,
    var roi: Roi? = null,
    val symbol: String,
    val total_supply: Double,
    val total_volume: Double,

    //Adding Sparkline
    val sparkline_in_7d: Sparkline
)