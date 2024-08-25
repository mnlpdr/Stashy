package com.mnlpdr.stashy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnlpdr.stashy.R

@Composable
fun HomeScreen(userName: String, totalBalance: Double, balanceChange: Double, balanceChangePercent: Double, bags: List<Bag>) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Saudações com ícone de perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pixel_pfp_raccon_nobg), // Substitua com o seu recurso de ícone de perfil
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hello, $userName",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Saldo total
            Text(
                text = "$${"%.2f".format(totalBalance)} USD",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Variação do saldo
            Text(
                text = "${if (balanceChange >= 0) "+" else "-"}$${"%.2f".format(balanceChange)} (${balanceChangePercent}%) Today",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (balanceChange >= 0) Color.Green else Color.Red,
                    fontSize = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Seção de Bags
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bags",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
                TextButton(onClick = { /* TODO: Adicionar lógica para criar nova Bag */ }) {
                    Text(text = "+ New", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Bags
            bags.forEach { bag ->
                BagCard(bag)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun BagCard(bag: Bag) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = bag.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = bag.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: Navegar para Home */ }) {
                Icon(painter = painterResource(id = R.drawable.round_home_24), contentDescription = "Home")
            }
            IconButton(onClick = { /* TODO: Navegar para Market */ }) {
                Icon(painter = painterResource(id = R.drawable.round_auto_graph_24), contentDescription = "Market")
            }
            IconButton(onClick = { /* TODO: Navegar para Bags */ }) {
                Icon(painter = painterResource(id = R.drawable.round_attach_money_24), contentDescription = "Bags")
            }
            IconButton(onClick = { /* TODO: Navegar para News */ }) {
                Icon(painter = painterResource(id = R.drawable.round_newspaper_24), contentDescription = "News")
            }
            IconButton(onClick = { /* TODO: Navegar para Profile/Settings */ }) {
                Icon(painter = painterResource(id = R.drawable.round_person_24), contentDescription = "Profile/Settings")
            }
        }
    }
}


// Classe Bag para representar as Bags do usuário
data class Bag(
    val name: String,
    val description: String
)
