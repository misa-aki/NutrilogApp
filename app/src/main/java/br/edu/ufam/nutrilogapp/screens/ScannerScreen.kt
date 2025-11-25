package br.edu.ufam.nutrilogapp.screens

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import br.edu.ufam.nutrilogapp.R
import br.edu.ufam.nutrilogapp.components.CameraPreview
import br.edu.ufam.nutrilogapp.ui.theme.NutrilogAppTheme
import br.edu.ufam.nutrilogapp.ui.theme.PrimaryBlue

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    onCancelClick: () -> Unit = {},
    onConfirmClick: (String) -> Unit = {}
) {
    NutrilogAppTheme {
        // 1. Gerencia o estado da permissão da câmera
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        // 2. Armazena o código de barras detectado
        var scannedBarcode by remember { mutableStateOf("") }

        // 3. Armazena o estado de ativação do scanner (impede múltiplos scans rápidos)
        var isScanningActive by remember { mutableStateOf(true) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            when {
                // Permissão Concedida: Exibe o preview da câmera
                cameraPermissionState.status.isGranted -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.center_barcode),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .aspectRatio(3f / 4f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isScanningActive) {
                                CameraPreview(
                                    onBarcodeDetected = { barcode ->
                                        // Desativa o scanner e exibe o resultado
                                        isScanningActive = false
                                        scannedBarcode = barcode
                                    }
                                )
                            } else {
                                CameraPreview(
                                    onBarcodeDetected = { barcode ->
                                        scannedBarcode = barcode
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        if (scannedBarcode.isNotEmpty()) {
                            Text(
                                text = scannedBarcode,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.wait_barcode),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(48.dp))


                        Spacer(modifier = Modifier.height(48.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = onCancelClick,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.cancel),
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        scannedBarcode = "Aguardando Código..."
                                        isScanningActive = true
                                    },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = stringResource(R.string.retake),
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        if (!isScanningActive) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        if (!isScanningActive && scannedBarcode.isNotEmpty()) {
                                            onConfirmClick(scannedBarcode)
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                    enabled = !isScanningActive
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = stringResource(R.string.confirm),
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }

                }

                // Permissão Necessária: Exibe o botão para solicitar
                cameraPermissionState.status.shouldShowRationale || !cameraPermissionState.status.isGranted -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
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
}