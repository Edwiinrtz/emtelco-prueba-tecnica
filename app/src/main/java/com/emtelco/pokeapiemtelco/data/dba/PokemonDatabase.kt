package com.emtelco.pokeapiemtelco.data.dba

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [PokemonCartEntity::class, PokemonItemEntity::class], version = 1)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}