package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciamos el AuthViewModel usando el Factory
        val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory(this) }

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Lógica para decidir qué pantalla mostrar según el estado de autenticación
                    val authState by authViewModel.authState
                    when (val state = authState) {
                        is AuthState.LoggedIn -> MainScreen(user = state.user, authViewModel = authViewModel)
                        is AuthState.LoggedOut -> AuthScreen(authViewModel = authViewModel)
                    }
                }
            }
        }
    }
}
