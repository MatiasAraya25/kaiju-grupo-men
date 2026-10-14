package ui.components

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
            Text(text = producto.nombre, fontWeight = FontWeight.Bold)
            Text(text = "${producto.codigo} · ${producto.categoria}", color = Color.Gray)
            if (producto.detalle.isNotBlank()) {
                Text(text = producto.detalle, color = Color.Gray)
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