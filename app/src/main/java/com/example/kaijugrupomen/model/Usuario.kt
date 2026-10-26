package com.example.kaijugrupomen.model

enum class Rol(val etiqueta: String) {
    ADMINISTRADOR("Administrador"),
    VENDEDOR("Personal de venta"),
    ENCARGADO_INVENTARIO("Encargado de inventario")
}

data class Usuario(
    val id: Int,
    val nombre: String,
    val rol: Rol
)
