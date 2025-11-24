package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import kotlin.random.Random

// --- Clases de Datos ---
data class Movie(
    val id: Int, val title: String, val director: String, val genre: String, 
    val synopsis: String, val releaseDate: String, val availableTimes: List<String>, val imageUrl: String
)
data class ConcessionItem(val name: String, val price: Double)
data class Seat(val id: String, val row: Char, val number: Int, var status: SeatStatus)
enum class SeatStatus { AVAILABLE, SELECTED, OCCUPIED }

// --- Datos de Ejemplo ---
val sampleMovies = listOf(
    Movie(1, "Mundos Paralelos", "Dr. A. Einstein", "Ciencia Ficción", "Un viaje a través de dimensiones...", "25 Dic 2024", listOf("15:00", "17:30", "20:00"), "https://picsum.photos/seed/mundos/400/600"),
    Movie(2, "El Último Código", "Sra. L. Lovelace", "Thriller", "Una programadora descubre un secreto...", "10 Ene 2025", listOf("16:15", "18:45", "21:15"), "https://picsum.photos/seed/codigo/400/600"),
    Movie(3, "La Sombra del Tiempo", "Sr. C. Nolan", "Misterio", "Un detective debe resolver un caso...", "14 Feb 2025", listOf("14:00", "19:00", "22:00"), "https://picsum.photos/seed/sombra/400/600"),
    Movie(4, "Aventura en la IA", "Sra. S. Johnson", "Aventura", "Un grupo de adolescentes queda atrapado...", "20 Mar 2025", listOf("15:30", "18:00"), "https://picsum.photos/seed/ia/400/600"),
    Movie(5, "El Despertar Cuántico", "Dr. M. Planck", "Drama", "Un científico debe arriesgarlo todo...", "05 Abr 2025", listOf("17:00", "20:30"), "https://picsum.photos/seed/cuantico/400/600"),
    Movie(6, "Planeta Olvidado", "Sr. G. Lucas", "Ciencia Ficción", "Exploradores espaciales encuentran un planeta...", "18 May 2025", listOf("16:00", "19:45", "22:30"), "https://picsum.photos/seed/planeta/400/600"),
    Movie(7, "Código Cero", "Sr. A. Turing", "Documental", "La historia no contada de los héroes...", "30 Jun 2025", listOf("18:30"), "https://picsum.photos/seed/cero/400/600")
)

val sampleConcessions = listOf(
    ConcessionItem("Palomitas Grandes", 5.50), ConcessionItem("Refresco Mediano", 3.75),
    ConcessionItem("Nachos con Queso", 6.20), ConcessionItem("Hot Dog", 4.50),
    ConcessionItem("Chocolates", 2.80), ConcessionItem("Agua Embotellada", 2.00)
)


