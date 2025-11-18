package br.edu.ufam.nutrilogapp.screens

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import br.edu.ufam.nutrilogapp.components.CameraPreview
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import br.edu.ufam.nutrilogapp.ui.theme.PrimaryBlue

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen() {
    NutrilogAppTheme {
        // 1. Gerencia o estado da permissão da câmera
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        // 2. Armazena o código de barras detectado
        var scannedBarcode by remember { mutableStateOf("Aguardando Código...") }

        // 3. Armazena o estado de ativação do scanner (impede múltiplos scans rápidos)
        var isScanningActive by remember { mutableStateOf(true) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scanner de Código de Barras",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )

            when {
                // Permissão Concedida: Exibe o preview da câmera
                cameraPermissionState.status.isGranted -> {
                    Text(
                        text = "Enquadre o código de barras para escanear.",
                        color = Color.Green,
                        modifier = Modifier.padding(8.dp)
                    )

                    // Container da Câmera
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .aspectRatio(1f) // Usa 1:1 para um bom preview
                            .border(4.dp, PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isScanningActive) {
                            CameraPreview(
                                modifier = Modifier.fillMaxSize(),
                                onBarcodeDetected = { barcode ->
                                    // Desativa o scanner e exibe o resultado
                                    isScanningActive = false
                                    scannedBarcode = barcode
                                }
                            )
                        } else {
                            // Tela de resultado temporária enquanto o scanner está inativo
                            Text(
                                text = "Código Escaneado! $scannedBarcode",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Desenha um "quadro" para indicar a área de foco
                        Box(modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, Color.Red)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Resultado do Scan
                    Card(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Código Detectado:", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = scannedBarcode,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    // Botão para reativar o scan (reset)
                    Button(
                        onClick = {
                            scannedBarcode = "Aguardando Código..."
                            isScanningActive = true
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        enabled = !isScanningActive
                    ) {
                        Text("Escanear Novamente")
                    }
                }

                // Permissão Necessária: Exibe o botão para solicitar
                cameraPermissionState.status.shouldShowRationale || !cameraPermissionState.status.isGranted -> {
                    Text(
                        text = "É necessário acesso à câmera para escanear códigos de barras.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(24.dp)
                    )
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text("Conceder Permissão da Câmera")
                    }
                }
            }
        }
    }
}