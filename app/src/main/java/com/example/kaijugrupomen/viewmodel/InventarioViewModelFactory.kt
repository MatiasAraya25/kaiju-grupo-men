package com.example.kaijugrupomen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaijugrupomen.repository.InventarioRepository
import com.example.kaijugrupomen.repository.KaijuDatabase

class InventarioViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val baseDatos = KaijuDatabase.obtenerInstancia(context)
    private val repositorio = InventarioRepository(baseDatos.productoDao(), baseDatos.movimientoDao())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = InventarioViewModel(repositorio) as T
}
