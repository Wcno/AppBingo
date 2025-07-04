package com.example.lab6

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import java.util.*

@Composable
fun PantallaBingo(dimension: Int, uid: String) {
    val context = LocalContext.current

    val tts = remember {
        TextToSpeech(context, null).apply {
            language = Locale("es", "ES")
        }
    }

    var tablero by remember { mutableStateOf(generarTablero(dimension)) }
    val marcado = remember { mutableStateListOf<Boolean>().apply { repeat(dimension * dimension) { add(false) } } }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Juego de Bingo - UID: $uid", fontWeight = FontWeight.Bold, fontSize = 20.sp)

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
                        .border(
                            width = 3.dp,
                            color = Color.Red,
                            shape = CircleShape
                        )
                        .background(
                            color = if (marcadoActual) Color.Blue else Color(0xFF673AB7),
                            shape = CircleShape
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
                        text = if (marcadoActual) "X" else numero.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            tablero = generarTablero(dimension)
            marcado.clear()
            repeat(dimension * dimension) { marcado.add(false) }
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
    // Horizontal
    for (i in 0 until dimension) {
        if ((0 until dimension).all { j -> marcado[i * dimension + j] }) return true
    }

    // Vertical
    for (j in 0 until dimension) {
        if ((0 until dimension).all { i -> marcado[i * dimension + j] }) return true
    }

    // Diagonal ↘
    if ((0 until dimension).all { i -> marcado[i * dimension + i] }) return true

    // Diagonal
    if ((0 until dimension).all { i -> marcado[i * dimension + (dimension - i - 1)] }) return true

    for (i in 0 until dimension - 1) {
        for (j in 0 until dimension - 1) {
            val idx = i * dimension + j
            if (
                marcado[idx] &&
                marcado[idx + 1] &&
                marcado[idx + dimension] &&
                marcado[idx + dimension + 1]
            ) {
                return true
            }
        }
    }

    return false
}
