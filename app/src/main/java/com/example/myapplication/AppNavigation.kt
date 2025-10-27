package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Define las rutas de la aplicación.
 */
object AppRoutes {
    const val LOGIN = "login"
    const val MAIN = "main"
}

/**
 * Composable principal de navegación.
 * Decide si mostrar la pantalla de Login o la pantalla principal (MainScreen)
 * basándose en si el nombre de usuario está presente en el ViewModel.
 */
@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val username by viewModel.username

    // Define el punto de inicio dependiendo de si el usuario ya inició sesión
    val startDestination = if (username == null) AppRoutes.LOGIN else AppRoutes.MAIN

    NavHost(navController = navController, startDestination = startDestination) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLogin = { name ->
                    viewModel.login(name)
                    // Navega a la pantalla principal y limpia la pila de navegación
                    navController.navigate(AppRoutes.MAIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.MAIN) {
            // Solo muestra MainScreen si el nombre de usuario no es nulo.
            // Esto evita un crash durante el cierre de sesión.
            username?.let { name ->
                MainScreen(
                    username = name,
                    onLogout = {
                        viewModel.logout()
                        // Navega al login y limpia la pila de navegación
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.MAIN) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
