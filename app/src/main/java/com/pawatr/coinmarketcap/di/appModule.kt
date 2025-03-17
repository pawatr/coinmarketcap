package com.pawatr.coinmarketcap.di

import com.pawatr.coinmarketcap.data.remote.ApiService
import com.pawatr.coinmarketcap.data.remote.ServiceFactory
import com.pawatr.coinmarketcap.data.repository.CoinRepository
import com.pawatr.coinmarketcap.data.repository.CoinRepositoryImpl
import com.pawatr.coinmarketcap.domain.usecase.GetCoinDetailUseCase
import com.pawatr.coinmarketcap.domain.usecase.GetCoinListUseCase
import com.pawatr.coinmarketcap.domain.usecase.SearchCoinUseCase
import com.pawatr.coinmarketcap.ui.detail.CoinDetailViewModel
import com.pawatr.coinmarketcap.ui.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ApiService
    single<ApiService> { ServiceFactory.create(isDebug = true) }

    // CoinRepository
    single<CoinRepository> { CoinRepositoryImpl(get()) }

    // UseCase
    single { GetCoinListUseCase(get()) }
    single { SearchCoinUseCase(get()) }
    single { GetCoinDetailUseCase(get()) }

    // ViewModel
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CoinDetailViewModel(get()) }
}