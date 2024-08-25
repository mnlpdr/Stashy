package com.mnlpdr.stashy.data

data class CryptoPrice (
    val coinTicker: String,
    val cryptoName: String,
    val quantity: Double,
    val currentPrice: Double,
    val iconUrl: String? = null
    )

