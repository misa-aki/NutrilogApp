package br.edu.ufam.nutrilogapp.screens

import android.Manifest
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.components.ActionButton
import br.edu.ufam.nutrilogapp.components.CameraCaptureScreen
import br.edu.ufam.nutrilogapp.components.InfoBox
import br.edu.ufam.nutrilogapp.model.MealAnalysis
import br.edu.ufam.nutrilogapp.repository.GeminiRepository
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RegistraRefeicaoScreen(
    onScanMealClick: () -> Unit = {}, // This might be used for navigation if we weren't doing it inline, but let's keep it for compatibility
    onSearchFoodClick: () -> Unit = {},
    onReadBarcodeClick: () -> Unit = {}
) {
    // State to manage the current view
    var currentStep by remember { mutableStateOf(RegistraRefeicaoStep.MENU) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var analysisResult by remember { mutableStateOf<MealAnalysis?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val repository = remember { GeminiRepository() }
    val scope = rememberCoroutineScope()
    
    // Camera Permission State
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    NutrilogAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentStep) {
                RegistraRefeicaoStep.MENU -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.add_new_meal),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { currentStep = RegistraRefeicaoStep.CAMERA },
                            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.scan_meal))
                        }

                        Button(
                            onClick = onSearchFoodClick,
                            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.search_food))
                        }

                        Button(
                            onClick = onReadBarcodeClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.read_barcode))
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        InfoBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )
                    }
                }
                RegistraRefeicaoStep.CAMERA -> {
                    if (cameraPermissionState.status.isGranted) {
                        CameraCaptureScreen(
                            onPhotoCaptured = { bitmap ->
                                capturedBitmap = bitmap
                                currentStep = RegistraRefeicaoStep.LOADING
                                isLoading = true
                                errorMessage = null
                                
                                scope.launch {
                                    repository.analyzeMeal(bitmap).collect { result ->
                                        isLoading = false
                                        result.onSuccess { analysis ->
                                            analysisResult = analysis
                                            currentStep = RegistraRefeicaoStep.RESULT
                                        }.onFailure { error ->
                                            errorMessage = error.message ?: "Erro desconhecido"
                                            // Stay in loading/error state or go back?
                                            // For now let's show error in the loading view
                                        }
                                    }
                                }
                            }
                        )
                        
                        // Back button for camera
                        IconButton(
                            onClick = { currentStep = RegistraRefeicaoStep.MENU },
                            modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "É necessário acesso à câmera para tirar foto da refeição.",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(24.dp)
                            )
                            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                                Text("Conceder Permissão da Câmera")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            TextButton(onClick = { currentStep = RegistraRefeicaoStep.MENU }) {
                                Text("Voltar")
                            }
                        }
                    }
                }
                RegistraRefeicaoStep.LOADING -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (errorMessage != null) {
                            Text(
                                text = "Erro: $errorMessage",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(onClick = { currentStep = RegistraRefeicaoStep.CAMERA }) {
                                Text("Tentar Novamente")
                            }
                        } else {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Analisando sua refeição...")
                        }
                    }
                }
                RegistraRefeicaoStep.RESULT -> {
                    if (capturedBitmap != null && analysisResult != null) {
                        AnaliseRefeicaoScreen(
                            bitmap = capturedBitmap!!,
                            analysis = analysisResult!!,
                            onConfirmClick = {
                                // Here we would save the meal. For now, just go back to menu
                                currentStep = RegistraRefeicaoStep.MENU
                            },
                            onRetakeClick = {
                                currentStep = RegistraRefeicaoStep.CAMERA
                            }
                        )
                    } else {
                        // Should not happen
                        currentStep = RegistraRefeicaoStep.MENU
                    }
                }
            }
        }
    }
}

enum class RegistraRefeicaoStep {
    MENU, CAMERA, LOADING, RESULT
}

