package ui.screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    viewModel: InventarioViewModel,
    esAdministrador: Boolean,
    onProductoClick: (Producto) -> Unit,
    onAgregar: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kaiju · Catálogo") },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            if (esAdministrador) {
                FloatingActionButton(onClick = onAgregar) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar producto")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            items(productos) { producto ->
                TarjetaProducto(
                    producto = producto,
                    enAlerta = viewModel.enAlerta(producto),
                    onClick = { onProductoClick(producto) }
                )
            }
        }
    }
}