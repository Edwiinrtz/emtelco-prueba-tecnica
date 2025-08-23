package com.emtelco.pokeapiemtelco

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emtelco.pokeapiemtelco.core.PermissionHelper
import com.emtelco.pokeapiemtelco.view.screens.CartScreen
import com.emtelco.pokeapiemtelco.view.screens.PokemonListScreen
import com.emtelco.pokeapiemtelco.view.ui.theme.PokeApiEmtelcoTheme
import com.emtelco.pokeapiemtelco.viewmodel.PokemonCartViewModel
import com.emtelco.pokeapiemtelco.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val pokemonListViewModel by viewModels<PokemonListViewModel>()
    private val pokemonCartViewModel by viewModels<PokemonCartViewModel>()

    @Inject
    lateinit var permissionHelper: PermissionHelper

    private val requestPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {Log.d(this.packageName, "USER DENIED PERMISSION") } else { Log.d(this.packageName, "USER GRANTED PERMISSION") }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) permissionHelper.requestPermission(android.Manifest.permission.POST_NOTIFICATIONS, requestPermissionLauncher, this)

        enableEdgeToEdge()
        setContent {
            PokeApiEmtelcoTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "listScreen") {
                    composable("listScreen") {
                        PokemonListScreen(pokemonListViewModel, goCart = { navController.navigate("cart") })
                    }
                    composable("cart") {
                        CartScreen(pokemonCartViewModel, activity  = this@MainActivity, goBack = { navController.navigate("listScreen") })
                    }
                }
            }
        }
    }

}
