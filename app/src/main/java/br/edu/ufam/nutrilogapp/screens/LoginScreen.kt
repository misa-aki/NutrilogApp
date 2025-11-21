package com.seunomeprojeto.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.components.NutriLogLogo
import br.edu.ufam.nutrilogapp.common.LoginFormCard
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // O que fazer após o login
    onNavigateToRegister: () -> Unit // O que fazer ao clicar em Registrar
) {
    NutrilogAppTheme {
        // Usamos o LazyColumn para garantir que a tela seja scrollável em telas menores
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 48.dp), // Espaçamento superior
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Alinha o conteúdo ao topo
        ) {
            // 1. Logo
            NutriLogLogo(
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Título "NutriLog"
            Text(
                text = "NutriLog",
                style = MaterialTheme.typography.headlineLarge, // Um pouco menor que o displayMedium
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Subtítulo
            Text(
                text = "Acesse sua Conta",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Formulário Modularizado
            LoginFormCard (
                onLoginClick = { username, password ->
                    // Lógica de autenticação:
                    // Exemplo: if (autenticar(username, password)) onLoginSuccess()
                    onLoginSuccess()
                },
                onRegisterClick = onNavigateToRegister
            )

            // Espaçamento final
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}