package com.pawatr.coinmarketcap.data.remote

import android.util.Log
import com.pawatr.coinmarketcap.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceFactory {

    private const val API_KEY_HEADER_NAME = "x-access-token"
    private const val OK_HTTP_TIMEOUT = 60L
    private const val TAG = "ServiceFactory"

    fun create(isDebug: Boolean): ApiService {
        val retrofit = createRetrofit(isDebug)
        return retrofit.create(ApiService::class.java)
    }

    private fun createRetrofit(isDebug: Boolean): Retrofit {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST_NAME)
            .client(createOkHttpClient(createLoggingInterceptor(isDebug)))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun createOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createApiKeyInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(createResponseLoggingInterceptor())
            .connectTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun createApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(API_KEY_HEADER_NAME, BuildConfig.API_KEY)
                .build()
            chain.proceed(request)
        }
    }

    private fun createLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (isDebug) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    private fun createResponseLoggingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            val responseBodyString = response.peekBody(Long.MAX_VALUE).string()
            Log.i(TAG, "Response: ${response.code} - ${request.url} \n $responseBodyString")
            response
        }
    }
}