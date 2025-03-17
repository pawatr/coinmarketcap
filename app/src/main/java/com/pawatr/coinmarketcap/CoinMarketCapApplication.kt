package com.pawatr.coinmarketcap

import android.app.Application
import com.pawatr.coinmarketcap.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CoinMarketCapApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoinMarketCapApplication)
            modules(
                appModule
            )
        }
    }
}