package com.seunomeprojeto.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ufam.nutrilogapp.components.NutriLogLogo
import br.edu.ufam.nutrilogapp.common.LoginFormCard
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import br.edu.ufam.nutrilogapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel { LoginViewModel(context.applicationContext as android.app.Application) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            kotlinx.coroutines.delay(5000)
            viewModel.clearError()
        }
    }

    NutrilogAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
        ) {
            NutriLogLogo(
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "NutriLog",
                    style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Acesse sua Conta",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

                LoginFormCard(
                onLoginClick = { username, password ->
                        viewModel.login(username, password, onLoginSuccess)
                },
                    onRegisterClick = onNavigateToRegister,
                    isLoading = uiState.isLoading
                )

                uiState.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            Spacer(modifier = Modifier.height(32.dp))
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}