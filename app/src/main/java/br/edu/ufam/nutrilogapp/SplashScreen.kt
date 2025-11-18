package com.seunomeprojeto.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.NutriLogLogo
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme

@Composable
fun SplashScreen() {
    // 1. Aplica o tema
    NutrilogAppTheme {
        // 2. Container principal com cor de fundo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 3. O Componente modular do Logo
            NutriLogLogo(
                modifier = Modifier.size(150.dp) // Defina o tamanho do logo
            )

            // Espaçamento entre o logo e o texto
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}