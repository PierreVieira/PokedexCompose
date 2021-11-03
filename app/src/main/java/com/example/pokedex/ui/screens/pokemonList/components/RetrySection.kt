package com.example.pokedex.ui.screens.pokemonList.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RetrySection(error: String, onRetry: () -> Unit) {
    Column {
        Text(
            text = error,
            style = TextStyle(
                color = Color.Red,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.align(CenterHorizontally),
            onClick = { onRetry() }
        ) {
            Text(text = "Retry")
        }
    }
}