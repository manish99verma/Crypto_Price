package com.manish.cryptoprice.domain.use_case

import com.manish.cryptoprice.domain.repository.CoinsRepository

class GetCoinsListUseCase(private val coinsRepository: CoinsRepository) {
    suspend fun execute() = coinsRepository.getCoinsList()
}