package com.example.kaijugrupomen.data.remote

import com.example.kaijugrupomen.data.model.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{nombre}")
    suspend fun obtenerPokemon(@Path("nombre") nombre: String): PokemonDto
}
