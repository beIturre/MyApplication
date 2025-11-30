package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

// --- Clases de Datos ---
data class Movie(val id: Int, val title: String, val director: String, val genre: String, val synopsis: String, val releaseDate: String, val availableTimes: List<String>, val imageUrl: String)
data class ConcessionItem(val name: String, val price: Double, val imageUrl: String)
data class Seat(val id: String, val row: Char, val number: Int, var status: SeatStatus)
enum class SeatStatus { AVAILABLE, SELECTED, OCCUPIED }

// --- Listas de productos y peliculas ---
val sampleMovies = listOf(
    Movie(1, "Kimetsu No Yaiba: Tren Infinito", "Haruo Sotozaki", "Accion/anime", "Narra la misión de Tanjiro Kamado y sus compañeros del Cuerpo de Matademonios para investigar una serie de desapariciones misteriosas en un tren llamado Tren Infinito. En este viaje, se unen al Pilar de las Llamas, Kyojuro Rengoku, para enfrentar al demonio Enmu, una de las Lunas Inferiores que ha tendido una trampa mortal a bordo del tren.", "25 Ene 2026", listOf("15:00", "17:30", "20:00"), "https://www.selecta-vision.com/wp-content/uploads/2024/07/Los-guardianes-de-la-noche-70x100-1-scaled-1-1448x2048.jpg"),
    Movie(2, "Kimetsu No Yaiba: Castillo Infinito", "Haruo Sotozaki", "Anime de acción, fantasía oscura", "Se centra en la batalla decisiva entre Tanjiro Kamado, los Pilares y la Compañía Cazademonios contra Muzan Kibutsuji y las Lunas Superiores dentro del Castillo Infinito, el escondite interdimensional de Muzan.", "10 Ene 2026", listOf("16:15", "18:45", "21:15"), "https://i0.wp.com/tomatazos.buscafs.com/2025/08/Demon-Slayer-Kimetsu-no-Yaiba-Castillo-infinito-Poster-1-1.jpg?fit=1500,2250&quality=75&strip=all"),
    Movie(3, "Chainsaw Man - La película: Arco de Reze", "Tatsuya Yoshihara ", "acción, aventura, fantasía oscura y terror", "Chainsaw Man - La película: Arco de Reze sigue a Denji, quien se fusionó con el demonio motosierra Pochita para convertirse en Chainsaw Man tras ser traicionado por la yakuza. Una misteriosa chica llamada Reze entra en su vida, desencadenando una brutal guerra entre demonios, cazadores y enemigos ocultos, donde Denji enfrenta su batalla más mortífera impulsado por el amor en un mundo sin reglas.", "14 Feb 2026", listOf("14:00", "19:00", "22:00"), "https://m.media-amazon.com/images/M/MV5BMzk1ZmNmMmMtNTc5OS00ZTMzLWIzNTMtZDQwNDNjYTU0YzU2XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg"),
    Movie(4, "Aventura en la IA", "Sra. S. Johnson", "Aventura", "Un grupo de adolescentes queda atrapado...", "20 Mar 2026", listOf("15:30", "18:00"), "https://picsum.photos/seed/ia/400/600"),
    Movie(5, "El Despertar Cuántico", "Dr. M. Planck", "Drama", "Un científico debe arriesgarlo todo...", "05 Abr 2026", listOf("17:00", "20:30"), "https://picsum.photos/seed/cuantico/400/600"),
    Movie(6, "Planeta Olvidado", "Sr. G. Lucas", "Ciencia Ficción", "Exploradores espaciales encuentran un planeta...", "18 May 2026", listOf("16:00", "19:45", "22:30"), "https://picsum.photos/seed/planeta/400/600"),
    Movie(7, "Código Cero", "Sr. A. Turing", "Documental", "La historia no contada de los héroes...", "30 Jun 2026", listOf("18:30"), "https://picsum.photos/seed/cero/400/600")
)
val sampleConcessions = listOf(
    ConcessionItem("Palomitas Grandes", 5.50, "https://media.istockphoto.com/id/497857462/es/foto/palomitas-de-ma%C3%ADz-en-el-per%C3%ADodo.jpg?s=612x612&w=0&k=20&c=dFQRkQrgC7ZYpuHATOdotegQFXCEIASXelrb1UsCPnc="),
    ConcessionItem("Refresco Mediano", 3.75, "https://tb-static.uber.com/prod/image-proc/processed_images/c064570420dce0c6c2991aeb7f967a54/0fb376d1da56c05644450062d25c5c84.jpeg"),
    ConcessionItem("Nachos con Queso", 6.20, "https://www.divinacocina.es/wp-content/uploads/nachos-con-salsa-queso.jpg"),
    ConcessionItem("Hot Dog", 4.50, "https://awrestaurants.com/_next/static/chunks/images/sites-default-files-styles-responsive_image_5x4-public-2024-11-HotDogHotDog_0.3840.jpg"),
    ConcessionItem("Chocolates", 2.80, "https://us.britishessentials.com/cdn/shop/files/28688011_0_640x640_7a2c7a96-6709-459b-9bdd-87e268414c23_x700.jpg?v=1739461545"),
    ConcessionItem("Agua Embotellada", 2.00, "https://img.mundopmmi.com/files/base/pmmi/mundo/image/2020/01/botella_cristal_100_100.5e31c8c214c05.png?auto=format%2Ccompress&fit=max&q=70&w=1200")
)


