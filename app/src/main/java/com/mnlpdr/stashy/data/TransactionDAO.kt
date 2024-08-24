package com.mnlpdr.stashy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDAO {

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Query("SELECT * FROM transactions WHERE coinTicker = :coinTicker")
    suspend fun getTransactionsForCoinTicker(coinTicker: String): List<TransactionEntity>

    @Query("SELECT SUM(amount) FROM transactions WHERE coinTicker = :coinTicker AND transactionType = 'BUY'")
    suspend fun getTotalAmountBought(coinTicker: String): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE coinTicker = :coinTicker AND transactionType = 'SELL'")
    suspend fun getTotalAmountSold(coinTicker: String): Double?

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: Int): Int
}
