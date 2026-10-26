package com.example.kaijugrupomen.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kaijugrupomen.model.Producto

@Composable
fun TarjetaProducto(
    producto: Producto,
    enAlerta: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = producto.nombre.uppercase(), fontWeight = FontWeight.Black)
            Text(
                text = "${producto.codigo} · ${producto.categoria}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (producto.detalle.isNotBlank()) {
                Text(text = producto.detalle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(text = formatearPesos(producto.precio), color = MaterialTheme.colorScheme.primary)
            IndicadorStock(
                stockActual = producto.stockActual,
                stockMinimo = producto.stockMinimo,
                enAlerta = enAlerta
            )
        }
    }
}

fun formatearPesos(valor: Int): String {
    val conPuntos = valor.toString().reversed().chunked(3).joinToString(".").reversed()
    return "$$conPuntos"
}
