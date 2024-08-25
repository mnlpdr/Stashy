package com.mnlpdr.stashy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mnlpdr.stashy.R

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Imagem do mascote
            Image(
                painter = painterResource(id = R.drawable.stashy_vault_nobg),
                contentDescription = "Mascote",
                modifier = Modifier.height(280.dp)
            )

            // Botão de login
            Button(
                onClick = onLogin,
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentWidth()
            ) {
                Text(text = "Enter the vault")
            }
        }
    }
}
