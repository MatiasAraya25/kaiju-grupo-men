package com.example.kaijugrupomen.ui.screen

import android.Manifest
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kaijugrupomen.model.Producto
import com.example.kaijugrupomen.ui.components.EncabezadoKaiju
import com.example.kaijugrupomen.ui.components.Notificaciones
import com.example.kaijugrupomen.ui.components.TarjetaProducto
import com.example.kaijugrupomen.ui.components.rememberSolicitudPermiso
import com.example.kaijugrupomen.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAlertas(
    viewModel: InventarioViewModel,
    onProductoClick: (Producto) -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val enAlerta = productos.filter { viewModel.enAlerta(it) }
    val context = LocalContext.current

    val enviarAviso = {
        Notificaciones.avisarStockBajo(context, viewModel.textoReposicion(productos), enAlerta.size)
    }
    val pedirNotificaciones = rememberSolicitudPermiso(
        permiso = Manifest.permission.POST_NOTIFICATIONS,
        titulo = "Avisos de stock bajo",
        explicacion = "Te avisaremos cuando haya productos bajo el stock mínimo. No enviaremos nada más.",
        onConcedido = enviarAviso,
        onDenegado = {
            Toast.makeText(context, "Sin permiso: no se mostrará el aviso.", Toast.LENGTH_SHORT).show()
        }
    )

    Scaffold(
        topBar = { EncabezadoKaiju(titulo = "Alertas de stock") }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            item {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, viewModel.textoReposicion(productos))
                        }
                        context.startActivity(Intent.createChooser(intent, "Compartir reposición"))
                    },
                    enabled = enAlerta.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compartir lista de reposición")
                }
                Button(
                    onClick = {
                        // Antes de Android 13 no existe el permiso: se envía directo
                        if (Build.VERSION.SDK_INT >= 33) pedirNotificaciones() else enviarAviso()
                    },
                    enabled = enAlerta.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Filled.Notifications, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Avisar stock bajo")
                }
            }
            if (enAlerta.isEmpty()) {
                item {
                    Text(
                        text = "No hay productos con stock bajo.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(enAlerta) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        enAlerta = true,
                        onClick = { onProductoClick(producto) }
                    )
                }
            }
        }
    }
}