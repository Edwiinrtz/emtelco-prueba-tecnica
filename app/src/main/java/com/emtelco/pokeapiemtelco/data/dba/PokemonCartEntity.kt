package com.emtelco.pokeapiemtelco.data.dba

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_cart")
data class PokemonCartEntity(
    @PrimaryKey @ColumnInfo(name = "id")  val id:String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "cant") val cant: Int
)