package com.mnlpdr.stashy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnlpdr.stashy.model.Transaction
import com.mnlpdr.stashy.data.TransactionDAO
import com.mnlpdr.stashy.data.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionDAO: TransactionDAO) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    // Função para adicionar uma transação localmente e no banco de dados
    fun addTransaction(transaction: Transaction) {
        _transactions.value += transaction
        saveTransaction(transaction.toEntity())  // Salva no banco de dados
    }

    private fun Transaction.toEntity(): TransactionEntity {
        return TransactionEntity(
            cryptoName = this.cryptoName,
            bagId = this.bagId,
            coinTicker = this.coinTicker,
            amount = this.quantity,
            pricePerUnit = this.priceAtTransaction,
            date = java.util.Date(this.transactionDate.time),
            transactionType = this.transactionType
        )
    }

    // Converte TransactionEntity para Transaction
    private fun TransactionEntity.toTransaction(): Transaction {
        return Transaction(
            id = this.id.toString(),
            bagId = this.bagId, // Passando o bagId
            cryptoName = this.cryptoName,
            coinTicker = this.coinTicker,
            quantity = this.amount,
            priceAtTransaction = this.pricePerUnit, // Corrigido para priceAtTransaction
            transactionDate = this.date, // Passando a data da transação
            transactionType = this.transactionType
        )
    }


    // Carrega transações para um determinado coinTicker
    fun loadTransactionsForCoin(coinTicker: String) {
        viewModelScope.launch {
            val entities = transactionDAO.getTransactionsForCoinTicker(coinTicker)
            _transactions.value = entities.map { it.toTransaction() }
        }
    }

    // Obtém todas as transações filtradas por coinTicker
    fun getTransactions(coinTicker: String): List<Transaction> {
        return _transactions.value.filter { it.coinTicker == coinTicker }
    }

    // Remove uma transação por ID
    fun removeTransaction(transactionId: String) {
        viewModelScope.launch {
            transactionDAO.deleteTransaction(transactionId.toInt())
            _transactions.value = _transactions.value.filter { it.id != transactionId }
        }
    }

    // Função para salvar a transação no banco de dados
    private fun saveTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionDAO.insertTransaction(transaction)
        }
    }
}
