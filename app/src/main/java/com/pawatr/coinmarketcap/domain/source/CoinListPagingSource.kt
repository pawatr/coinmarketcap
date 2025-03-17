package com.pawatr.coinmarketcap.domain.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pawatr.coinmarketcap.data.repository.CoinRepository
import com.pawatr.coinmarketcap.data.model.Coin

class CoinListPagingSource(
    private val repository: CoinRepository,
    private val isSearch: Boolean,
    private val query: String? = null,
    private val startOffset: Int = 0
) : PagingSource<Int, Coin>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize
            val offset = (page * limit) + startOffset

            val coins = if (isSearch) {
                repository.getCoinsSearch(query ?: "", limit, offset)
            } else {
                repository.getCoins(limit, offset)
            }

            LoadResult.Page(
                data = coins,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (coins.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}