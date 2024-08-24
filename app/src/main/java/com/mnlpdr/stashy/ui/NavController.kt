package com.mnlpdr.stashy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mnlpdr.stashy.R

@Composable
fun InitialScreen(navController: NavController) {


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.stashy_nobg), // Substitua pelo seu mascote
            contentDescription = "Mascot",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("mainMenu") }) {
            Text(text = "Login")
        }
    }
}

@Composable
fun MainMenuScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { /* Navegar para Portfolio */ }) {
            Text(text = "Portfolio")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Navegar para Top 10 Cryptos */ }) {
            Text(text = "Top 10 Cryptos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitialScreenPreview() {
    val navController = rememberNavController()
    InitialScreen(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    val navController = rememberNavController()
    MainMenuScreen(navController = navController)
}
