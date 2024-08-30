package com.mnlpdr.stashy.ui.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.disk.DiskCache
import com.mnlpdr.stashy.ui.splash.SplashScreen
import com.mnlpdr.stashy.ui.LoginScreen
import com.mnlpdr.stashy.ui.HomeScreen
import com.mnlpdr.stashy.ui.CryptoPricesScreen
import com.mnlpdr.stashy.ui.CryptoPricesViewModel
import com.mnlpdr.stashy.ui.TransactionViewModel
import com.mnlpdr.stashy.ui.Bag
import com.mnlpdr.stashy.ui.FuturesLogbookScreen
import com.mnlpdr.stashy.viewmodel.FuturesLogbookViewModel

@Composable
fun AppNavigator(
    navController: NavHostController,
    apiKey: String,
    transactionViewModel: TransactionViewModel,
    activity: AppCompatActivity
) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = { navController.navigate("login") })
        }
        composable("login") {
            LoginScreen(
                onLogin = { navController.navigate("home") },
                activity = activity
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
                navController = navController
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
        composable("futures_logbook") {
            FuturesLogbookScreen(viewModel = FuturesLogbookViewModel())
        }
    }
}
