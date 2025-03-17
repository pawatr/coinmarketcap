package com.pawatr.coinmarketcap.data.model

import com.pawatr.coinmarketcap.data.model.response.CoinDataItem
import java.util.UUID

sealed class CoinItem {
    data class HeaderItem(val text: String, val description: String = "") : CoinItem()
    data class CoinData(val coin: Coin) : CoinItem()
    data class InviteItem(val text: String, val description: String = "") : CoinItem()
}

data class Coin(
    val uuid: String,
    val name: String,
    val price: String,
    val url: String,
    val symbol: String,
    val rank: Int,
    val change: String,
    val color: String
)

fun CoinDataItem.toCoin(): Coin {
    return Coin(
        uuid = this.uuid,
        name = this.name ?: "",
        price = this.price ?: "0",
        url = this.iconUrl ?: "",
        symbol = this.symbol ?: "",
        rank = this.rank,
        change = this.change ?: "0",
        color = this.color ?: ""
    )
}