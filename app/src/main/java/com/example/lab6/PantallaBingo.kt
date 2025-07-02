package com.example.lab6

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun PantallaBingo(dimension: Int, uid: String) {
    val context = LocalContext.current

    // ✅ Corregido: tts declarado fuera del bloque
    val tts = remember {
        TextToSpeech(context, null).apply {
            language = Locale("es", "ES")
        }
    }

    var tablero by remember { mutableStateOf(generarTablero(dimension)) }
    var marcado by remember { mutableStateOf(MutableList(dimension * dimension) { false }) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Juego de Bingo - UID: $uid", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(dimension),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(tablero) { index, numero ->
                val marcadoActual = marcado[index]
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            if (marcadoActual) Color.Blue else Color(0xFF673AB7),
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            if (!marcadoActual) {
                                marcado[index] = true
                                if (validarBingo(marcado, dimension)) {
                                    showDialog = true
                                    tts.speak("¡Bingo!", TextToSpeech.QUEUE_FLUSH, null, null)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (marcadoActual) "XX" else numero.toString(),
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            tablero = generarTablero(dimension)
            marcado = MutableList(dimension * dimension) { false }
        }) {
            Text("Regenerar Carta")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            },
            title = { Text("¡Bingo!") },
            text = { Text("Has ganado el juego. ¡Felicidades!") }
        )
    }
}

fun generarTablero(dimension: Int): List<Int> {
    val cantidad = dimension * dimension
    return (1..99).shuffled().take(cantidad)
}

fun validarBingo(marcado: List<Boolean>, dimension: Int): Boolean {
    for (i in 0 until dimension) {
        if ((0 until dimension).all { j -> marcado[i * dimension + j] }) return true
    }
    for (j in 0 until dimension) {
        if ((0 until dimension).all { i -> marcado[i * dimension + j] }) return true
    }
    if ((0 until dimension).all { i -> marcado[i * dimension + i] }) return true
    if ((0 until dimension).all { i -> marcado[i * dimension + (dimension - i - 1)] }) return true
    return false
}
