package com.emtelco.pokeapiemtelco.view.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.emtelco.pokeapiemtelco.R


@Composable
fun PokemonComponent(pokemonId: String, name: String, action: () -> Unit) {

    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = {
            action()
            Toast.makeText(context, "$name added to cart", Toast.LENGTH_SHORT).show()
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                imageUrl,
                contentDescription = "Image for $name",
                placeholder = painterResource(R.drawable.img),
            )
            Text(
                text = name.uppercase(),
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PokemonPreview() {

    PokemonComponent("1","bulbasaur"){}

}