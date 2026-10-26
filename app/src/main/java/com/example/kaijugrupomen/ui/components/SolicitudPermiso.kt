package com.example.kaijugrupomen.ui.components

import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/** Devuelve una función que ejecuta el flujo completo: verificar, explicar, pedir. */
@Composable
fun rememberSolicitudPermiso(
    permiso: String,
    titulo: String,
    explicacion: String,
    onConcedido: () -> Unit,
    onDenegado: () -> Unit
): () -> Unit {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var mostrarExplicacion by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido -> if (concedido) onConcedido() else onDenegado() }

    if (mostrarExplicacion) {
        AlertDialog(
            onDismissRequest = { mostrarExplicacion = false },
            title = { Text(titulo) },
            text = { Text(explicacion) },
            confirmButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    launcher.launch(permiso)
                }) { Text("Continuar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    onDenegado()
                }) { Text("Ahora no") }
            }
        )
    }

    return {
        val yaConcedido = ContextCompat.checkSelfPermission(context, permiso) ==
                PackageManager.PERMISSION_GRANTED
        when {
            yaConcedido -> onConcedido()
            activity != null &&
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permiso) ->
                mostrarExplicacion = true
            else -> launcher.launch(permiso)
        }
    }
}