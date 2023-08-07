package com.manish.cryptoprice.data.model.chart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "graph_values_table")
data class GraphValues(
    val market_caps: List<List<Double>>,
    val prices: List<List<Double>>,
    val total_volumes: List<List<Double>>,

    @PrimaryKey
    var id: String = "",
)