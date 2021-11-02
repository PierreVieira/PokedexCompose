package com.example.pokedex.repository

import com.example.pokedex.data.remote.PokeApi
import com.example.pokedex.data.remote.responses.Pokemon
import com.example.pokedex.data.remote.responses.PokemonList
import com.example.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(private val api: PokeApi) {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        return getData { api.getPokemonList(limit, offset) }
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return getData { api.getPokemonInfo(pokemonName) }
    }

    private suspend fun <T> getData(requestBlock: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(requestBlock())
        } catch (e: Exception) {
            Resource.Error("An unknown error ocurred.")
        }
    }
}