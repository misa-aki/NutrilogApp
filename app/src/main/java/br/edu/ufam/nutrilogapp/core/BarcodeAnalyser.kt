package br.edu.ufam.nutrilogapp.core

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

/**
 * Classe que implementa ImageAnalysis.Analyzer para processar frames da câmera
 * e detectar códigos de barras usando ML Kit.
 *
 * @param onBarcodeDetected Callback chamado quando um código de barras é detectado.
 */
class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    // Configura o scanner ML Kit para focar apenas em códigos de barras comuns (ex: EAN, UPC)
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    // O executor para rodar a análise em uma thread separada
    private val executor = Executors.newSingleThreadExecutor()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        // Obter a imagem do ImageProxy
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            // Processa a imagem
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    // Se algum código de barras for encontrado
                    if (barcodes.isNotEmpty()) {
                        val rawValue = barcodes.first().rawValue
                        if (rawValue != null) {
                            // Chama o callback com o valor do código
                            onBarcodeDetected(rawValue)
                        }
                    }
                }
                .addOnFailureListener {
                    // Log de erro, se necessário
                    // println("Erro ao escanear código de barras: ${it.message}")
                }
                .addOnCompleteListener {
                    // Fechar o ImageProxy para liberar o buffer para o próximo frame
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}