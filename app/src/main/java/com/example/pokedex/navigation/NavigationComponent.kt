package com.example.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.pokedex.ui.screens.pokemonList.PokemonListScreen

@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "pokemon_list_screen") {
        composable("pokemon_list_screen") {
            PokemonListScreen(navController = navController)
        }
        composable(
            route = "pokemon_detail_screen/{dominantColor}/{pokemonName}",
            arguments = listOf(
                navArgument("dominantColor") {
                    type = NavType.IntType
                },
                navArgument("pokemonName") {
                    type = NavType.StringType
                }
            )
        ) {
            val dominantColor = remember {
                it.arguments?.getInt("dominantColor") ?: Color.White
            }
            val pokemonName = remember {
                it.arguments?.getString("pokemonName")
            }
        }
    }
}