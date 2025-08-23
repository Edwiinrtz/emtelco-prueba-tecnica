package com.emtelco.pokeapiemtelco.data.network

import com.emtelco.pokeapiemtelco.data.model.PokemonApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiClient {
    @GET("pokemon?limit=2000")
    suspend fun getPokemonList(@Query("offset") offset: Int = 0): PokemonApiResponse
}