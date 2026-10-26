package com.example.kaijugrupomen.repository

import com.example.kaijugrupomen.model.Usuario

class UsuarioRepository {
    fun obtenerUsuarios(): List<Usuario> = DatosDePrueba.usuarios
}
