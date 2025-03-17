package com.pawatr.coinmarketcap.data.model.response

import com.squareup.moshi.Json

data class CoinRankingResponse(
    val status: String,
    val data: CoinRankingData
)

data class CoinRankingData(
    val stats: Stats,
    val coins: List<CoinDataItem>
)

data class Stats(
    val total: Int?,
    val totalCoins: Int?,
    val totalMarkets: Int?,
    val totalExchanges: Int?,
    val totalMarketCap: String?,
    val total24hVolume: String?
)

data class CoinDataItem(
    val uuid: String,
    val symbol: String?,
    val name: String?,
    val color: String?,
    val iconUrl: String?,
    val marketCap: String?,
    val price: String?,
    val listedAt: Int?,
    val tier: Int?,
    val change: String?,
    val rank: Int,
    val sparkline: List<String?>?,
    val lowVolume: Boolean?,
    val coinrankingUrl: String?,
    @Json(name = "24hVolume")
    val volume24h: String?,
    val btcPrice: String?,
    val contractAddresses: List<String>?
)