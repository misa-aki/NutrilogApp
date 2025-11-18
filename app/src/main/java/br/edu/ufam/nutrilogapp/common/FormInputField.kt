package br.edu.ufam.nutrilogapp.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import br.edu.ufam.nutrilogapp.ui.theme.TextFieldDark

@Composable
fun FormInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
        )

        // Usando o TextField padrão do Material 3
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),

            // Customização do estilo do TextField para combinar com o tema escuro
            colors = TextFieldDefaults.colors(
                focusedContainerColor = TextFieldDark,
                unfocusedContainerColor = TextFieldDark,
                disabledContainerColor = TextFieldDark,

                focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Cor da linha quando focado
                unfocusedIndicatorColor = TextFieldDark, // Cor da linha quando não focado (ou usar Color.Transparent se quiser sem linha)

                cursorColor = MaterialTheme.colorScheme.primary,

                // Texto digitado
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
            ),
            shape = MaterialTheme.shapes.small // Borda arredondada
        )
    }
}