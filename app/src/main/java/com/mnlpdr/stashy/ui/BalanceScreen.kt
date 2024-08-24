package com.mnlpdr.stashy.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mnlpdr.stashy.data.CryptoPrice

@Composable
fun BalanceScreen(viewModel: CryptoPricesViewModel) {
    val totalBalance = viewModel.calculateTotalBalanceInUSD()
    val cryptoHoldings = viewModel.userHoldings

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Total Balance: $totalBalance USD",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your Holdings:",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        for (holding in cryptoHoldings) {
            Text(
                text = "${holding.cryptoName} (${holding.coinTicker}): ${holding.quantity} coins @ ${holding.currentPrice} USD"
            )
        }
    }
}

@Composable
fun BalanceItem(holding: CryptoPrice) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = holding.cryptoName)
        Text(text = "${holding.quantity} ${holding.coinTicker}")
        Text(text = "${holding.quantity * holding.currentPrice} USD")
    }
}
