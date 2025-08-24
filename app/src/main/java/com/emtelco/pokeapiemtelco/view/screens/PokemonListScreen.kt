package com.emtelco.pokeapiemtelco.view.screens

import android.content.Context
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emtelco.pokeapiemtelco.view.components.PokemonComponent
import com.emtelco.pokeapiemtelco.viewmodel.PokemonListViewModel
import kotlinx.coroutines.delay


@Composable
fun PokemonListScreen(viewModel: PokemonListViewModel, goCart: () -> Unit){

    val pokemonList by viewModel.pokemonList.collectAsState()
    val internetStatus by viewModel.internetStatus.collectAsState()

    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

    LaunchedEffect(internetStatus) {
        while (true) {
            viewModel.checkInternetStatus()
            if (internetStatus) {
                viewModel.getPokemonList()
            }
            delay(5000L)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(0.dp),
        bottomBar = {
            Row(modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.End){
                SmallFloatingActionButton(onClick = { goCart() }, shape = CircleShape) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                }
            }
        }
    ){ padding ->

        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(padding)) {
                items(pokemonList){ pokemon ->
                    val pokemonId = pokemon.url.split("/").dropLast(1).last()
                    PokemonComponent(pokemonId = pokemonId, name = pokemon.name){
                        viewModel.addPokemonToCart(pokemonId = pokemonId, name = pokemon.name)
                        vibrator.vibrate(CombinedVibration.createParallel(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)))
                    }
                }
        }
    }
}

@Preview
@Composable
private fun PokemonListScreenPreview() {
    //val pokemonList = listOf(Pokemon("Bulbasaur", "1"), Pokemon("Ivysaur", "2"), Pokemon("Venusaur", "3"), Pokemon("Charmander", "4"))
    //PokemonListScreen(pokemonList)
}