package br.edu.ufam.nutrilogapp.common

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.ui.theme.ButtonBlue

@Composable
fun LoginFormCard(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // O Card com o fundo mais claro (surface)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // CardBackgroundDark
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo de Usuário
            FormInputField(
                value = username,
                onValueChange = { username = it },
                label = "Usuário:",
                placeholder = "Seu e-mail ou nome de usuário"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Senha
            FormInputField(
                value = password,
                onValueChange = { password = it },
                label = "Senha:",
                placeholder = "Sua senha",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botão Entrar
            Button(
                onClick = { onLoginClick(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBlue // Cor de destaque do botão
                )
            ) {
                Text("Entrar", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Link de Registro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = "Não tem uma conta? ",
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), // Texto cinza/claro
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(5.dp))
                TextButton(onClick = onRegisterClick, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        "Registre-se aqui",
                        color = MaterialTheme.colorScheme.primary, // Cor de destaque
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}