package com.mnlpdr.stashy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import coil.ImageLoader
import coil.disk.DiskCache
import com.mnlpdr.stashy.data.AppDatabase
import com.mnlpdr.stashy.ui.*
import com.mnlpdr.stashy.ui.splash.SplashScreen
import com.mnlpdr.stashy.viewmodel.TransactionViewModelFactory
import com.mnlpdr.stashy.ui.navigation.AppNavigator
import com.mnlpdr.stashy.ui.CryptoPricesViewModel

class MainActivity : AppCompatActivity() {

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
            val navController = rememberNavController()
            AppNavigator(navController, apiKey, transactionViewModel, activity = this)
        }
    }
}

@Composable
fun AppNavigator(
    navController: NavHostController,
    apiKey: String,
    transactionViewModel: TransactionViewModel
) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = { navController.navigate("login") })
        }
        composable("login") {
            LoginScreen(
                onLogin = { navController.navigate("home") },
                activity = navController.context as AppCompatActivity
                )
        }
        composable("home") {
            HomeScreen(
                userName = "User",
                totalBalance = 10000.0,
                balanceChange = 50.0,
                balanceChangePercent = 0.5,
                bags = listOf(
                    Bag(name = "Bag 1", description = "Minha primeira bag"),
                    Bag(name = "Bag 2", description = "Outra bag importante"),
                    Bag(name = "Bag 3", description = "Bag de investimentos")
                ),
                navController
            )
        }
        composable("top10") {
            val context = LocalContext.current
            val imageLoader = ImageLoader.Builder(context)
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02) // Define 2% do espaço de armazenamento para o cache de imagens
                        .build()
                }
                .build()

            // Criando o ViewModel passando apiKey, context e imageLoader
            val viewModel = CryptoPricesViewModel(apiKey, context, imageLoader)
            CryptoPricesScreen(viewModel, imageLoader)
        }
    }
}
