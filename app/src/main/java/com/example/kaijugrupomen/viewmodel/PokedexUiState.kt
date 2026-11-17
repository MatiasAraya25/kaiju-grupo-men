package com.example.kaijugrupomen.viewmodel

import com.example.kaijugrupomen.data.model.PokemonDto

data class PokedexUiState(
    val consulta: String = "rayquaza",
    val cargando: Boolean = false,
    val pokemon: PokemonDto? = null,
    val error: String? = null
)
