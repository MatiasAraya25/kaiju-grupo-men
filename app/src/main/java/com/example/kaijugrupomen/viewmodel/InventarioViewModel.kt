package com.example.kaijugrupomen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaijugrupomen.model.ErroresMovimiento
import com.example.kaijugrupomen.model.ErroresProducto
import com.example.kaijugrupomen.model.FormularioMovimientoEstado
import com.example.kaijugrupomen.model.FormularioProductoEstado
import com.example.kaijugrupomen.model.Movimiento
import com.example.kaijugrupomen.model.Producto
import com.example.kaijugrupomen.model.Rol
import com.example.kaijugrupomen.model.TipoMovimiento
import com.example.kaijugrupomen.model.Usuario
import com.example.kaijugrupomen.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventarioViewModel(private val repositorio: InventarioRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repositorio.productos.stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
    )

    val movimientos: StateFlow<List<Movimiento>> = repositorio.movimientos.stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
    )

    init {
        viewModelScope.launch { repositorio.cargarDatosIniciales() }
    }

    fun enAlerta(producto: Producto): Boolean = producto.stockActual <= producto.stockMinimo

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _categoriaFiltro = MutableStateFlow<String?>(null)
    val categoriaFiltro: StateFlow<String?> = _categoriaFiltro.asStateFlow()

    fun actualizarBusqueda(valor: String) {
        _busqueda.value = valor
    }

    fun filtrarPorCategoria(categoria: String?) {
        _categoriaFiltro.value = categoria
    }

    fun filtrarProductos(lista: List<Producto>, texto: String, categoria: String?): List<Producto> =
        lista.filter { producto ->
            val coincideTexto = texto.isBlank() ||
                    producto.nombre.contains(texto.trim(), ignoreCase = true) ||
                    producto.codigo.contains(texto.trim(), ignoreCase = true)
            val coincideCategoria = categoria == null || producto.categoria == categoria
            coincideTexto && coincideCategoria
        }

    private val _codigoSeleccionado = MutableStateFlow<String?>(null)
    val codigoSeleccionado: StateFlow<String?> = _codigoSeleccionado.asStateFlow()

    fun seleccionarProducto(codigo: String) {
        _codigoSeleccionado.value = codigo
    }

    fun historialDe(codigo: String, lista: List<Movimiento>): List<Movimiento> =
        lista.filter { it.codigoProducto == codigo }

    fun textoReposicion(lista: List<Producto>): String {
        val enAlerta = lista.filter { enAlerta(it) }
        if (enAlerta.isEmpty()) return "Kaiju: no hay productos para reponer."
        val lineas = enAlerta.joinToString("\n") {
            "- ${it.codigo} ${it.nombre}: stock ${it.stockActual} (mínimo ${it.stockMinimo})"
        }
        return "Kaiju - Productos para reponer:\n$lineas"
    }

    private val _estadoFormulario = MutableStateFlow(FormularioProductoEstado())
    val estadoFormulario: StateFlow<FormularioProductoEstado> = _estadoFormulario.asStateFlow()

    fun actualizarCodigo(valor: String) {
        _estadoFormulario.update { it.copy(codigo = valor) }
    }

    fun actualizarNombre(valor: String) {
        _estadoFormulario.update { it.copy(nombre = valor) }
    }

    fun actualizarDescripcion(valor: String) {
        _estadoFormulario.update { it.copy(descripcion = valor) }
    }

    fun actualizarCategoria(valor: String) {
        _estadoFormulario.update { it.copy(categoria = valor) }
    }

    fun actualizarPrecio(valor: String) {
        _estadoFormulario.update { it.copy(precio = valor) }
    }

    fun actualizarStockMinimo(valor: String) {
        _estadoFormulario.update { it.copy(stockMinimo = valor) }
    }

    fun actualizarDetalle(valor: String) {
        _estadoFormulario.update { it.copy(detalle = valor) }
    }

    fun limpiarFormulario() {
        _estadoFormulario.value = FormularioProductoEstado()
    }

    fun prepararEdicion(producto: Producto) {
        _estadoFormulario.value = FormularioProductoEstado(
            codigo = producto.codigo,
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            categoria = producto.categoria,
            precio = producto.precio.toString(),
            stockMinimo = producto.stockMinimo.toString(),
            detalle = producto.detalle,
            modoEdicion = true
        )
    }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value
        val codigo = estado.codigo.trim()
        val precio = estado.precio.trim().toIntOrNull()
        val stockMinimo = estado.stockMinimo.trim().toIntOrNull()

        val errorCodigo = when {
            codigo.isBlank() -> "El código es obligatorio"
            !estado.modoEdicion && productos.value.any { it.codigo.equals(codigo, ignoreCase = true) } ->
                "Ya existe un producto con ese código"
            else -> null
        }
        val errorNombre = when {
            estado.nombre.isBlank() -> "El nombre es obligatorio"
            estado.nombre.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
        val errorCategoria = if (estado.categoria.isBlank()) "Debes elegir una categoría" else null
        val errorPrecio = when {
            estado.precio.isBlank() -> "El precio es obligatorio"
            precio == null -> "El precio debe ser un número entero"
            precio <= 0 -> "El precio debe ser mayor a cero"
            else -> null
        }
        val errorStockMinimo = when {
            estado.stockMinimo.isBlank() -> "El stock mínimo es obligatorio"
            stockMinimo == null -> "El stock mínimo debe ser un número entero"
            stockMinimo < 0 -> "El stock mínimo no puede ser negativo"
            else -> null
        }

        _estadoFormulario.update {
            it.copy(
                errores = ErroresProducto(
                    errorCodigo = errorCodigo,
                    errorNombre = errorNombre,
                    errorCategoria = errorCategoria,
                    errorPrecio = errorPrecio,
                    errorStockMinimo = errorStockMinimo
                )
            )
        }
        return errorCodigo == null && errorNombre == null && errorCategoria == null &&
                errorPrecio == null && errorStockMinimo == null
    }

    fun guardarProducto(): Boolean {
        if (!validarFormulario()) return false

        val estado = _estadoFormulario.value
        val existente = productos.value.find { it.codigo == estado.codigo.trim() }
        val producto = Producto(
            codigo = estado.codigo.trim(),
            nombre = estado.nombre.trim(),
            descripcion = estado.descripcion.trim(),
            categoria = estado.categoria,
            tipo = existente?.tipo ?: "Polera",
            precio = estado.precio.trim().toIntOrNull() ?: 0,
            stockActual = existente?.stockActual ?: 0,
            stockMinimo = estado.stockMinimo.trim().toIntOrNull() ?: 0,
            detalle = estado.detalle.trim()
        )

        viewModelScope.launch {
            if (estado.modoEdicion) repositorio.actualizarProducto(producto)
            else repositorio.agregarProducto(producto)
        }
        _estadoFormulario.value = FormularioProductoEstado()
        return true
    }

    private val _estadoMovimiento = MutableStateFlow(FormularioMovimientoEstado())
    val estadoMovimiento: StateFlow<FormularioMovimientoEstado> = _estadoMovimiento.asStateFlow()

    private val _mensajeMovimiento = MutableStateFlow<String?>(null)
    val mensajeMovimiento: StateFlow<String?> = _mensajeMovimiento.asStateFlow()

    fun tiposPermitidos(rol: Rol): List<TipoMovimiento> = when (rol) {
        Rol.ADMINISTRADOR -> listOf(TipoMovimiento.ENTRADA, TipoMovimiento.AJUSTE)
        Rol.VENDEDOR -> listOf(TipoMovimiento.SALIDA_VENTA, TipoMovimiento.SALIDA_CONSUMO)
        Rol.ENCARGADO_INVENTARIO -> listOf(
            TipoMovimiento.ENTRADA, TipoMovimiento.SALIDA_VENTA,
            TipoMovimiento.SALIDA_CONSUMO, TipoMovimiento.AJUSTE
        )
    }

    fun seleccionarProductoMovimiento(codigo: String) {
        _estadoMovimiento.update { it.copy(codigoProducto = codigo) }
        _mensajeMovimiento.value = null
    }

    fun seleccionarTipo(tipo: TipoMovimiento) {
        _estadoMovimiento.update { it.copy(tipo = tipo) }
        _mensajeMovimiento.value = null
    }

    fun actualizarCantidad(valor: String) {
        _estadoMovimiento.update { it.copy(cantidad = valor) }
    }

    fun actualizarMotivo(valor: String) {
        _estadoMovimiento.update { it.copy(motivo = valor) }
    }

    fun validarMovimiento(rol: Rol): Boolean {
        val estado = _estadoMovimiento.value
        val producto = productos.value.find { it.codigo == estado.codigoProducto }
        val tipo = estado.tipo
        val cantidad = estado.cantidad.trim().toIntOrNull()
        val esSalida = tipo == TipoMovimiento.SALIDA_VENTA || tipo == TipoMovimiento.SALIDA_CONSUMO

        val errorProducto = if (producto == null) "Debes elegir un producto" else null
        val errorTipo = when {
            tipo == null -> "Debes elegir el tipo de movimiento"
            tipo !in tiposPermitidos(rol) -> "Tu perfil no puede registrar este tipo"
            else -> null
        }
        val errorCantidad = when {
            estado.cantidad.isBlank() -> "La cantidad es obligatoria"
            cantidad == null -> "La cantidad debe ser un número entero"
            cantidad <= 0 -> "La cantidad debe ser mayor a cero"
            esSalida && producto != null && cantidad > producto.stockActual ->
                "No puedes sacar más de ${producto.stockActual} unidades"
            else -> null
        }
        val errorMotivo = if (tipo == TipoMovimiento.AJUSTE && estado.motivo.isBlank())
            "El motivo es obligatorio en un ajuste" else null

        _estadoMovimiento.update {
            it.copy(errores = ErroresMovimiento(errorProducto, errorTipo, errorCantidad, errorMotivo))
        }
        return errorProducto == null && errorTipo == null && errorCantidad == null && errorMotivo == null
    }

    fun registrarMovimiento(usuario: Usuario): Boolean {
        if (!validarMovimiento(usuario.rol)) return false

        val estado = _estadoMovimiento.value
        val producto = productos.value.find { it.codigo == estado.codigoProducto } ?: return false
        val tipo = estado.tipo ?: return false
        val cantidad = estado.cantidad.trim().toIntOrNull() ?: return false

        val nuevoStock = when (tipo) {
            TipoMovimiento.ENTRADA -> producto.stockActual + cantidad
            TipoMovimiento.SALIDA_VENTA, TipoMovimiento.SALIDA_CONSUMO -> producto.stockActual - cantidad
            TipoMovimiento.AJUSTE -> cantidad
        }

        val movimiento = Movimiento(
            codigoProducto = producto.codigo,
            tipo = tipo,
            cantidad = cantidad,
            motivo = estado.motivo.trim().ifBlank { tipo.etiqueta },
            fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()),
            usuario = usuario.nombre
        )
        viewModelScope.launch { repositorio.registrarMovimiento(movimiento, nuevoStock) }

        _mensajeMovimiento.value = "${tipo.etiqueta} registrada. ${producto.nombre} queda con $nuevoStock u."
        _estadoMovimiento.value = FormularioMovimientoEstado(codigoProducto = producto.codigo)
        return true
    }
}
