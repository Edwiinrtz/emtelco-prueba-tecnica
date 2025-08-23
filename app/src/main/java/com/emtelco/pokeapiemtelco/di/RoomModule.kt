package com.emtelco.pokeapiemtelco.di

import android.content.Context
import androidx.room.Room
import com.emtelco.pokeapiemtelco.data.dba.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(context, PokemonDatabase::class.java, "pokemon_cart_db").build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(database: PokemonDatabase) = database.pokemonDao()

}