package com.pawatr.coinmarketcap.data.model.response

import com.squareup.moshi.Json

data class CoinDetailDataResponse(
    @Json(name = "data") val data: CoinDetailResponse
)

data class CoinDetailResponse(
    @Json(name = "coin") val coin: CoinDataResponse
)

data class CoinDataResponse(
    @Json(name = "uuid") val uuid: String,
    @Json(name = "symbol") val symbol: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "color") val color: String?,
    @Json(name = "iconUrl") val iconUrl: String?,
    @Json(name = "websiteUrl") val websiteUrl: String?,
    @Json(name = "links") val links: List<Link>,
    @Json(name = "supply") val supply: Supply,
    @Json(name = "numberOfMarkets") val numberOfMarkets: Int,
    @Json(name = "numberOfExchanges") val numberOfExchanges: Int,
    @Json(name = "24hVolume") val volume24h: String,
    @Json(name = "marketCap") val marketCap: String,
    @Json(name = "fullyDilutedMarketCap") val fullyDilutedMarketCap: String,
    @Json(name = "price") val price: String?,
    @Json(name = "btcPrice") val btcPrice: String?,
    @Json(name = "priceAt") val priceAt: Long,
    @Json(name = "change") val change: String,
    @Json(name = "rank") val rank: Int,
    @Json(name = "sparkline") val sparkline: List<String?>,
    @Json(name = "allTimeHigh") val allTimeHigh: AllTimeHigh,
    @Json(name = "coinrankingUrl") val coinrankingUrl: String,
    @Json(name = "tier") val tier: Int,
    @Json(name = "lowVolume") val lowVolume: Boolean,
    @Json(name = "listedAt") val listedAt: Long,
    @Json(name = "hasContent") val hasContent: Boolean,
    @Json(name = "notices") val notices: Any?,
    @Json(name = "contractAddresses") val contractAddresses: List<String>?,
    @Json(name = "tags") val tags: List<String>
)

data class Link(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String,
    @Json(name = "type") val type: String
)

data class Supply(
    @Json(name = "confirmed") val confirmed: Boolean,
    @Json(name = "supplyAt") val supplyAt: Long,
    @Json(name = "max") val max: String?,
    @Json(name = "total") val total: String,
    @Json(name = "circulating") val circulating: String
)

data class AllTimeHigh(
    @Json(name = "price") val price: String,
    @Json(name = "timestamp") val timestamp: Long
)