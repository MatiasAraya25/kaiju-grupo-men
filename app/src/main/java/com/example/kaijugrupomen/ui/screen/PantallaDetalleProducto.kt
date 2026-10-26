package com.example.kaijugrupomen.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kaijugrupomen.model.Producto
import com.example.kaijugrupomen.ui.components.EncabezadoKaiju
import com.example.kaijugrupomen.ui.components.IndicadorStock
import com.example.kaijugrupomen.ui.components.formatearPesos
import com.example.kaijugrupomen.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    viewModel: InventarioViewModel,
    esAdministrador: Boolean,
    onVolver: () -> Unit,
    onEditar: (Producto) -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val todosLosMovimientos by viewModel.movimientos.collectAsState()
    val codigo by viewModel.codigoSeleccionado.collectAsState()
    val actual = productos.find { it.codigo == codigo }

    Scaffold(
        topBar = {
            EncabezadoKaiju(
                titulo = "Detalle del producto",
                onVolver = onVolver,
                acciones = {
                    if (esAdministrador && actual != null) {
                        IconButton(onClick = { onEditar(actual) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar producto")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (actual == null) {
            Text(
                text = "Producto no encontrado.",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
        } else {
            val movimientos = viewModel.historialDe(actual.codigo, todosLosMovimientos)

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 12.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = actual.nombre.uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${actual.codigo} · ${actual.categoria} · ${actual.tipo}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = actual.descripcion)
                        if (actual.detalle.isNotBlank()) {
                            Text(text = actual.detalle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(text = formatearPesos(actual.precio), fontWeight = FontWeight.Bold)
                        IndicadorStock(
                            stockActual = actual.stockActual,
                            stockMinimo = actual.stockMinimo,
                            enAlerta = viewModel.enAlerta(actual)
                        )
                    }
                }
                item {
                    Text(
                        text = "Historial de movimientos",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }
                if (movimientos.isEmpty()) {
                    item {
                        Text(
                            text = "Este producto aún no tiene movimientos.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(movimientos) { movimiento ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = movimiento.tipo.etiqueta, fontWeight = FontWeight.Bold)
                                    Text(text = movimiento.motivo, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = "${movimiento.fecha} · ${movimiento.usuario}",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(text = "${movimiento.cantidad} u.")
                            }
                        }
                    }
                }
            }
        }
    }
}
