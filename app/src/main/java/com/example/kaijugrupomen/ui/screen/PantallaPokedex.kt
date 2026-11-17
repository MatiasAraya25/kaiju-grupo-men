package com.example.kaijugrupomen.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.kaijugrupomen.data.model.PokemonDto
import com.example.kaijugrupomen.ui.components.EncabezadoKaiju
import com.example.kaijugrupomen.viewmodel.PokedexViewModel

@Composable
fun PantallaPokedex(
    viewModel: PokedexViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { EncabezadoKaiju(titulo = "Pokédex secreta", onVolver = onVolver) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("¡Encontraste el easter egg!", style = MaterialTheme.typography.headlineSmall)
            Text("Busca por nombre o número. Por ejemplo: pikachu o 25.")
            OutlinedTextField(
                value = estado.consulta,
                onValueChange = viewModel::actualizarConsulta,
                label = { Text("Nombre o número") },
                singleLine = true,
                enabled = !estado.cargando,
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = viewModel::buscar, enabled = !estado.cargando) {
                Text("Buscar")
            }
            when {
                estado.cargando -> {
                    CircularProgressIndicator()
                    Text("Consultando PokéAPI…")
                }
                estado.error != null -> {
                    Text(estado.error!!, color = MaterialTheme.colorScheme.error)
                    TextButton(onClick = viewModel::buscar) { Text("Reintentar") }
                }
                estado.pokemon != null -> FichaPokemon(estado.pokemon!!)
                else -> Text("Escribe un Pokémon y presiona Buscar.")
            }
        }
    }
}

@Composable
private fun FichaPokemon(pokemon: PokemonDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "#${pokemon.id} · ${pokemon.name}",
                style = MaterialTheme.typography.headlineSmall
            )
            val imagen = pokemon.sprites.other?.showdown?.frontDefault ?: pokemon.sprites.frontDefault
            if (imagen == null) {
                Text("Imagen no disponible")
            } else {
                PokemonAnimado(url = imagen, nombre = pokemon.name)
            }
            Text("Tipos: " + pokemon.types.sortedBy { it.slot }.joinToString { it.type.name })
            Spacer(modifier = Modifier.height(4.dp))
            Text("Altura: ${pokemon.height / 10.0} m")
            Text("Peso: ${pokemon.weight / 10.0} kg")
        }
    }
}

@Composable
private fun PokemonAnimado(url: String, nombre: String) {
    val entrada = remember(url) { Animatable(-600f) }
    LaunchedEffect(url) {
        entrada.animateTo(0f, animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing))
    }

    val flotar by rememberInfiniteTransition(label = "flotar").animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 900), RepeatMode.Reverse),
        label = "flotar"
    )

    AsyncImage(
        model = url,
        contentDescription = "Imagen animada de $nombre",
        error = painterResource(android.R.drawable.ic_menu_report_image),
        contentScale = ContentScale.Fit,
        filterQuality = FilterQuality.None,
        modifier = Modifier
            .size(160.dp)
            .graphicsLayer {
                translationX = entrada.value
                translationY = flotar
            }
    )
}
