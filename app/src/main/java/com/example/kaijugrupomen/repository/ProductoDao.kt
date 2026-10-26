package com.example.kaijugrupomen.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kaijugrupomen.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY codigo")
    fun obtenerTodos(): Flow<List<Producto>>

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(producto: Producto)

    @Insert
    suspend fun insertarTodos(productos: List<Producto>)

    @Update
    suspend fun actualizar(producto: Producto)

    @Query("UPDATE productos SET stockActual = :nuevoStock WHERE codigo = :codigo")
    suspend fun actualizarStock(codigo: String, nuevoStock: Int)
}
