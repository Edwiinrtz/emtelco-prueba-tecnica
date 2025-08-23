package com.emtelco.pokeapiemtelco.di

import android.content.Context
import com.emtelco.pokeapiemtelco.core.ConnectionHelper
import com.emtelco.pokeapiemtelco.data.network.PokemonApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): PokemonApiClient {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectionHelper(@ApplicationContext context: Context): ConnectionHelper {
        return  ConnectionHelper(context);
    }
}