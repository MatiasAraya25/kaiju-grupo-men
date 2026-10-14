package model
enum class TipoMovimiento(val etiqueta: String) {
    ENTRADA("Entrada"),
    SALIDA_VENTA("Salida por venta"),
    SALIDA_CONSUMO("Salida por consumo"),
    AJUSTE("Ajuste")
}
data class Movimiento(
    val id: Int,
    val codigoProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: String,
    val fecha: String,
    val usuario: String
)
