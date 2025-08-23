package com.emtelco.pokeapiemtelco.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emtelco.pokeapiemtelco.core.AuthHelper
import com.emtelco.pokeapiemtelco.core.NotificationsHelper
import com.emtelco.pokeapiemtelco.data.dba.PokemonDao
import com.emtelco.pokeapiemtelco.data.model.PokemonCartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonCartViewModel @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val authHelper: AuthHelper,
    private val notificationsHelper: NotificationsHelper
) : ViewModel() {


    val _pokemonCartList = MutableStateFlow<List<PokemonCartItem>>(emptyList())
    val pokemonCartList = _pokemonCartList

    val _totalCost = MutableStateFlow(0.0)
    val totalCost = _totalCost

    fun dropPokemon(pokemonId: String) {
        viewModelScope.launch {
            pokemonDao.delete(pokemonId)
            getPokemonCartList()
        }

    }

    fun updatePokemonCart(pokemon: PokemonCartItem) {
        viewModelScope.launch {
            pokemonDao.updateCant(pokemon.id, pokemon.cant)
        }.invokeOnCompletion {
            _pokemonCartList.value =
                _pokemonCartList.value.map { if (it.id == pokemon.id) pokemon else it }
            _totalCost.value = _pokemonCartList.value.sumOf { it.price * it.cant }

        }
    }

    fun getPokemonCartList() {
        viewModelScope.launch {
            _pokemonCartList.value = pokemonDao.getAllCart().map {
                PokemonCartItem(
                    name = it.name, id = it.id, cant = it.cant, price = it.price
                )
            }
            _totalCost.value = _pokemonCartList.value.sumOf { it.price * it.cant }
        }
    }

    fun pay(activity: FragmentActivity) {

        authHelper.authenticate(activity, onSucceeded = {

            viewModelScope.launch {

                pokemonDao.deleteAll()
                _pokemonCartList.value = emptyList()
                _totalCost.value = _pokemonCartList.value.sumOf { it.price * it.cant }
                notificationsHelper.sendNotification("Compra realizada", "¡¡Ahora eres un maestro Pokemon!! Recuerdanos cuuando seas Famoso.")
            }

        }, onFailed = {
            notificationsHelper.sendNotification("Autenticación", "Autenticación fallida")
        })
    }
}