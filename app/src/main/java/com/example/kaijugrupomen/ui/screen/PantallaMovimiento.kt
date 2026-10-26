package com.example.kaijugrupomen.ui.screen

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kaijugrupomen.model.TipoMovimiento
import com.example.kaijugrupomen.model.Usuario
import com.example.kaijugrupomen.ui.components.EncabezadoKaiju
import com.example.kaijugrupomen.ui.components.IndicadorStock
import com.example.kaijugrupomen.ui.components.SelectorOpcion
import com.example.kaijugrupomen.ui.components.rememberSolicitudPermiso
import com.example.kaijugrupomen.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovimiento(
    viewModel: InventarioViewModel,
    usuario: Usuario,
    onEscanear: () -> Unit
) {
    val estado by viewModel.estadoMovimiento.collectAsState()
    val mensaje by viewModel.mensajeMovimiento.collectAsState()
    val productos by viewModel.productos.collectAsState()
    val tipos = viewModel.tiposPermitidos(usuario.rol)
    val productoElegido = productos.find { it.codigo == estado.codigoProducto }
    val haptic = LocalHapticFeedback.current

    val pedirCamara = rememberSolicitudPermiso(
        permiso = Manifest.permission.CAMERA,
        titulo = "Usaremos la cámara",
        explicacion = "La cámara sirve solo para leer el código QR del producto. No guardamos fotos.",
        onConcedido = onEscanear,
        onDenegado = viewModel::escaneoNoDisponible
    )

    Scaffold(
        topBar = { EncabezadoKaiju(titulo = "Registrar movimiento") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Registrando como ${usuario.nombre} (${usuario.rol.etiqueta})",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedButton(onClick = pedirCamara, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escanear código QR")
            }

            SelectorOpcion(
                textoBoton = productoElegido?.let { "${it.codigo} · ${it.nombre}" } ?: "Elegir producto",
                opciones = productos.map { "${it.codigo} · ${it.nombre}" },
                onSeleccionar = { indice -> viewModel.seleccionarProductoMovimiento(productos[indice].codigo) },
                error = estado.errores.errorProducto,
                icono = Icons.Filled.ShoppingCart
            )
            productoElegido?.let {
                IndicadorStock(
                    stockActual = it.stockActual,
                    stockMinimo = it.stockMinimo,
                    enAlerta = viewModel.enAlerta(it)
                )
            }

            SelectorOpcion(
                textoBoton = estado.tipo?.etiqueta ?: "Elegir tipo de movimiento",
                opciones = tipos.map { it.etiqueta },
                onSeleccionar = { indice -> viewModel.seleccionarTipo(tipos[indice]) },
                error = estado.errores.errorTipo,
                icono = Icons.Filled.Refresh
            )

            OutlinedTextField(
                value = estado.cantidad,
                onValueChange = viewModel::actualizarCantidad,
                label = {
                    Text(if (estado.tipo == TipoMovimiento.AJUSTE) "Stock real contado" else "Cantidad")
                },
                leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null) },
                isError = estado.errores.errorCantidad != null,
                supportingText = { estado.errores.errorCantidad?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.motivo,
                onValueChange = viewModel::actualizarMotivo,
                label = {
                    Text(if (estado.tipo == TipoMovimiento.AJUSTE) "Motivo (obligatorio)" else "Motivo (opcional)")
                },
                leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                isError = estado.errores.errorMotivo != null,
                supportingText = { estado.errores.errorMotivo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (viewModel.registrarMovimiento(usuario)) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar movimiento")
            }

            AnimatedVisibility(visible = mensaje != null) {
                Text(text = mensaje ?: "", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}