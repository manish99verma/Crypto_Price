package com.manish.cryptoprice.data.model.coinsList

data class Sparkline(
    val price: ArrayList<Double>


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sparkline

        if (price.size != other.price.size) return false
        (0 until price.size).forEach { i ->
            if (other.price[i] != price[i])
                return false
        }

        return true
    }

    override fun hashCode(): Int {
        return price.hashCode()
    }
}