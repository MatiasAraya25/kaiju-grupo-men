package com.example.kaijugrupomen.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kaijugrupomen.model.Producto
import com.example.kaijugrupomen.model.Rol
import com.example.kaijugrupomen.ui.screen.PantallaAlertas
import com.example.kaijugrupomen.ui.screen.PantallaCatalogo
import com.example.kaijugrupomen.ui.screen.PantallaDetalleProducto
import com.example.kaijugrupomen.ui.screen.PantallaFormularioProducto
import com.example.kaijugrupomen.ui.screen.PantallaLogin
import com.example.kaijugrupomen.ui.screen.PantallaMovimiento
import com.example.kaijugrupomen.ui.theme.KaijuBlanco
import com.example.kaijugrupomen.ui.theme.KaijuGrisTexto
import com.example.kaijugrupomen.ui.theme.KaijuMorado
import com.example.kaijugrupomen.ui.theme.KaijuNegro
import com.example.kaijugrupomen.viewmodel.InventarioViewModel
import com.example.kaijugrupomen.viewmodel.InventarioViewModelFactory
import com.example.kaijugrupomen.viewmodel.SesionViewModel

@Composable
fun AppNavHost(
    sesionViewModel: SesionViewModel = viewModel(),
    inventarioViewModel: InventarioViewModel = viewModel(
        factory = InventarioViewModelFactory(LocalContext.current)
    )
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val usuarioActivo by sesionViewModel.usuarioActivo.collectAsState()
    val esAdministrador = usuarioActivo?.rol == Rol.ADMINISTRADOR
    val veAlertas = usuarioActivo?.rol == Rol.ADMINISTRADOR || usuarioActivo?.rol == Rol.ENCARGADO_INVENTARIO

    val rutasConMenu = listOf(Rutas.Catalogo.ruta, Rutas.Movimiento.ruta, Rutas.Alertas.ruta)

    val abrirDetalle: (Producto) -> Unit = { producto ->
        inventarioViewModel.seleccionarProducto(producto.codigo)
        navController.navigate(Rutas.Detalle.ruta)
    }

    val coloresMenu = NavigationBarItemDefaults.colors(
        selectedIconColor = KaijuBlanco,
        selectedTextColor = KaijuBlanco,
        indicatorColor = KaijuMorado,
        unselectedIconColor = KaijuGrisTexto,
        unselectedTextColor = KaijuGrisTexto
    )

    Scaffold(
        containerColor = KaijuNegro,
        bottomBar = {
            if (rutaActual in rutasConMenu) {
                NavigationBar(containerColor = KaijuNegro) {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Catalogo.ruta,
                        onClick = { navController.navigate(Rutas.Catalogo.ruta) { launchSingleTop = true } },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text("Catálogo") },
                        colors = coloresMenu
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Movimiento.ruta,
                        onClick = { navController.navigate(Rutas.Movimiento.ruta) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.Refresh, contentDescription = null) },
                        label = { Text("Movimientos") },
                        colors = coloresMenu
                    )
                    if (veAlertas) {
                        NavigationBarItem(
                            selected = rutaActual == Rutas.Alertas.ruta,
                            onClick = { navController.navigate(Rutas.Alertas.ruta) { launchSingleTop = true } },
                            icon = { Icon(Icons.Filled.Warning, contentDescription = null) },
                            label = { Text("Alertas") },
                            colors = coloresMenu
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Login.ruta,
            modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)
        ) {
            composable(Rutas.Login.ruta) {
                PantallaLogin(
                    viewModel = sesionViewModel,
                    onIngresoExitoso = {
                        navController.navigate(Rutas.Catalogo.ruta) {
                            popUpTo(Rutas.Login.ruta) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rutas.Catalogo.ruta) {
                PantallaCatalogo(
                    viewModel = inventarioViewModel,
                    esAdministrador = esAdministrador,
                    onProductoClick = abrirDetalle,
                    onAgregar = {
                        inventarioViewModel.limpiarFormulario()
                        navController.navigate(Rutas.Formulario.ruta)
                    },
                    onCerrarSesion = {
                        sesionViewModel.cerrarSesion()
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(Rutas.Catalogo.ruta) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rutas.Detalle.ruta) {
                PantallaDetalleProducto(
                    viewModel = inventarioViewModel,
                    esAdministrador = esAdministrador,
                    onVolver = { navController.popBackStack() },
                    onEditar = { producto ->
                        inventarioViewModel.prepararEdicion(producto)
                        navController.navigate(Rutas.Formulario.ruta)
                    }
                )
            }
            composable(Rutas.Formulario.ruta) {
                PantallaFormularioProducto(
                    viewModel = inventarioViewModel,
                    onVolver = { navController.popBackStack() },
                    onGuardado = { navController.popBackStack() }
                )
            }
            composable(Rutas.Movimiento.ruta) {
                usuarioActivo?.let { usuario ->
                    PantallaMovimiento(viewModel = inventarioViewModel, usuario = usuario)
                }
            }
            composable(Rutas.Alertas.ruta) {
                PantallaAlertas(
                    viewModel = inventarioViewModel,
                    onProductoClick = abrirDetalle
                )
            }
        }
    }
}
