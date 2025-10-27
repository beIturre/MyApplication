package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Pantalla de Inicio de Sesión
 */
@Composable
fun LoginScreen(onLogin: (String) -> Unit) {
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido al Cine", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Ingresa tu nombre") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onLogin(username) },
            enabled = username.isNotBlank()
        ) {
            Text("Entrar")
        }
    }
}

/**
 * Pantalla Principal que contiene el Scaffold, TopBar, BottomNav y
 * el host de navegación para las 3 ventanas internas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(username: String, onLogout: () -> Unit) {
    // NavController para la navegación interna (Inicio, Películas, Confitería)
    val mainNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CinemaxExtreme") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE), // Un color de ejemplo
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    // Muestra el nombre de usuario a la derecha
                    Text(
                        text = "Hola, $username",
                        modifier = Modifier.padding(end = 8.dp),
                        fontWeight = FontWeight.Medium
                    )
                    // Botón de Cerrar Sesión
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        bottomBar = {
            MainBottomNavigation(navController = mainNavController)
        }
    ) { paddingValues ->
        // Host de navegación para el contenido principal
        MainContentNavigation(
            navController = mainNavController,
            padding = paddingValues,
            username = username,
            onLogout = onLogout
        )
    }
}

/**
 * Define la barra de navegación inferior.
 */
@Composable
fun MainBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Inicio", Icons.Filled.Home, "home"),
        BottomNavItem("Películas", Icons.Filled.Theaters, "movies"),
        BottomNavItem("Confitería", Icons.Filled.Fastfood, "concessions"),
        BottomNavItem("Perfil", Icons.Filled.Person, "profile")
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {

                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


@Composable
fun MainContentNavigation(navController: NavHostController, padding: PaddingValues, username: String, onLogout: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = "home", // Inicia en la pantalla "home"
        modifier = Modifier.padding(padding)
    ) {
        composable("home") { HomeScreen() }
        composable("movies") { MoviesScreen() }
        composable("concessions") { ConcessionsScreen() }
        composable("profile") { ProfileScreen(username = username, onLogout = onLogout) }
    }
}


@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ventana de Inicio", fontSize = 22.sp)
    }
}

@Composable
fun MoviesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ventana de Películas", fontSize = 22.sp)
    }
}

@Composable
fun ConcessionsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ventana de Confitería", fontSize = 22.sp)
    }
}

@Composable
fun ProfileScreen(username: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hola, $username!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { /* TODO */ }) {
            Text("Historial de compras")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO */ }) {
            Text("Cambiar contraseña")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO */ }) {
            Text("Boletas")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLogout) {
            Text("Cerrar sesión")
        }
    }
}

// Clase de datos para los ítems de la barra de navegación
data class BottomNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)
