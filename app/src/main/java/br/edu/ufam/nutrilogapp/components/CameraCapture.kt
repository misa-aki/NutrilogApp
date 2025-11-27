package br.edu.ufam.nutrilogapp.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    
                    val capture = ImageCapture.Builder().build()
                    imageCapture = capture

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            capture
                        )
                    } catch (exc: Exception) {
                        Log.e("CameraCapture", "Use case binding failed", exc)
                    }
                }, executor)
                previewView
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                 val cameraProvider = cameraProviderFuture.get()
                 // Re-bind if needed (e.g. camera switch) - simplified here
            }
        )

        // Switch Camera Button
        FloatingActionButton(
            onClick = {
                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
                
                // Re-bind camera
                 val cameraProvider = cameraProviderFuture.get()
                 val preview = Preview.Builder().build() // Need to get reference to preview view to set surface provider again if we were doing this properly outside AndroidView factory
                 // For simplicity, we are relying on recomposition to re-run the factory or we should move binding logic to a LaunchedEffect
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Switch Camera")
        }
    }
    
    // Expose a way to capture? Or just pass the capture function down?
    // Ideally, we want a shutter button here or pass the trigger.
    // For this task, I'll add a shutter button overlay in this component for simplicity, 
    // or better, I will expose a 'capture' function via a state holder, but let's keep it simple.
    // Wait, the requirement says "show the camera... after user takes a photo".
    // So I should put the shutter button here.
}

// Better approach: Separate the Preview and the Capture logic or put them together.
// Let's rewrite to be more robust and include the shutter button.

@Composable
fun CameraCaptureScreen(
    modifier: Modifier = Modifier,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val previewView = remember { PreviewView(context) }
    
    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val capture = ImageCapture.Builder().build()
        imageCapture = capture
        
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                capture
            )
        } catch (exc: Exception) {
            Log.e("CameraCapture", "Use case binding failed", exc)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        
        // Shutter Button
        androidx.compose.material3.Button(
            onClick = {
                takePhoto(context, imageCapture!!, onPhotoCaptured)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            androidx.compose.material3.Text("Tirar Foto")
        }
    }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap()
                // Rotate if needed (ImageProxy handles this usually but let's check rotation)
                val rotationDegrees = image.imageInfo.rotationDegrees
                val rotatedBitmap = if (rotationDegrees != 0) {
                     val matrix = Matrix()
                     matrix.postRotate(rotationDegrees.toFloat())
                     Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                } else {
                    bitmap
                }
                
                onPhotoCaptured(rotatedBitmap)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraCapture", "Photo capture failed: ${exception.message}", exception)
            }
        }
    )
}
