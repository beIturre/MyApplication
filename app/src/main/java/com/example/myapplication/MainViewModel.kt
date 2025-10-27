package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    // Estado observable para el nombre de usuario
    private val _username = mutableStateOf<String?>(null)
    val username: State<String?> = _username

    init {
        // Al iniciar el ViewModel, cargar el usuario guardado
        _username.value = repository.getUser()
    }

    /**
     * Inicia sesión: guarda el usuario y actualiza el estado.
     */
    fun login(name: String) {
        if (name.isNotBlank()) {
            repository.saveUser(name)
            _username.value = name
        }
    }

    /**
     * Cierra sesión: borra el usuario y actualiza el estado.
     */
    fun logout() {
        repository.clearUser()
        _username.value = null
    }
}

/**
 * Factory necesario para poder inyectar el UserRepository (que necesita Context)
 * en nuestro MainViewModel.
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(UserRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}