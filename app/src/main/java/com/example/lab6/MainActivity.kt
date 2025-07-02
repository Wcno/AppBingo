package com.example.lab6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab6.ui.theme.Lab6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab6Theme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "inicio") {
                        composable("inicio") {
                            PantallaInicio(navController)
                        }
                        composable(
                            route = "bingo/{dimension}/{uid}",
                            arguments = listOf(
                                navArgument("dimension") { type = NavType.IntType },
                                navArgument("uid") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val dimension = backStackEntry.arguments?.getInt("dimension") ?: 5
                            val uid = backStackEntry.arguments?.getString("uid") ?: "UID"
                            PantallaBingo(dimension, uid)
                        }
                    }
                }
            }
        }
    }
}
