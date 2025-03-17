package com.pawatr.coinmarketcap.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pawatr.coinmarketcap.data.repository.CoinRepository
import com.pawatr.coinmarketcap.domain.source.CoinListPagingSource
import com.pawatr.coinmarketcap.data.model.Coin
import kotlinx.coroutines.flow.Flow

class SearchCoinUseCase(
    private val repository: CoinRepository
) {
    fun invokePaging(query: String): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { CoinListPagingSource(repository, isSearch = true, query = query) }
        ).flow
    }
}