@Composable
fun LoginScreen(onLogin: (String) -> Unit) {
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
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
        Button(onClick = { onLogin(username) }, enabled = username.isNotBlank()) {
            Text("Entrar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(username: String, onLogout: () -> Unit) {
    val mainNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CinemaxExtreme") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    Text(text = "Hola, $username", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Medium)
                    IconButton(onClick = onLogout) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            MainBottomNavigation(navController = mainNavController)
        }
    ) { paddingValues ->
        MainContentNavigation(
            navController = mainNavController,
            padding = paddingValues,
            username = username,
            onLogout = onLogout
        )
    }
}

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
                selected = currentRoute?.startsWith(item.route) == true,
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
    NavHost(navController = navController, startDestination = "movies", modifier = Modifier.padding(padding)) {
        composable("home") { HomeScreen() }
        composable("movies") { 
            MoviesScreen(onMovieClick = { movieId -> navController.navigate("movie_detail/$movieId") }) 
        }
        composable("concessions") { ConcessionsScreen() }
        composable("profile") { ProfileScreen(username = username, onLogout = onLogout) }
        
        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            sampleMovies.find { it.id == movieId }?.let { movie ->
                MovieDetailScreen(
                    movie = movie, 
                    onNavigateBack = { navController.popBackStack() },
                    onContinue = { time -> navController.navigate("seat_selection/${movie.id}/$time") }
                )
            }
        }

        composable(
            route = "seat_selection/{movieId}/{time}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            val time = backStackEntry.arguments?.getString("time") ?: ""
            sampleMovies.find { it.id == movieId }?.let { movie ->
                SeatSelectionScreen(
                    movie = movie,
                    time = time,
                    onNavigateBack = { navController.popBackStack() },
                    onConfirm = { selectedSeats ->
                        val seatIds = selectedSeats.joinToString(",") { it.id }
                        navController.navigate("payment/${movie.id}/$time/$seatIds")
                    }
                )
            }
        }

        composable(
            route = "payment/{movieId}/{time}/{seatIds}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType },
                navArgument("time") { type = NavType.StringType },
                navArgument("seatIds") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            val time = backStackEntry.arguments?.getString("time") ?: ""
            val seatIds = backStackEntry.arguments?.getString("seatIds") ?: ""
            PaymentScreen(
                onNavigateBack = { navController.popBackStack() },
                onConfirmPayment = { navController.navigate("confirmation/${movieId}/$time/$seatIds") }
            )
        }
        
        composable(
            route = "confirmation/{movieId}/{time}/{seatIds}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType },
                navArgument("time") { type = NavType.StringType },
                navArgument("seatIds") { type = NavType.StringType }
            )
        ) { backStackEntry ->
             val movieId = backStackEntry.arguments?.getInt("movieId")
            val time = backStackEntry.arguments?.getString("time") ?: ""
            val seatIds = backStackEntry.arguments?.getString("seatIds") ?: ""
            sampleMovies.find { it.id == movieId }?.let { movie ->
                ConfirmationScreen(
                    movie = movie,
                    time = time,
                    selectedSeatIds = seatIds,
                    onFinish = { 
                        navController.navigate("movies") {
                            popUpTo("movies") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ventana de Inicio", fontSize = 22.sp)
    }
}

@Composable
fun MoviesScreen(onMovieClick: (Int) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(sampleMovies) { movie ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onMovieClick(movie.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(movie.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Director: ${movie.director}", fontSize = 16.sp)
                    Text("Género: ${movie.genre}", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ConcessionsScreen() {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(sampleConcessions) { item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.name, fontSize = 18.sp)
                Text("$%.2f".format(item.price), fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(movie: Movie, onNavigateBack: () -> Unit, onContinue: (String) -> Unit) {
    var selectedTime by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie.title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { selectedTime?.let { onContinue(it) } },
                enabled = selectedTime != null,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Continuar")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it).padding(16.dp)) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = movie.imageUrl,
                        contentDescription = "Póster de ${movie.title}",
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text("Sinopsis", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(movie.synopsis, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Fecha de Estreno", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(movie.releaseDate, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Horas de Función Disponibles", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    movie.availableTimes.forEach { time ->
                        Button(
                            onClick = { selectedTime = time },
                            colors = if (selectedTime == time) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Text(time)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SeatLegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.size(16.dp).background(color, RoundedCornerShape(2.dp)))
        Text(text, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelectionScreen(movie: Movie, time: String, onNavigateBack: () -> Unit, onConfirm: (List<Seat>) -> Unit) {
    val seatRows = 'A'..'H'
    val seatNumbers = 1..8
    var seats by remember { mutableStateOf(
        seatRows.flatMap { row ->
            seatNumbers.map { number ->
                val id = "$row$number"
                val isOccupied = Random.nextFloat() < 0.2
                Seat(id, row, number, if(isOccupied) SeatStatus.OCCUPIED else SeatStatus.AVAILABLE)
            }
        }
    )}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar Asientos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
             Button(
                onClick = { onConfirm(seats.filter { it.status == SeatStatus.SELECTED }) },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                enabled = seats.any { it.status == SeatStatus.SELECTED }
            ) {
                Text("Confirmar Selección")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("PANTALLA", modifier = Modifier.background(Color.Gray, RoundedCornerShape(4.dp)).padding(horizontal = 24.dp, vertical = 4.dp), color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                seatRows.forEach { rowChar ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "$rowChar", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        val seatsInRow = seats.filter { seat -> seat.row == rowChar }
                        seatsInRow.forEach { seat ->
                            val color = when (seat.status) {
                                SeatStatus.AVAILABLE -> Color.LightGray
                                SeatStatus.SELECTED -> MaterialTheme.colorScheme.primary
                                SeatStatus.OCCUPIED -> Color.DarkGray
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color)
                                    .clickable(enabled = seat.status != SeatStatus.OCCUPIED) {
                                        seats = seats.map {
                                            if (it.id == seat.id) {
                                                it.copy(status = if (it.status == SeatStatus.AVAILABLE) SeatStatus.SELECTED else SeatStatus.AVAILABLE)
                                            } else it
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "${seat.number}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SeatLegendItem(color = Color.LightGray, text = "Disponible")
                SeatLegendItem(color = MaterialTheme.colorScheme.primary, text = "Seleccionado")
                SeatLegendItem(color = Color.DarkGray, text = "Ocupado")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(onNavigateBack: () -> Unit, onConfirmPayment: () -> Unit) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Simulación de Pago") }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            })
        }
    ) {
        Column(modifier = Modifier.padding(it).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Transbank", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005EB8))
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(value = cardNumber, onValueChange = { cardNumber = it }, label = { Text("Número de Tarjeta") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = expiryDate, onValueChange = { expiryDate = it }, label = { Text("MM/AA") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = cvv, onValueChange = { cvv = it }, label = { Text("CVV") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onConfirmPayment, modifier = Modifier.fillMaxWidth()) {
                Text("Ir al Pago")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(movie: Movie, time: String, selectedSeatIds: String, onFinish: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("¡Compra Exitosa!") }) },
        bottomBar = {
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Finalizar")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Default.Done, contentDescription = "Éxito", tint = Color(0xFF007A33), modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("¡Disfruta la función!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Película: ${movie.title}", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hora: $time", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Asientos: $selectedSeatIds", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}


@Composable
fun ProfileScreen(username: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hola, $username!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { /* TODO */ }) { Text("Historial de compras") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO */ }) { Text("Cambiar contraseña") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO */ }) { Text("Boletas") }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLogout) { Text("Cerrar sesión") }
    }
}

data class BottomNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)
