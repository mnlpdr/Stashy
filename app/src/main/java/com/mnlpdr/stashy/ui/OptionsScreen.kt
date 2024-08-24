package com.mnlpdr.stashy.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OptionsScreen(onPortfolioClick: () -> Unit, onTop10CryptosClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onPortfolioClick, modifier = Modifier.padding(16.dp)) {
            Text(text = "Portfolio", fontSize = 18.sp)
        }
        Button(onClick = onTop10CryptosClick, modifier = Modifier.padding(16.dp)) {
            Text(text = "Top 10 Cryptos", fontSize = 18.sp)
        }
    }
}
