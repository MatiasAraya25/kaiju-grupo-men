package com.example.kaijugrupomen.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kaijugrupomen.model.Movimiento
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {
    @Query("SELECT * FROM movimientos ORDER BY fecha DESC, id DESC")
    fun obtenerTodos(): Flow<List<Movimiento>>

    @Insert
    suspend fun insertar(movimiento: Movimiento)

    @Insert
    suspend fun insertarTodos(movimientos: List<Movimiento>)
}
