package com.mnlpdr.stashy.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinCapApiService {
    @GET("v2/assets")
    suspend fun getCryptoPrices(
        @Query("limit") limit: Int = 10
    ): CoinCapResponse

    companion object {
        private const val BASE_URL = "https://api.coincap.io/"

        fun create(apiKey: String): CoinCapApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $apiKey")
                        .build()
                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinCapApiService::class.java)
        }
    }
}

data class CoinCapResponse(
    val data: List<CoinData>
)

data class CoinData(
    val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: String
)