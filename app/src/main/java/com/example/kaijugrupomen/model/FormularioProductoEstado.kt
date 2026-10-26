package com.example.kaijugrupomen.model

data class FormularioProductoEstado(
    val codigo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val precio: String = "",
    val stockMinimo: String = "",
    val detalle: String = "",
    val modoEdicion: Boolean = false,
    val errores: ErroresProducto = ErroresProducto()
)

data class ErroresProducto(
    val errorCodigo: String? = null,
    val errorNombre: String? = null,
    val errorCategoria: String? = null,
    val errorPrecio: String? = null,
    val errorStockMinimo: String? = null
)
