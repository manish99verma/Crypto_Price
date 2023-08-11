package com.manish.cryptoprice.domain.use_case

import com.manish.cryptoprice.domain.repository.CoinsRepository

class Get24HoursHourlyPricesListUseCase(private val repository: CoinsRepository) {
    suspend fun execute(id:String) = repository.getDailyPriceChart(id)
}