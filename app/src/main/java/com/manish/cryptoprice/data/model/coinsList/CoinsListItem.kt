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
    val fully_diluted_valuation: Double,
    val high_24h: Double,

    @PrimaryKey
    val id: String,

    val image: String,
    val last_updated: String,
    val low_24h: Double,
    val market_cap: Double,
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


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoinsListItem

        if (ath != other.ath) return false
        if (ath_change_percentage != other.ath_change_percentage) return false
        if (ath_date != other.ath_date) return false
        if (atl != other.atl) return false
        if (atl_change_percentage != other.atl_change_percentage) return false
        if (atl_date != other.atl_date) return false
        if (circulating_supply != other.circulating_supply) return false
        if (current_price != other.current_price) return false
        if (fully_diluted_valuation != other.fully_diluted_valuation) return false
        if (high_24h != other.high_24h) return false
        if (id != other.id) return false
        if (image != other.image) return false
        if (last_updated != other.last_updated) return false
        if (low_24h != other.low_24h) return false
        if (market_cap != other.market_cap) return false
        if (market_cap_change_24h != other.market_cap_change_24h) return false
        if (market_cap_change_percentage_24h != other.market_cap_change_percentage_24h) return false
        if (market_cap_rank != other.market_cap_rank) return false
        if (max_supply != other.max_supply) return false
        if (name != other.name) return false
        if (price_change_24h != other.price_change_24h) return false
        if (price_change_percentage_24h != other.price_change_percentage_24h) return false
        if (roi != other.roi) return false
        if (symbol != other.symbol) return false
        if (total_supply != other.total_supply) return false
        if (total_volume != other.total_volume) return false
        if (sparkline_in_7d != other.sparkline_in_7d) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ath.hashCode()
        result = 31 * result + ath_change_percentage.hashCode()
        result = 31 * result + ath_date.hashCode()
        result = 31 * result + atl.hashCode()
        result = 31 * result + atl_change_percentage.hashCode()
        result = 31 * result + atl_date.hashCode()
        result = 31 * result + circulating_supply.hashCode()
        result = 31 * result + current_price.hashCode()
        result = 31 * result + fully_diluted_valuation.hashCode()
        result = 31 * result + high_24h.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + last_updated.hashCode()
        result = 31 * result + low_24h.hashCode()
        result = 31 * result + market_cap.hashCode()
        result = 31 * result + market_cap_change_24h.hashCode()
        result = 31 * result + market_cap_change_percentage_24h.hashCode()
        result = 31 * result + market_cap_rank
        result = 31 * result + max_supply.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + price_change_24h.hashCode()
        result = 31 * result + price_change_percentage_24h.hashCode()
        result = 31 * result + (roi?.hashCode() ?: 0)
        result = 31 * result + symbol.hashCode()
        result = 31 * result + total_supply.hashCode()
        result = 31 * result + total_volume.hashCode()
        result = 31 * result + sparkline_in_7d.hashCode()
        return result
    }
}