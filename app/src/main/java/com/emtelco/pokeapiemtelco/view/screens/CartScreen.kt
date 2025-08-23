package com.emtelco.pokeapiemtelco.view.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.VibratorManager
import android.service.autofill.OnClickAction
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.emtelco.pokeapiemtelco.view.components.CartItemComponent
import com.emtelco.pokeapiemtelco.viewmodel.PokemonCartViewModel


@Composable
fun CartScreen(viewModel: PokemonCartViewModel, goBack: () -> Unit, activity:FragmentActivity) {

    LaunchedEffect(true) {
        viewModel.getPokemonCartList()
    }

    val focusManager = LocalFocusManager.current
    val pokemonCartList by viewModel.pokemonCartList.collectAsState()
    val totalCost by viewModel.totalCost.collectAsState()


    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

    Scaffold(
        modifier = Modifier.clickable { focusManager.clearFocus() },
        topBar = {
            Row (modifier = Modifier.fillMaxWidth().padding(0.dp, 32.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = {goBack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
            }
        },
        bottomBar = {
            Row(modifier = Modifier
                .padding(16.dp)
                .height(64.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Button(onClick = { viewModel.pay(activity) }, modifier = Modifier.padding(16.dp), enabled = pokemonCartList.isNotEmpty() && totalCost > 0) { Text("Pay") }
                Text("Total: $totalCost", modifier = Modifier.padding(16.dp), fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(pokemonCartList) { pokemonCartItem ->
                CartItemComponent(
                    pokemonCartItem,
                    dropPokemon = {
                        viewModel.dropPokemon(pokemonCartItem.id)
                        vibrator.vibrate(CombinedVibration.createParallel(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))) },
                    updateCant = {
                        it -> viewModel._pokemonCartList.value.find { it.id == pokemonCartItem.id }?.cant = it
                        viewModel.updatePokemonCart(pokemonCartItem)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview(showBackground = true)
@Composable
private fun CartScreenPreview() {
    /*val pokemonCartList =
        listOf(PokemonCartItem("Bulbasaur", "1", 1), PokemonCartItem("Ivysaur", "2", 2))
    val viewModel = PokemonCartViewModel()
    viewModel._pokemonCartList.value = pokemonCartList
    CartScreen(viewModel){}*/
}