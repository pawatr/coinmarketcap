package com.pawatr.coinmarketcap.data.remote

import com.pawatr.coinmarketcap.data.model.response.CoinDetailDataResponse
import com.pawatr.coinmarketcap.data.model.response.CoinRankingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/coins")
    suspend fun getCoins(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CoinRankingResponse

    @GET("/v2/coins")
    suspend fun getCoinsSearch(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String
    ): CoinRankingResponse

    @GET("v2/coin/{uuid}")
    suspend fun getCoinDetail(
        @Path("uuid") uuid: String
    ): CoinDetailDataResponse
}