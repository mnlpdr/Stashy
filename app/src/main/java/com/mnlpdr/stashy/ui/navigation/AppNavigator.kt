package com.mnlpdr.stashy.ui.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mnlpdr.stashy.ui.splash.SplashScreen
import com.mnlpdr.stashy.ui.LoginScreen
import com.mnlpdr.stashy.ui.HomeScreen
import com.mnlpdr.stashy.ui.CryptoPricesScreen
import com.mnlpdr.stashy.ui.CryptoPricesViewModel
import com.mnlpdr.stashy.ui.TransactionViewModel
import com.mnlpdr.stashy.ui.Bag

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
            LoginScreen(onLogin = { navController.navigate("home") },
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
            CryptoPricesScreen(CryptoPricesViewModel(apiKey))
        }
    }
}
