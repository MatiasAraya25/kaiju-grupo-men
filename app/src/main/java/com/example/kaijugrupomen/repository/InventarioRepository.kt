package repository

import model.Movimiento
import model.Producto
import model.Usuario

class InventarioRepository {

    private val productos = DatosDePrueba.productos.toMutableList()
    private val movimientos = DatosDePrueba.movimientos.toMutableList()

    fun obtenerProductos(): List<Producto> = productos.toList()

    fun obtenerUsuarios(): List<Usuario> = DatosDePrueba.usuarios

    fun obtenerMovimientos(codigoProducto: String): List<Movimiento> =
        movimientos.filter { it.codigoProducto == codigoProducto }

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }
}
