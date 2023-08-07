package com.manish.cryptoprice.data.model.description

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_details_table")
data class CoinDetails(
    val description: Description,

    @PrimaryKey
    val id: String,

    val image: Image,
    val last_updated: String,
    val market_cap_rank: Int,
    val name: String,
)