// --- Pantallas de Autenticación ---
@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val email by authViewModel.loginEmail
    val password by authViewModel.loginPassword
    val statusMessage by authViewModel.statusMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        statusMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        OutlinedTextField(value = email, onValueChange = { authViewModel.loginEmail.value = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { authViewModel.loginPassword.value = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { authViewModel.login() }) {
            Text("Entrar")
        }
        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes una cuenta? Regístrate")
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    val name by authViewModel.name
    val email by authViewModel.email
    val password by authViewModel.password
    val statusMessage by authViewModel.statusMessage
    val isLoading by authViewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        statusMessage?.let {
            Text(
                it, 
                color = if (it.contains("Error") || it.contains("no es válido") || it.contains("ya está en uso")) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color(0xFF4CAF50) // Verde para mensajes de éxito
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        OutlinedTextField(
            value = name, 
            onValueChange = { authViewModel.name.value = it }, 
            label = { Text("Nombre") },
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email, 
            onValueChange = { authViewModel.email.value = it }, 
            label = { Text("Email") },
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password, 
            onValueChange = { authViewModel.password.value = it }, 
            label = { Text("Contraseña") }, 
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { authViewModel.register() },
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Validando email...")
            } else {
                Text("Registrarse")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { navController.popBackStack() },
            enabled = !isLoading
        ) {
            Text("¿Ya tienes una cuenta? Inicia Sesión")
        }
    }
}

// --- Pantallas Principales ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(user: User, authViewModel: AuthViewModel) {
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
                    Text(text = "Hola, ${user.name}", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Medium)
                    IconButton(onClick = { authViewModel.logout() }) {
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
            authViewModel = authViewModel
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
fun MainContentNavigation(navController: NavHostController, padding: PaddingValues, authViewModel: AuthViewModel) {
    val user = (authViewModel.authState.value as AuthState.LoggedIn).user

    NavHost(navController = navController, startDestination = "movies", modifier = Modifier.padding(padding)) {
        composable("home") { HomeScreen() }
        composable("movies") {
            MoviesScreen(onMovieClick = { movieId -> navController.navigate("movie_detail/$movieId") })
        }
        composable("concessions") { ConcessionsScreen() }
        composable("profile") {
            ProfileScreen(
                user = user,
                authViewModel = authViewModel,
                onLogout = { authViewModel.logout() },
                onNavigateToHistory = { navController.navigate("purchase_history") },
                onNavigateToChangePassword = { navController.navigate("change_password") }
            )
        }
        composable("purchase_history") {
            PurchaseHistoryScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("change_password") {
            ChangePasswordScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
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
            sampleMovies.find { it.id == movieId }?.let { movie ->
                 PaymentScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onConfirmPayment = {
                        authViewModel.addPurchase(movie.title, time, seatIds)
                        navController.navigate("confirmation/${movie.id}/$time/$seatIds")
                    }
                )
            }
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
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Añade espacio entre tarjetas
    ) {
        items(sampleMovies) { movie ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMovieClick(movie.id) },
                elevation = CardDefaults.cardElevation(4.dp) // Añade una pequeña sombra
            ) {
                // Usamos un Row para poner imagen y texto lado a lado
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    // 1. Imagen de la película
                    AsyncImage(
                        model = movie.imageUrl,
                        contentDescription = "Póster de ${movie.title}",
                        modifier = Modifier
                            .size(width = 80.dp, height = 120.dp) // Tamaño fijo para la imagen
                            .clip(RoundedCornerShape(8.dp)), // Bordes redondeados
                        contentScale = ContentScale.Crop // Escala la imagen para que llene el espacio
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Espacio entre imagen y texto

                    // 2. Columna con la información de la película
                    Column {
                        Text(movie.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Director: ${movie.director}", fontSize = 16.sp)
                        Text("Género: ${movie.genre}", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun ConcessionsScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sampleConcessions) { item ->
            ConcessionCard(item = item, onAddToCart = { /* TODO: Add to cart logic */ })
        }
    }
}

@Composable
fun ConcessionCard(item: ConcessionItem, onAddToCart: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "Imagen de ${item.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$%.2f".format(item.price),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir")
                }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Continuar")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {
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
        Box(modifier = Modifier
            .size(16.dp)
            .background(color, RoundedCornerShape(2.dp)))
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = seats.any { it.status == SeatStatus.SELECTED }
            ) {
                Text("Confirmar Selección")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("PANTALLA", modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(4.dp))
                .padding(horizontal = 24.dp, vertical = 4.dp), color = Color.White, fontWeight = FontWeight.Bold)
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
    val isFormValid by remember(cardNumber, expiryDate, cvv) {
        mutableStateOf(
            cardNumber.length == 16 &&
            expiryDate.length == 4 &&
            cvv.length == 3
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Simulación de Pago") }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            })
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Transbank", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005EB8))
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { if (it.length <= 16) cardNumber = it },
                label = { Text("Número de Tarjeta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = CardNumberVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { if (it.length <= 4) expiryDate = it },
                    label = { Text("MM/AA") },
                    visualTransformation = ExpiryDateVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 3) cvv = it },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onConfirmPayment,
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Ir al Pago")
            }
        }
    }
}

// --- Visual Transformations for Payment Screen ---
class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        val annotatedString = buildAnnotatedString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i % 4 == 3 && i < 15) {
                    append(" ")
                }
            }
        }

        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }
        return TransformedText(annotatedString, creditCardOffsetTranslator)
    }
}

