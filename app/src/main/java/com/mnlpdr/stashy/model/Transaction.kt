package com.mnlpdr.stashy.model

import com.mnlpdr.stashy.data.TransactionType
import java.util.Date

data class Transaction(
    val id: String = "0L",
    val bagId: Long,
    val coinTicker: String, // Símbolo da cripto
    val cryptoName: String, // Nome completo da cripto
    val transactionType: TransactionType, // Tipo de transação (compra ou venda)
    val quantity: Double, // Quantidade de cripto envolvida
    val priceAtTransaction: Double, // Preço por unidade da cripto no momento da transação
    val transactionDate: Date // Data da transação
) {
    // Cálculo do valor total da transação
    fun totalValue(): Double {
        return quantity * priceAtTransaction
    }
}