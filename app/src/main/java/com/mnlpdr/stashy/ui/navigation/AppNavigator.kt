package com.mnlpdr.stashy.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mnlpdr.stashy.ui.splash.SplashScreen
import com.mnlpdr.stashy.ui.LoginScreen
import com.mnlpdr.stashy.ui.OptionsScreen
import com.mnlpdr.stashy.ui.CryptoPricesScreen
import com.mnlpdr.stashy.ui.CryptoPricesViewModel
import com.mnlpdr.stashy.ui.TransactionViewModel

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
            LoginScreen(navController)
        }
        composable("options") {
            OptionsScreen(
                onTop10CryptosClick = { navController.navigate("top10") },
                onPortfolioClick = { /* Navegação para a tela de Portfolio, quando implementada */ }
            )
        }
        composable("top10") {
            CryptoPricesScreen(CryptoPricesViewModel(apiKey))
        }
    }
}
