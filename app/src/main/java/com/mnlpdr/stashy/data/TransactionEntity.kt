package com.mnlpdr.stashy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bagId: Long,
    val cryptoName: String,
    val coinTicker: String,
    val amount: Double,
    val pricePerUnit: Double,
    val date: Date,
    val transactionType: TransactionType

    )

enum class TransactionType {
    BUY, SELL
}