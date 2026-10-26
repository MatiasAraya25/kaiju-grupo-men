package com.example.kaijugrupomen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kaijugrupomen.model.Usuario
import com.example.kaijugrupomen.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionViewModel : ViewModel() {

    private val repositorio = UsuarioRepository()

    val usuariosDisponibles: List<Usuario> = repositorio.obtenerUsuarios()

    private val _usuarioSeleccionado = MutableStateFlow<Usuario?>(null)
    val usuarioSeleccionado: StateFlow<Usuario?> = _usuarioSeleccionado.asStateFlow()

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    fun seleccionarUsuario(usuario: Usuario) {
        _usuarioSeleccionado.value = usuario
        _errorLogin.value = null
    }

    fun ingresar(): Boolean {
        val elegido = _usuarioSeleccionado.value
        if (elegido == null) {
            _errorLogin.value = "Debes elegir un usuario para ingresar"
            return false
        }
        _usuarioActivo.value = elegido
        return true
    }

    fun cerrarSesion() {
        _usuarioActivo.value = null
        _usuarioSeleccionado.value = null
        _errorLogin.value = null
    }
}
