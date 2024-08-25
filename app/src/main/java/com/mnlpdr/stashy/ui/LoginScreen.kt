package com.mnlpdr.stashy.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha
import com.mnlpdr.stashy.R
import com.mnlpdr.stashy.biometricauth.BiometricPromptManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLogin: () -> Unit, activity: AppCompatActivity) {
    val biometricPromptManager = remember { BiometricPromptManager(activity) }
    val scope = rememberCoroutineScope()
    val alpha = remember { Animatable(0f) }

    // Animação de fade in (esmaecimento)
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000, // Duração da animação (1 segundo)
                easing = LinearOutSlowInEasing
            )
        )
    }

    // Observando o resultado da autenticação
    LaunchedEffect(Unit) {
        scope.launch {
            biometricPromptManager.promptResults.collect { result ->
                when (result) {
                    is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        onLogin()
                    }
                    is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                        Log.e("LoginScreen", "Erro de autenticação: ${result.error}")
                    }
                    is BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                        Log.e("LoginScreen", "Autenticação falhou")
                    }
                    is BiometricPromptManager.BiometricResult.HardwareUnavailable,
                    is BiometricPromptManager.BiometricResult.FeatureUnavailable,
                    is BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                        Log.e("LoginScreen", "Autenticação biométrica não disponível")
                    }
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha.value),
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
                onClick = {
                    biometricPromptManager.showBiometricPrompt(
                        title = "Autenticação Necessária",
                        description = "Por favor, autentique para entrar no aplicativo."
                    )
                },
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentWidth()
            ) {
                Text(text = "Enter the vault")
            }
        }
    }
}
