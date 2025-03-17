package com.pawatr.coinmarketcap.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pawatr.coinmarketcap.data.repository.CoinRepository
import com.pawatr.coinmarketcap.domain.source.CoinListPagingSource
import com.pawatr.coinmarketcap.data.model.Coin
import kotlinx.coroutines.flow.Flow

class GetCoinListUseCase(
    private val repository: CoinRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): List<Coin> {
        return repository.getCoins(limit = limit, offset =  offset)
    }

    fun invokePaging(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { CoinListPagingSource(repository, isSearch = false, startOffset = 3) }
        ).flow
    }
}