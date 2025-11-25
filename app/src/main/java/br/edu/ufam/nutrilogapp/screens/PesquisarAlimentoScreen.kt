package br.edu.ufam.nutrilogapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.components.AlimentoDetailsCard
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import br.edu.ufam.nutrilogapp.viewmodel.PesquisarAlimentoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesquisarAlimentoScreen(
    onBackClick: () -> Unit = {},
    onAlimentoFound: (br.edu.ufam.nutrilogapp.data.model.Alimento) -> Unit = {},
    viewModel: PesquisarAlimentoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var barcodeInput by remember { mutableStateOf("") }

    NutrilogAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.search_food_title)) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = barcodeInput,
                    onValueChange = { barcodeInput = it },
                    label = { Text(stringResource(R.string.enter_barcode)) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (barcodeInput.isNotBlank()) {
                            viewModel.searchAlimento(barcodeInput)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading && barcodeInput.isNotBlank()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(stringResource(R.string.search))
                }

                Spacer(modifier = Modifier.height(24.dp))

                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.searching),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    uiState.errorMessage != null -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = uiState.errorMessage ?: stringResource(R.string.food_not_found),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    uiState.alimento != null -> {
                        AlimentoDetailsCard(
                            alimento = uiState.alimento!!,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                uiState.alimento?.let { onAlimentoFound(it) }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.continue_btn))
                        }
                    }
                }
            }
        }
    }
}

