package com.mnlpdr.stashy.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinGeckoApiService {
    @GET("coins/{id}")
    suspend fun getCoinDetails(
        @Path("id") id: String
    ): CoinGeckoResponse

    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3/"

        fun create(): CoinGeckoApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinGeckoApiService::class.java)
        }
    }
}

data class CoinGeckoResponse(
    val id: String,
    val symbol: String,
    val name: String,
    val image: CoinGeckoImage
)

data class CoinGeckoImage(
    val thumb: String,
    val small: String,
    val large: String
)