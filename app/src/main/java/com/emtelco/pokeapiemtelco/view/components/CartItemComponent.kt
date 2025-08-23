package com.emtelco.pokeapiemtelco.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.emtelco.pokeapiemtelco.R
import com.emtelco.pokeapiemtelco.data.model.Pokemon
import com.emtelco.pokeapiemtelco.data.model.PokemonCartItem
import okhttp3.internal.wait


@Composable
fun CartItemComponent(pokemon: PokemonCartItem, updateCant:(Int)->Unit, dropPokemon: () -> Unit) {

    var cant by remember { mutableStateOf(pokemon.cant.toString()) }
    val focusManager = LocalFocusManager.current
    val unitPrice = 10.0
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
    Row(modifier = Modifier.fillMaxWidth(1f).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = cant,
            onValueChange = {
                cant = it.replace("-", "").replace(",", "").replace(".", "").trim().ifEmpty { "" }
                if(cant.length > 4) cant = cant.substring(0, 4)
                updateCant( if (cant.isEmpty()) 0 else cant.toInt()) },
            modifier = Modifier.fillMaxWidth(.25f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            label = { Text("N°") }
        )
        AsyncImage(
            imageUrl,
            contentDescription = "Image for ${pokemon.name}",
            placeholder = painterResource(R.drawable.img),
            modifier = Modifier.padding(8.dp).size(64.dp).fillMaxWidth(.2f)
        )
        Column(modifier = Modifier.fillMaxWidth(.8f)){
            Text(pokemon.name, modifier = Modifier.fillMaxWidth(), fontSize = 26.sp, fontWeight =  FontWeight.Bold)
            Text((unitPrice * if (cant.isEmpty()) 0 else cant.toInt()).toString(), modifier = Modifier, fontSize = 16.sp, fontWeight =  FontWeight.SemiBold)
        }
        IconButton(onClick = {
            focusManager.clearFocus()
            dropPokemon() }){
            Icon(Icons.Outlined.Delete, contentDescription = "Drop")
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun CartItemComponentPreview() {
    CartItemComponent(PokemonCartItem("Bulbasaur", "1", 1000), {}){}
}