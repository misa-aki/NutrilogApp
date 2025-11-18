package br.edu.ufam.nutrilogapp.components

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import br.edu.ufam.nutrilogapp.core.BarcodeAnalyzer
import java.util.concurrent.Executors

/**
 * Composable que exibe o feed de vídeo da câmera e configura a análise de frames.
 *
 * @param modifier Modificador de layout.
 * @param onBarcodeDetected Callback para o código de barras detectado.
 */
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onBarcodeDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Prepara a visualização da câmera
    val previewView = remember { PreviewView(context) }

    // Obtém o provider da câmera de forma assíncrona
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Cria o analisador de frames
    val analyzer = remember { BarcodeAnalyzer(onBarcodeDetected) }

    AndroidView(
        factory = {
            // Garante que o binding da câmera aconteça apenas uma vez
            val cameraProvider = cameraProviderFuture.get()
            bindCamera(context, cameraProvider, previewView, analyzer)
            previewView
        },
        modifier = modifier
    )
}

/**
 * Configura o CameraX: vincula a câmera ao ciclo de vida e inicia a análise de imagem.
 */
private fun bindCamera(
    context: Context,
    cameraProvider: ProcessCameraProvider,
    previewView: PreviewView,
    analyzer: ImageAnalysis.Analyzer
) {
    // 1. Desvincula tudo para reconfigurar (importante para evitar crash)
    cameraProvider.unbindAll()

    // 2. Cria o Preview
    val preview = Preview.Builder()
        .build()
        .also { it.setSurfaceProvider(previewView.surfaceProvider) }

    // 3. Configura a Análise de Imagem (para o scanner)
    val imageAnalyzer = ImageAnalysis.Builder()
        // O ML Kit trabalha melhor com esta configuração
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .also {
            it.setAnalyzer(Executors.newSingleThreadExecutor(), analyzer)
        }

    // 4. Seleciona a câmera (geralmente a traseira)
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    // 5. Vincula os casos de uso ao ciclo de vida
    try {
        cameraProvider.bindToLifecycle(
            (context as androidx.lifecycle.LifecycleOwner), // Converte o contexto para LifecycleOwner
            cameraSelector,
            preview,
            imageAnalyzer
        )
    } catch (exc: Exception) {
        // Lidar com erros de binding da câmera (ex: camera indisponível)
        println("Erro ao vincular a câmera: ${exc.message}")
    }
}