package com.mnlpdr.stashy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mnlpdr.stashy.data.TransactionDAO
import com.mnlpdr.stashy.ui.TransactionViewModel

class TransactionViewModelFactory(
    private val transactionDAO: TransactionDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(transactionDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
