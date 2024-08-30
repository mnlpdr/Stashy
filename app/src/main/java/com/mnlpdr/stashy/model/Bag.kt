package com.mnlpdr.stashy.model

import com.mnlpdr.stashy.data.TransactionType


data class Bag(
    val id: Long = 0L,
    val name: String, // Nome da Bag
    val description: String, // Descrição da Bag
    val transactions: List<Transaction> = emptyList() // Lista de transações associadas a essa Bag
) {
    // Cálculo do saldo total da Bag
    fun totalBalance(): Double {
        return transactions.filter { it.transactionType == TransactionType.BUY || it.transactionType == TransactionType.SELL }
            .sumOf { if (it.transactionType == TransactionType.BUY) it.quantity else -it.quantity }
    }

    // Cálculo do custo total em moeda fiduciária
    fun totalCost(): Double {
        return transactions.filter { it.transactionType == TransactionType.BUY }
            .sumOf { it.totalValue() }
    }

    // Cálculo do lucro ou prejuízo não realizado
    fun unrealizedProfitOrLoss(currentPrices: Map<String, Double>): Double {
        return transactions.filter { it.transactionType == TransactionType.BUY }
            .sumOf { currentPrices[it.coinTicker]?.times(it.quantity) ?: 0.0 } - totalCost()
    }

    // Cálculo do lucro ou prejuízo realizado
    fun realizedProfitOrLoss(): Double {
        return transactions.filter { it.transactionType == TransactionType.SELL }
            .sumOf { it.totalValue() } -
                transactions.filter { it.transactionType == TransactionType.BUY }
                    .sumOf { it.quantity * it.priceAtTransaction }
    }
}
