package com.pawatr.coinmarketcap.data.repository

import com.pawatr.coinmarketcap.data.model.Coin
import com.pawatr.coinmarketcap.data.model.CoinDetail
import com.pawatr.coinmarketcap.data.model.toCoin
import com.pawatr.coinmarketcap.data.model.toCoinDetail
import com.pawatr.coinmarketcap.data.remote.ApiService

interface CoinRepository {
    suspend fun getCoins(limit: Int, offset: Int): List<Coin>
    suspend fun getCoinsSearch(keyword: String, limit: Int, offset: Int): List<Coin>
    suspend fun getCoinDetail(uuid: String): CoinDetail
}

class CoinRepositoryImpl(private val coinService: ApiService) : CoinRepository {
    override suspend fun getCoins(limit: Int, offset: Int): List<Coin> {
        return coinService.getCoins(limit = limit, offset = offset).data.coins.map { it.toCoin() }
    }

    override suspend fun getCoinsSearch(keyword: String, limit: Int, offset: Int): List<Coin> {
        return coinService.getCoinsSearch(search = keyword, limit =  limit, offset = offset).data.coins.map { it.toCoin() }
    }

    override suspend fun getCoinDetail(uuid: String): CoinDetail {
        return coinService.getCoinDetail(uuid = uuid).data.coin.toCoinDetail()
    }
}