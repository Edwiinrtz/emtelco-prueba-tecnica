package com.emtelco.pokeapiemtelco.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emtelco.pokeapiemtelco.core.ConnectionHelper
import com.emtelco.pokeapiemtelco.core.NotificationsHelper
import com.emtelco.pokeapiemtelco.data.dba.PokemonCartEntity
import com.emtelco.pokeapiemtelco.data.dba.PokemonDao
import com.emtelco.pokeapiemtelco.data.dba.PokemonItemEntity
import com.emtelco.pokeapiemtelco.data.model.Pokemon
import com.emtelco.pokeapiemtelco.data.network.PokemonApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val api: PokemonApiClient,
    private val pokemonDao: PokemonDao,
    private val connectionHelper: ConnectionHelper,
    private val notificationsHelper: NotificationsHelper): ViewModel() {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    private val _internetStatus = MutableStateFlow(true)
    val internetStatus: StateFlow<Boolean> = _internetStatus

    init {
        checkInternetStatus()
        getPokemonList()
    }

    fun getPokemonList(){
        viewModelScope.launch {
            if(internetStatus.value){
                val results = api.getPokemonList().results
                _pokemonList.value = results
                pokemonDao.insertList(results.map { PokemonItemEntity(name = it.name, url = it.url) })
            }else{
                val databaseList = pokemonDao.getAllList()
                if(databaseList.isNotEmpty()) {
                    notificationsHelper.sendNotification("Internet", "Todos los recursos seran consultados en local")
                    Log.d("PokemonListViewModel", "Getting data from dba ${databaseList.size}")
                    _pokemonList.value = databaseList.map { Pokemon(name = it.name, url = it.url) }
                }else{
                    notificationsHelper.sendNotification("Database", "No tienes internet y la base de datos esta vacia. No tenemos nada que mostrar.")
                    Log.d("PokemonListViewModel", "Database is empty")
                    _pokemonList.value = emptyList()
                }
            }
        }
    }

    fun checkInternetStatus(){
        val internet = connectionHelper.isNetworkAvailable()
        if(_internetStatus.value != internet){
            if(internet){
                notificationsHelper.sendNotification("Internet", "Internet disponible")
                Log.d("PokemonListViewModel", "Internet disponible")
            }else{
                Log.d("PokemonListViewModel", "Internet no disponible")
            }
            _internetStatus.value = connectionHelper.isNetworkAvailable()
        }
    }

    fun addPokemonToCart(pokemonId: String, name: String){
        val pokemonCartEntity = PokemonCartEntity(name = name, id = pokemonId, price = 10.0, cant =  1)
        viewModelScope.launch {
            pokemonDao.insert(pokemonCartEntity)
        }
    }

}