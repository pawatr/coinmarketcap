package com.pawatr.coinmarketcap.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawatr.coinmarketcap.data.model.CoinDetail
import com.pawatr.coinmarketcap.domain.model.Result
import com.pawatr.coinmarketcap.domain.usecase.GetCoinDetailUseCase
import kotlinx.coroutines.launch

class CoinDetailViewModel(
    private val coinDetailUseCase: GetCoinDetailUseCase
): ViewModel() {

    private val _coinDetail = MutableLiveData<Result<CoinDetail>>()
    val coinDetail: LiveData<Result<CoinDetail>> = _coinDetail

    fun getCoinDetail(uuid: String) {
        viewModelScope.launch {
            _coinDetail.value = Result.Loading
            try {
                val response = coinDetailUseCase(uuid = uuid)
                _coinDetail.value = Result.Success(response)
            } catch (e: Exception) {
                _coinDetail.value = Result.Error(e)
            }
        }
    }
}