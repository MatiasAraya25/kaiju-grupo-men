package com.example.kaijugrupomen.navigation

sealed class Rutas(val ruta: String) {
    object Escaner : Rutas("escaner")
    object Login : Rutas("login")
    object Catalogo : Rutas("catalogo")
    object Detalle : Rutas("detalle")
    object Formulario : Rutas("formulario")
    object Movimiento : Rutas("movimiento")
    object Alertas : Rutas("alertas")
}