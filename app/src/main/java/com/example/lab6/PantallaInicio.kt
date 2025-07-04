package com.example.lab6

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable

fun PantallaInicio(navController: NavController) {
    var dimensionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Juego de Bingo",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Reglas del juego
        Text(
            text = "Reglas: Marca una fila, columna, diagonal o un bloque 2x2 para ganar.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )


        OutlinedTextField(
            value = dimensionText,
            onValueChange = { dimensionText = it },
            label = { Text("Ingrese la matriz del Bingo") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón
        Button(onClick = {
            val dim = dimensionText.toIntOrNull()
            if (dim != null && dim > 0) {
                val uid = generarUID()
                navController.navigate("bingo/$dim/$uid")
            }
        }) {
            Text("GENERAR BINGO")
        }
    }
}

fun generarUID(): String {
    val letras = ('A'..'Z').toList()
    return (1..2).map { letras.random() }.joinToString("") +
            "x" + (10..99).random().toString() + letras.random()
}
