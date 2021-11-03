package com.example.pokedex.ui.screens.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedex.data.models.PokedexListEntry
import com.example.pokedex.data.remote.responses.PokemonList
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Constants
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    companion object {
        private const val POKEMON_IMAGE_BASE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    }

    private var currentPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    private set
    var loadError = mutableStateOf("")
    private set
    var isLoading = mutableStateOf(false)
    private set
    var endReached = mutableStateOf(false)
    private set

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.getPokemonList(
                limit = Constants.PAGE_SIZE,
                offset = currentPage * Constants.PAGE_SIZE
            )
            when (result) {
                is Resource.Success -> successAction(result.data!!)
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun successAction(data: PokemonList) {
        endReached.value = currentPage * Constants.PAGE_SIZE >= data.count
        val pokedexEntries = data.results.mapIndexed { _, entry ->
            val number = if (entry.url.endsWith("/")) {
                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                entry.url.takeLastWhile { it.isDigit() }
            }
            val url = buildImageUrl(number)
            PokedexListEntry(
                entry.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                url,
                number.toInt()
            )
        }
        currentPage++
        loadError.value = ""
        isLoading.value = false
        pokemonList.value += pokedexEntries
    }

    private fun buildImageUrl(number: String) = "$POKEMON_IMAGE_BASE_URL${number}.png"

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bpm = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bpm).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}