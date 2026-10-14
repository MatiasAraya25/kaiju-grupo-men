package ui.components

@Composable
fun IndicadorStock(
    stockActual: Int,
    stockMinimo: Int,
    enAlerta: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Stock: $stockActual (mínimo $stockMinimo)",
            color = if (enAlerta) Color.Red else Color.Unspecified
        )
        if (enAlerta) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Stock bajo",
                tint = Color.Red
            )
        }
    }
}