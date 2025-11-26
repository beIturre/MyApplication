package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // Estado para refelejar el estado de la autenticación
    private val _authState = mutableStateOf<AuthState>(AuthState.LoggedOut)
    val authState: State<AuthState> = _authState

    // Estado para los campos del formulario de registro
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    // Estado para los campos del formulario de inicio de sesión
    val loginEmail = mutableStateOf("")
    val loginPassword = mutableStateOf("")
    
    // Estado para mensajes de error
    val errorMessage = mutableStateOf<String?>(null)

    init {
        // Comprobar si ya hay un usuario que ha iniciado sesión
        val loggedInUser = repository.getLoggedInUser()
        if (loggedInUser != null) {
            _authState.value = AuthState.LoggedIn(loggedInUser)
        }
    }

    fun login() {
        val user = repository.findUser(loginEmail.value, loginPassword.value)
        if (user != null) {
            repository.loginUser(user.email)
            _authState.value = AuthState.LoggedIn(user)
            resetFields()
        } else {
            errorMessage.value = "Email o contraseña incorrectos"
        }
    }

    fun register() {
        if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            errorMessage.value = "Todos los campos son obligatorios"
            return
        }

        val newUser = User(name.value, email.value, password.value)
        val success = repository.addUser(newUser)

        if (success) {
            // Iniciar sesión automáticamente después del registro
            repository.loginUser(newUser.email)
            _authState.value = AuthState.LoggedIn(newUser)
            resetFields()
        } else {
            errorMessage.value = "El email ya está en uso"
        }
    }

    fun logout() {
        repository.logoutUser()
        _authState.value = AuthState.LoggedOut
    }

    // Resetea los campos de los formularios y los mensajes de error
    private fun resetFields() {
        name.value = ""
        email.value = ""
        password.value = ""
        loginEmail.value = ""
        loginPassword.value = ""
        errorMessage.value = null
    }
}

// Sealed class para representar los diferentes estados de autenticación
sealed class AuthState {
    object LoggedOut : AuthState()
    data class LoggedIn(val user: User) : AuthState()
}

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(UserRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}