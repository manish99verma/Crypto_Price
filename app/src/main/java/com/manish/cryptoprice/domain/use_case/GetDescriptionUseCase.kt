package com.manish.cryptoprice.domain.use_case

import com.manish.cryptoprice.domain.repository.CoinsRepository

class GetDescriptionUseCase(private val repository: CoinsRepository) {
    suspend fun execute(id:String) = repository.getCoinDetails(id)
}