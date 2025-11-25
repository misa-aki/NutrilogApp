package br.edu.ufam.nutrilogapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.common.FormInputField
import br.edu.ufam.nutrilogapp.data.model.AppConfig
import br.edu.ufam.nutrilogapp.data.model.ProfileUpdateRequest
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import br.edu.ufam.nutrilogapp.viewmodel.EditarConfiguracoesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarConfiguracoesScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: EditarConfiguracoesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var alturaCm by remember { mutableStateOf("") }
    var pesoKg by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var nivelAtividade by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(uiState.profile, uiState.config) {
        uiState.profile?.let { profile ->
            username = (profile.username ?: "").takeIf { it.isNotEmpty() } ?: ""
            email = (profile.email ?: "").takeIf { it.isNotEmpty() } ?: ""
        } ?: run {
            username = ""
            email = ""
        }
        uiState.config?.let { config ->
            alturaCm = config.alturaCm?.toInt()?.toString() ?: ""
            pesoKg = config.pesoKg?.toString() ?: ""
            dataNascimento = config.dataNascimento ?: ""
            sexo = config.sexo ?: ""
            nivelAtividade = config.nivelAtividade.takeIf { it.isNotEmpty() } ?: "Sedentário"
        } ?: run {
            alturaCm = ""
            pesoKg = ""
            dataNascimento = ""
            sexo = ""
            nivelAtividade = "Sedentário"
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            kotlinx.coroutines.delay(5000)
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearSuccess()
            isEditing = false
        }
    }

    NutrilogAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.my_profile)) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { paddingValues ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.my_profile),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.manage_account_info),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = stringResource(R.string.personal_info),
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(
                                            text = stringResource(R.string.update_personal_data),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                                if (!isEditing) {
                                    Button(
                                        onClick = { isEditing = true },
                                        enabled = !uiState.isSaving
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(stringResource(R.string.edit_profile))
                                    }
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                            if (isEditing) {
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = alturaCm,
                                        onValueChange = { alturaCm = it },
                                        label = { Text(stringResource(R.string.height_label)) },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                                        ),
                                        supportingText = { Text(stringResource(R.string.height_hint)) }
                                    )
                                    OutlinedTextField(
                                        value = pesoKg,
                                        onValueChange = { pesoKg = it },
                                        label = { Text(stringResource(R.string.current_weight)) },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                                        ),
                                        supportingText = { Text(stringResource(R.string.weight_hint)) }
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = dataNascimento,
                                        onValueChange = { dataNascimento = it },
                                        label = { Text(stringResource(R.string.birth_date)) },
                                        modifier = Modifier.weight(1f),
                                        placeholder = { Text("YYYY-MM-DD") }
                                    )
                                    var expandedSexo by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expandedSexo,
                                        onExpandedChange = { expandedSexo = !expandedSexo },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        OutlinedTextField(
                                            value = when (sexo) {
                                                "F" -> stringResource(R.string.female)
                                                "M" -> stringResource(R.string.male)
                                                else -> stringResource(R.string.prefer_not_to_say)
                                            },
                                            onValueChange = {},
                                            label = { Text(stringResource(R.string.gender)) },
                                            readOnly = true,
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSexo) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .menuAnchor()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedSexo,
                                            onDismissRequest = { expandedSexo = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text(stringResource(R.string.prefer_not_to_say)) },
                                                onClick = { sexo = ""; expandedSexo = false }
                                            )
                                            DropdownMenuItem(
                                                text = { Text(stringResource(R.string.female)) },
                                                onClick = { sexo = "F"; expandedSexo = false }
                                            )
                                            DropdownMenuItem(
                                                text = { Text(stringResource(R.string.male)) },
                                                onClick = { sexo = "M"; expandedSexo = false }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                var expandedAtividade by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = expandedAtividade,
                                    onExpandedChange = { expandedAtividade = !expandedAtividade },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = when (nivelAtividade) {
                                            "Sedentário" -> stringResource(R.string.sedentary)
                                            "Leve" -> stringResource(R.string.light)
                                            "Moderado" -> stringResource(R.string.moderate)
                                            "Intenso" -> stringResource(R.string.intense)
                                            else -> nivelAtividade.ifEmpty { stringResource(R.string.sedentary) }
                                        },
                                        onValueChange = {},
                                        label = { Text(stringResource(R.string.activity_level)) },
                                        readOnly = true,
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAtividade) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        supportingText = { Text(stringResource(R.string.activity_hint)) }
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expandedAtividade,
                                        onDismissRequest = { expandedAtividade = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.sedentary)) },
                                            onClick = { nivelAtividade = "Sedentário"; expandedAtividade = false }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.light)) },
                                            onClick = { nivelAtividade = "Leve"; expandedAtividade = false }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.moderate)) },
                                            onClick = { nivelAtividade = "Moderado"; expandedAtividade = false }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.intense)) },
                                            onClick = { nivelAtividade = "Intenso"; expandedAtividade = false }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = stringResource(R.string.change_password),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = stringResource(R.string.password_hint),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                FormInputField(
                                    value = currentPassword ?: "",
                                    onValueChange = { currentPassword = it },
                                    label = stringResource(R.string.current_password),
                                    placeholder = stringResource(R.string.current_password),
                                    isPassword = true
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                FormInputField(
                                    value = newPassword ?: "",
                                    onValueChange = { newPassword = it },
                                    label = stringResource(R.string.new_password),
                                    placeholder = stringResource(R.string.new_password),
                                    isPassword = true
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                FormInputField(
                                    value = confirmPassword ?: "",
                                    onValueChange = { confirmPassword = it },
                                    label = stringResource(R.string.confirm_password),
                                    placeholder = stringResource(R.string.confirm_password),
                                    isPassword = true
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            isEditing = false
                                            uiState.profile?.let { profile ->
                                                username = profile.username
                                                email = profile.email
                                            }
                                            uiState.config?.let { config ->
                                                alturaCm = config.alturaCm?.toInt()?.toString() ?: ""
                                                pesoKg = config.pesoKg?.toString() ?: ""
                                                dataNascimento = config.dataNascimento ?: ""
                                                sexo = config.sexo ?: ""
                                                nivelAtividade = config.nivelAtividade.ifEmpty { "Sedentário" }
                                            }
                                            currentPassword = ""
                                            newPassword = ""
                                            confirmPassword = ""
                                        },
                                        modifier = Modifier.weight(1f),
                                        enabled = !uiState.isSaving
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(stringResource(R.string.cancel))
                                    }

                                    Button(
                                        onClick = {
                                            val profileRequest = ProfileUpdateRequest(
                                                username = if (username != uiState.profile?.username) username else null,
                                                email = if (email != uiState.profile?.email) email else null,
                                                currentPassword = if (currentPassword.isNotEmpty()) currentPassword else null,
                                                newPassword = if (newPassword.isNotEmpty() && newPassword == confirmPassword) newPassword else null
                                            )
                                            
                                            val baseConfig = uiState.config ?: AppConfig()
                                            val configData = baseConfig.copy(
                                                alturaCm = alturaCm.toDoubleOrNull(),
                                                pesoKg = pesoKg.toDoubleOrNull(),
                                                dataNascimento = if (dataNascimento.isNotEmpty()) dataNascimento else null,
                                                sexo = if (sexo.isNotEmpty()) sexo else null,
                                                nivelAtividade = if (nivelAtividade.isNotEmpty()) nivelAtividade else "Sedentário"
                                            )
                                            
                                            viewModel.updateProfile(profileRequest, configData) {}
                                        },
                                        modifier = Modifier.weight(1f),
                                        enabled = !uiState.isSaving
                                    ) {
                                        if (uiState.isSaving) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        } else {
                                            Icon(Icons.Default.Add, contentDescription = null)
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (uiState.isSaving) stringResource(R.string.saving) else stringResource(R.string.save_changes))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = stringResource(R.string.danger_zone),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = stringResource(R.string.irreversible_actions),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = stringResource(R.string.logout_account),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = stringResource(R.string.end_current_session),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                OutlinedButton(
                                    onClick = onLogoutClick,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(Icons.Default.ExitToApp, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(stringResource(R.string.logout))
                                }
                            }
                        }
                    }

                    uiState.errorMessage?.let { error ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    uiState.successMessage?.let { success ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = success,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

