package com.example.kaijugrupomen.ui.screen

import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kaijugrupomen.ui.components.EncabezadoKaiju
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalGetImage::class)
@Composable
fun PantallaEscanerQR(
    onCodigoLeido: (String) -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val codigoLeido by rememberUpdatedState(onCodigoLeido)

    val escaner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        )
    }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val yaLeido = remember { AtomicBoolean(false) }
    val proveedor = remember { arrayOfNulls<ProcessCameraProvider>(1) }

    // Al salir de la pantalla se apaga la cámara y se liberan recursos
    DisposableEffect(Unit) {
        onDispose {
            proveedor[0]?.unbindAll()
            escaner.close()
            executor.shutdown()
        }
    }

    Scaffold(
        topBar = { EncabezadoKaiju(titulo = "Escanear código", onVolver = onVolver) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val vista = PreviewView(ctx)
                    val futuro = ProcessCameraProvider.getInstance(ctx)
                    futuro.addListener({
                        val camara = futuro.get()
                        proveedor[0] = camara

                        val preview = Preview.Builder().build()
                        preview.setSurfaceProvider(vista.surfaceProvider)

                        val analisis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        analisis.setAnalyzer(executor) { imagenProxy ->
                            val media = imagenProxy.image
                            if (media == null || yaLeido.get()) {
                                imagenProxy.close()
                                return@setAnalyzer
                            }
                            val imagen = InputImage.fromMediaImage(
                                media, imagenProxy.imageInfo.rotationDegrees
                            )
                            escaner.process(imagen)
                                .addOnSuccessListener { codigos ->
                                    val valor = codigos.firstOrNull()?.rawValue
                                    if (valor != null && yaLeido.compareAndSet(false, true)) {
                                        codigoLeido(valor)
                                    }
                                }
                                .addOnCompleteListener { imagenProxy.close() }
                        }

                        camara.unbindAll()
                        camara.bindToLifecycle(
                            lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analisis
                        )
                    }, ContextCompat.getMainExecutor(ctx))
                    vista
                }
            )
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)
            ) {
                Text("Apunta al código QR del producto", modifier = Modifier.padding(12.dp))
            }
        }
    }
}