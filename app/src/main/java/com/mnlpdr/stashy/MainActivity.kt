package com.mnlpdr.stashy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.mnlpdr.stashy.data.AppDatabase
import com.mnlpdr.stashy.ui.CryptoPricesScreen
import com.mnlpdr.stashy.ui.CryptoPricesViewModel
import com.mnlpdr.stashy.ui.TransactionViewModel
import com.mnlpdr.stashy.viewmodel.TransactionViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializando o banco de dados dentro do onCreate
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

        // Obtendo o DAO do banco de dados
        val transactionDAO = db.transactionDAO()

        // Configurando o ViewModel usando a TransactionViewModelFactory
        val viewModelFactory = TransactionViewModelFactory(transactionDAO)
        val transactionViewModel = ViewModelProvider(this, viewModelFactory).get(TransactionViewModel::class.java)

        // Chave de API para buscar preços de criptomoedas
        val apiKey = "6cb76328-da2c-4d71-92a4-6292f7bb1336"

        setContent {
            val cryptoPricesViewModel = CryptoPricesViewModel(apiKey)
            CryptoPricesScreen(cryptoPricesViewModel)
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        content()
    }
}
