package com.mnlpdr.stashy.data

data class Transaction (
    val id: String = "",
    val coinTicker: String,
    val cryptoName: String,
    val transactionType: TransactionType,
    val quantity: Double,
    val pricePerUnit: Double,
    val timestamp: Long = System.currentTimeMillis()

)
