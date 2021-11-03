package com.example.pokedex.ui.screens.pokemonList.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.pokedex.data.models.PokedexListEntry
import com.example.pokedex.ui.screens.pokemonList.PokemonListViewModel
import com.example.pokedex.ui.theme.RobotoCondensed
import kotlinx.coroutines.launch

@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        modifier = modifier
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    colors = listOf(dominantColor, defaultDominantColor)
                )
            )
            .clickable {
                navController.navigate("pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}")
            },
        contentAlignment = Center
    ) {
        CardContent(
            imageUrl = entry.imageUrl,
            pokemonName = entry.pokemonName,
            backgroundColorModifierAction = { drawable ->
                viewModel.calcDominantColor(drawable) { color ->
                    dominantColor = color
                }
            }
        )
    }
}

@Composable
private fun CardContent(
    imageUrl: String,
    pokemonName: String,
    backgroundColorModifierAction: (Drawable) -> Unit
) {
    Column {
        PokemonImage(
            imageUrl = imageUrl,
            pokemonName = pokemonName,
            backgroundColorModifierAction = backgroundColorModifierAction
        )
        PokemonTitle(pokemonName = pokemonName)
    }
}

@Composable
private fun ColumnScope.PokemonImage(
    imageUrl: String,
    pokemonName: String,
    backgroundColorModifierAction: (Drawable) -> Unit
) {
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
        }
    )
    Box(
        modifier = Modifier.align(CenterHorizontally),
        contentAlignment = Center
    ) {
        Image(
            modifier = Modifier
                .size(120.dp),
            painter = painter,
            contentDescription = pokemonName
        )
        (painter.state as? ImagePainter.State.Success)?.let { successState ->
            LaunchedEffect(key1 = Unit) {
                val drawable = successState.result.drawable
                backgroundColorModifierAction(drawable)
            }
        } ?: CircularProgressIndicator(
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
private fun PokemonTitle(pokemonName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = pokemonName,
        fontFamily = RobotoCondensed,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )
}
