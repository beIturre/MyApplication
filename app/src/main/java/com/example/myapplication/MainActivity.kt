package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapplication.AppNavigation
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.MainViewModel
import com.example.myapplication.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciamos el ViewModel usando un Factory para pasarle el Context
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(this)
        }


        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // AppNavigation es el Composable que manejará toda la navegación
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}