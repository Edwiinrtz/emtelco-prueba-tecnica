package com.emtelco.pokeapiemtelco.data.dba

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_cart")
    suspend fun getAllCart(): List<PokemonCartEntity>

    @Query("SELECT * FROM pokemon_item")
    suspend fun getAllList(): List<PokemonItemEntity>

    @Query("DELETE FROM pokemon_cart")
    suspend fun deleteAll()

    @Query("DELETE FROM pokemon_cart WHERE id = :id")
    suspend fun delete(id:String)

    @Query("UPDATE pokemon_cart SET cant = :cant WHERE id = :id")
    suspend fun updateCant(id: String, cant: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pokemon: PokemonCartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(pokemonList: List<PokemonItemEntity>)


}