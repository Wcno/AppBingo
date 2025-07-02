package com.example.lab6

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaInicio(navController: NavController) {
    var dimensionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = dimensionText,
            onValueChange = { dimensionText = it },
            label = { Text("Ingrese la dimensión de la matriz del Bingo") }
        )

        Spacer(modifier = Modifier.height(24.dp))

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
