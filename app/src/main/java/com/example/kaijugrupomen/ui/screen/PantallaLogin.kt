package com.example.kaijugrupomen.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kaijugrupomen.R
import com.example.kaijugrupomen.ui.theme.KaijuBlanco
import com.example.kaijugrupomen.ui.theme.KaijuGrisTexto
import com.example.kaijugrupomen.ui.theme.KaijuNegro
import com.example.kaijugrupomen.ui.theme.KaijuRojoClaro
import com.example.kaijugrupomen.viewmodel.SesionViewModel

@Composable
fun PantallaLogin(viewModel: SesionViewModel, onIngresoExitoso: () -> Unit) {
    val seleccionado by viewModel.usuarioSeleccionado.collectAsState()
    val error by viewModel.errorLogin.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KaijuNegro)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_kaiju),
            contentDescription = "Logo Kaiju Serigrafía",
            modifier = Modifier.size(170.dp)
        )
        Text(
            text = "INVENTARIO",
            style = MaterialTheme.typography.headlineMedium,
            color = KaijuBlanco,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "Elige un usuario de prueba",
            color = KaijuGrisTexto,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        viewModel.usuariosDisponibles.forEach { usuario ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { viewModel.seleccionarUsuario(usuario) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = usuario.nombre, fontWeight = FontWeight.Bold)
                        Text(text = usuario.rol.etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (usuario == seleccionado) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Seleccionado",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        error?.let { Text(text = it, color = KaijuRojoClaro) }

        Button(
            onClick = { if (viewModel.ingresar()) onIngresoExitoso() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Ingresar")
        }
    }
}
