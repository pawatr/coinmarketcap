package com.pawatr.coinmarketcap.domain.usecase

import com.pawatr.coinmarketcap.data.repository.CoinRepository

class GetCoinDetailUseCase(
    private val repository: CoinRepository
) {
    suspend operator fun invoke(uuid: String) = repository.getCoinDetail(uuid)
}