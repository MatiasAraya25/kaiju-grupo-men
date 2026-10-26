package com.example.kaijugrupomen.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kaijugrupomen.model.Categorias
import com.example.kaijugrupomen.model.Producto
import com.example.kaijugrupomen.ui.components.EncabezadoKaiju
import com.example.kaijugrupomen.ui.components.SelectorOpcion
import com.example.kaijugrupomen.ui.components.TarjetaProducto
import com.example.kaijugrupomen.viewmodel.InventarioViewModel

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
    val busqueda by viewModel.busqueda.collectAsState()
    val categoria by viewModel.categoriaFiltro.collectAsState()
    val visibles = viewModel.filtrarProductos(productos, busqueda, categoria)

    Scaffold(
        topBar = {
            EncabezadoKaiju(
                titulo = "Catálogo",
                acciones = {
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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = viewModel::actualizarBusqueda,
                label = { Text("Buscar por nombre o código") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            SelectorOpcion(
                textoBoton = "Categoría: ${categoria ?: "Todas"}",
                opciones = listOf("Todas") + Categorias.lista,
                onSeleccionar = { indice ->
                    viewModel.filtrarPorCategoria(if (indice == 0) null else Categorias.lista[indice - 1])
                },
                error = null,
                icono = Icons.Filled.Menu
            )

            LazyColumn {
                items(visibles) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        enAlerta = viewModel.enAlerta(producto),
                        onClick = { onProductoClick(producto) }
                    )
                }
            }
        }
    }
}
