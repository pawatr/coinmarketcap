package com.pawatr.coinmarketcap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pawatr.coinmarketcap.domain.usecase.GetCoinListUseCase
import com.pawatr.coinmarketcap.domain.usecase.SearchCoinUseCase
import com.pawatr.coinmarketcap.data.model.Coin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val getCoinListUseCase: GetCoinListUseCase,
    private val searchCoinUseCase: SearchCoinUseCase
) : ViewModel() {

    private val _refreshTopRank = MutableSharedFlow<Unit>()
    val refreshTopRank: SharedFlow<Unit> = _refreshTopRank.asSharedFlow()

    val topRankCoins: Flow<List<Coin>> = _refreshTopRank.flatMapLatest {
        flow {
            val coins = getCoinListUseCase.invoke(offset = 0, limit = 3)
            emit(coins)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val searchQuery = MutableStateFlow("")

    val coinList: Flow<PagingData<Coin>> = searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                getCoinListUseCase.invokePaging().cachedIn(viewModelScope)
            } else {
                searchCoinUseCase.invokePaging(query).cachedIn(viewModelScope)
            }
        }
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun refreshTopRankCoins() {
        viewModelScope.launch {
            _refreshTopRank.emit(Unit)
        }
    }
}