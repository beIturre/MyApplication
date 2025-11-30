package com.example.myapplication

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // Estado de autenticación
    private val _authState = mutableStateOf<AuthState>(AuthState.LoggedOut)
    val authState: State<AuthState> = _authState

    // Historial de compras
    private val _purchaseHistory = mutableStateOf<List<Purchase>>(emptyList())
    val purchaseHistory: State<List<Purchase>> = _purchaseHistory

    // Campos de formularios
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val loginEmail = mutableStateOf("")
    val loginPassword = mutableStateOf("")
    val newPassword = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    
    // Mensajes de estado (error o éxito)
    val statusMessage = mutableStateOf<String?>(null)
    
    // Estado de carga para validación de email
    val isLoading = mutableStateOf(false)
    
    // Servicio de validación de email
    private val emailValidationService = EmailValidationService()

    init {
        viewModelScope.launch {
            repository.getLoggedInUser()?.let { user ->
                _authState.value = AuthState.LoggedIn(user)
                loadPurchaseHistory()
            }
        }
    }

    // --- Lógica de Autenticación ---
    fun login() {
        viewModelScope.launch {
            val user = repository.findUser(loginEmail.value, loginPassword.value)
            if (user != null) {
                repository.loginUser(user.email)
                _authState.value = AuthState.LoggedIn(user)
                loadPurchaseHistory()
                resetFields()
            } else {
                statusMessage.value = "Email o contraseña incorrectos"
            }
        }
    }

    fun register() {
        if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            statusMessage.value = "Todos los campos son obligatorios"
            return
        }
        
        // Validar email antes de registrar
        isLoading.value = true
        viewModelScope.launch {
            try {
                val validationResult = emailValidationService.validateEmail(email.value)
                
                when (validationResult) {
                    EmailValidationResult.INVALID -> {
                        statusMessage.value = "El email ingresado no es válido. Por favor, verifica el formato."
                        isLoading.value = false
                        return@launch
                    }
                    EmailValidationResult.UNKNOWN -> {
                        // Si el resultado es desconocido, permitir el registro pero mostrar advertencia
                        statusMessage.value = "No se pudo verificar el email completamente. Continuando con el registro..."
                    }
                    EmailValidationResult.VALID -> {
                        // Email válido, continuar con el registro
                    }
                }
                
                // Proceder con el registro
                val newUser = User(name.value, email.value, password.value)
                if (repository.addUser(newUser)) {
                    repository.loginUser(newUser.email)
                    _authState.value = AuthState.LoggedIn(newUser)
                    loadPurchaseHistory()
                    resetFields()
                    statusMessage.value = null
                } else {
                    statusMessage.value = "El email ya está en uso"
                }
            } catch (e: Exception) {
                statusMessage.value = "Error al validar el email: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun logout() {
        repository.logoutUser()
        _authState.value = AuthState.LoggedOut
        _purchaseHistory.value = emptyList()
    }

    fun updateUserProfileImage(uri: Uri?) {
        val currentUser = (_authState.value as? AuthState.LoggedIn)?.user ?: return
        viewModelScope.launch {
            repository.updateUserProfileImage(currentUser.email, uri?.toString())
            // Refresh user state to show the new image immediately
            repository.getLoggedInUser()?.let { updatedUser ->
                _authState.value = AuthState.LoggedIn(updatedUser)
            }
        }
    }

    // --- Lógica de Contraseña ---
    fun changePassword() {
        val currentUser = (_authState.value as? AuthState.LoggedIn)?.user ?: return

        if (newPassword.value.isBlank() || confirmPassword.value.isBlank()) {
            statusMessage.value = "Los campos no pueden estar vacíos"
            return
        }
        if (newPassword.value != confirmPassword.value) {
            statusMessage.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            val success = repository.changePassword(currentUser.email, newPassword.value)
            if (success) {
                statusMessage.value = "Contraseña actualizada con éxito"
                // Opcional: cerrar sesión después de cambiar la contraseña
                // logout()
            } else {
                statusMessage.value = "Error al actualizar la contraseña"
            }
            newPassword.value = ""
            confirmPassword.value = ""
        }
    }

    // --- Lógica de Compras ---
    fun addPurchase(movieTitle: String, time: String, seatIds: String) {
        val currentUser = (_authState.value as? AuthState.LoggedIn)?.user ?: return
        viewModelScope.launch {
            val newPurchase = Purchase(
                userEmail = currentUser.email,
                movieTitle = movieTitle,
                time = time,
                seatIds = seatIds,
                purchaseTimestamp = System.currentTimeMillis()
            )
            repository.savePurchase(newPurchase)
            loadPurchaseHistory()
        }
    }

    private fun loadPurchaseHistory() {
        val currentUser = (_authState.value as? AuthState.LoggedIn)?.user ?: return
        viewModelScope.launch {
            _purchaseHistory.value = repository.getPurchaseHistory(currentUser.email)
        }
    }
    
    // --- Utilidades ---
    fun clearStatusMessage() {
        statusMessage.value = null
    }

    private fun resetFields() {
        name.value = ""
        email.value = ""
        password.value = ""
        loginEmail.value = ""
        loginPassword.value = ""
        newPassword.value = ""
        confirmPassword.value = ""
        statusMessage.value = null
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