package com.pawatr.coinmarketcap.data.model

import com.pawatr.coinmarketcap.data.model.response.CoinDataResponse


class CoinDetail (
    val uuid: String,
    val name: String,
    val price: String,
    val detail: String,
    val url: String,
    val symbol: String,
    val color: String,
    val marketCap: String,
    val iconUrl: String
)

fun CoinDataResponse?.toCoinDetail(): CoinDetail {
    return CoinDetail(
        uuid = this?.uuid?: "",
        name = this?.name ?: "",
        price = this?.price ?: "",
        detail = this?.description ?: "" ,
        url = this?.websiteUrl ?: "",
        symbol = this?.symbol ?: "",
        color = this?.color ?: "",
        marketCap = this?.marketCap ?: "",
        iconUrl = this?.iconUrl ?: ""
    )
}