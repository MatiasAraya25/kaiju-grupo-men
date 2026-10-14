package navigation

@Composable
fun AppNavHost(
    sesionViewModel: SesionViewModel = viewModel(),
    inventarioViewModel: InventarioViewModel = viewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val usuarioActivo by sesionViewModel.usuarioActivo.collectAsState()
    val esAdministrador = usuarioActivo?.rol == Rol.ADMINISTRADOR

    val rutasConMenu = listOf(Rutas.Catalogo.ruta, Rutas.Movimiento.ruta, Rutas.Alertas.ruta)

    val abrirDetalle: (Producto) -> Unit = { producto ->
        inventarioViewModel.seleccionarProducto(producto.codigo)
        navController.navigate(Rutas.Detalle.ruta)
    }

    Scaffold(
        bottomBar = {
            if (rutaActual in rutasConMenu) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Catalogo.ruta,
                        onClick = { navController.navigate(Rutas.Catalogo.ruta) { launchSingleTop = true } },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text("Catálogo") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Movimiento.ruta,
                        onClick = { navController.navigate(Rutas.Movimiento.ruta) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.Refresh, contentDescription = null) },
                        label = { Text("Movimientos") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Alertas.ruta,
                        onClick = { navController.navigate(Rutas.Alertas.ruta) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.Warning, contentDescription = null) },
                        label = { Text("Alertas") }
                    )
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
                    onVolver = { navController.popBackStack() }
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
                PantallaMovimiento()
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