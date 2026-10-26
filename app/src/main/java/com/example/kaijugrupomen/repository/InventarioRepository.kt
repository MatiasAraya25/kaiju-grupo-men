package com.example.kaijugrupomen.repository

import com.example.kaijugrupomen.model.Movimiento
import com.example.kaijugrupomen.model.Producto
import kotlinx.coroutines.flow.Flow

class InventarioRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao
) {

    val productos: Flow<List<Producto>> = productoDao.obtenerTodos()
    val movimientos: Flow<List<Movimiento>> = movimientoDao.obtenerTodos()

    suspend fun cargarDatosIniciales() {
        if (productoDao.contar() == 0) {
            productoDao.insertarTodos(DatosDePrueba.productos)
            movimientoDao.insertarTodos(DatosDePrueba.movimientos)
        }
    }

    suspend fun agregarProducto(producto: Producto) = productoDao.insertar(producto)

    suspend fun actualizarProducto(producto: Producto) = productoDao.actualizar(producto)

    suspend fun registrarMovimiento(movimiento: Movimiento, nuevoStock: Int) {
        movimientoDao.insertar(movimiento)
        productoDao.actualizarStock(movimiento.codigoProducto, nuevoStock)
    }
}
