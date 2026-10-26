package com.example.kaijugrupomen.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.kaijugrupomen.ui.components.TarjetaProducto
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
