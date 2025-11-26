package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // Estado para reflejar el estado de la autenticación
    private val _authState = mutableStateOf<AuthState>(AuthState.LoggedOut)
    val authState: State<AuthState> = _authState

    // Historial de compras
    private val _purchaseHistory = mutableStateOf<List<Purchase>>(emptyList())
    val purchaseHistory: State<List<Purchase>> = _purchaseHistory

    // Campos del formulario de registro
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    // Campos del formulario de inicio de sesión
    val loginEmail = mutableStateOf("")
    val loginPassword = mutableStateOf("")
    
    // Mensajes de error
    val errorMessage = mutableStateOf<String?>(null)

    init {
        repository.getLoggedInUser()?.let { user ->
            _authState.value = AuthState.LoggedIn(user)
            loadPurchaseHistory()
        }
    }

    fun login() {
        val user = repository.findUser(loginEmail.value, loginPassword.value)
        if (user != null) {
            repository.loginUser(user.email)
            _authState.value = AuthState.LoggedIn(user)
            loadPurchaseHistory()
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
        if (repository.addUser(newUser)) {
            repository.loginUser(newUser.email)
            _authState.value = AuthState.LoggedIn(newUser)
            loadPurchaseHistory() // Cargar historial (estará vacío)
            resetFields()
        } else {
            errorMessage.value = "El email ya está en uso"
        }
    }

    fun logout() {
        repository.logoutUser()
        _authState.value = AuthState.LoggedOut
        _purchaseHistory.value = emptyList() // Limpiar historial
    }

    fun addPurchase(movieTitle: String, time: String, seatIds: String) {
        val currentUser = (_authState.value as? AuthState.LoggedIn)?.user ?: return
        val newPurchase = Purchase(
            userEmail = currentUser.email,
            movieTitle = movieTitle,
            time = time,
            seatIds = seatIds,
            purchaseTimestamp = System.currentTimeMillis()
        )
        repository.savePurchase(newPurchase)
        loadPurchaseHistory() // Recargar el historial
    }

    private fun loadPurchaseHistory() {
        val currentUser = (_authState.value as? AuthState.LoggedIn)?.user ?: return
        _purchaseHistory.value = repository.getPurchaseHistory(currentUser.email)
    }

    private fun resetFields() {
        name.value = ""
        email.value = ""
        password.value = ""
        loginEmail.value = ""
        loginPassword.value = ""
        errorMessage.value = null
    }
}

// --- Clases de Estado y Factory ---
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