class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        val annotatedString = buildAnnotatedString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 1) {
                    append("/")
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(movie: Movie, time: String, selectedSeatIds: String, onFinish: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("¡Compra Exitosa!") }) },
        bottomBar = {
            Button(onClick = onFinish, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text("Finalizar")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
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
fun ProfileScreen(
    user: User,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToChangePassword: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                // Release persisted permissions for the old URI if it exists
                user.profileImageUri?.let { oldUriString ->
                    try {
                        val oldUri = Uri.parse(oldUriString)
                        context.contentResolver.releasePersistableUriPermission(oldUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    } catch (e: Exception) {
                        // Handle exceptions, e.g., the URI is invalid or permission was already revoked
                    }
                }
                // Take persistable permissions for the new URI
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                authViewModel.updateUserProfileImage(uri)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { launcher.launch(arrayOf("image/*")) },
                    contentAlignment = Alignment.Center
                ) {
                    val imageUri = user.profileImageUri?.let { Uri.parse(it) }
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Profile Icon",
                            modifier = Modifier.size(70.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            Divider()

            Column {
                ProfileMenuItem(
                    text = "Historial de compras",
                    icon = Icons.Default.History,
                    onClick = onNavigateToHistory
                )
                Divider()
                ProfileMenuItem(
                    text = "Cambiar contraseña",
                    icon = Icons.Default.Lock,
                    onClick = onNavigateToChangePassword
                )
                Divider()
                ProfileMenuItem(
                    text = "Boletas",
                    icon = Icons.Default.Receipt,
                    onClick = { /* TODO */ }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun ProfileMenuItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseHistoryScreen(authViewModel: AuthViewModel, onNavigateBack: () -> Unit) {
    val purchaseHistory by authViewModel.purchaseHistory

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Compras") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        if (purchaseHistory.isEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center) {
                Text("Aún no has realizado ninguna compra.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(it), contentPadding = PaddingValues(16.dp)) {
                items(purchaseHistory) { purchase ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(purchase.movieTitle, style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Fecha: ${purchase.purchaseTimestamp.toFormattedDateString()}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Hora: ${purchase.time}", style = MaterialTheme.typography.bodyLarge)
                            Text("Asientos: ${purchase.seatIds}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(authViewModel: AuthViewModel, onNavigateBack: () -> Unit) {
    val newPassword by authViewModel.newPassword
    val confirmPassword by authViewModel.confirmPassword
    val statusMessage by authViewModel.statusMessage

    // Limpiar el mensaje de estado cuando la pantalla se va
    DisposableEffect(Unit) {
        onDispose {
            authViewModel.clearStatusMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = newPassword,
                onValueChange = { authViewModel.newPassword.value = it },
                label = { Text("Nueva Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { authViewModel.confirmPassword.value = it },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { authViewModel.changePassword() }, modifier = Modifier.fillMaxWidth()) {
                Text("Actualizar Contraseña")
            }
            statusMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                val isError = "coinciden" in it || "vacíos" in it || "Error" in it
                Text(it, color = if (isError) MaterialTheme.colorScheme.error else Color.Green)
            }
        }
    }
}

fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)
