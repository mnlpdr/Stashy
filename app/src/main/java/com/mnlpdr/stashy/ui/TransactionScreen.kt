package com.mnlpdr.stashy.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mnlpdr.stashy.model.Transaction
import com.mnlpdr.stashy.data.TransactionType
import java.util.*

@Composable
fun TransactionScreen(viewModel: TransactionViewModel = viewModel()) {
    var cryptoName by remember { mutableStateOf("") }
    var coinTicker by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var pricePerUnit by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.BUY) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = cryptoName,
            onValueChange = { cryptoName = it },
            label = { Text("Crypto Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = coinTicker,
            onValueChange = { coinTicker = it },
            label = { Text("Coin Ticker") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = pricePerUnit,
            onValueChange = { pricePerUnit = it },
            label = { Text("Price per Unit (USD)") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Text(
                text = transactionType.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Buy") },
                    onClick = {
                        transactionType = TransactionType.BUY
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sell") },
                    onClick = {
                        transactionType = TransactionType.SELL
                        expanded = false
                    }
                )

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val transaction = Transaction(
                id = UUID.randomUUID().toString(),
                bagId = 0,
                cryptoName = cryptoName,  // Adicionando o campo faltante
                coinTicker = coinTicker,
                transactionType = transactionType,
                quantity = amount.toDouble(),
                priceAtTransaction = pricePerUnit.toDouble(),
                transactionDate = Date()
            )
            viewModel.addTransaction(transaction)
        }) {
            Text("Add Transaction")
        }
    }
}
