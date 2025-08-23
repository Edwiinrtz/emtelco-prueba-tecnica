package com.emtelco.pokeapiemtelco.data.model

data class PokemonApiResponse (
    val count:Int=0,
    val next:String="",
    val previous:String="",
    val results: List<Pokemon>
)