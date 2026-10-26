package com.example.kaijugrupomen.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoMovimiento(val etiqueta: String) {
    ENTRADA("Entrada"),
    SALIDA_VENTA("Salida por venta"),
    SALIDA_CONSUMO("Salida por consumo"),
    AJUSTE("Ajuste")
}

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigoProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: String,
    val fecha: String,
    val usuario: String
)
