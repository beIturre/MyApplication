package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.PurchaseEntity
import com.example.myapplication.database.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// --- Clases de Datos (mantenidas para compatibilidad) ---
data class User(
    val name: String,
    val email: String,
    val password: String,
    val profileImageUri: String? = null
)

data class Purchase(
    val userEmail: String,
    val movieTitle: String,
    val time: String,
    val seatIds: String,
    val purchaseTimestamp: Long
)

class UserRepository(private val context: Context) {

    // Base de datos Room
    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDao()
    private val purchaseDao = database.purchaseDao()

    // SharedPreferences solo para el email del usuario logueado (sesión)
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGGED_IN_USER_EMAIL = "logged_in_user_email"
    }

    // --- Funciones de conversión entre entidades y modelos de dominio ---
    private fun UserEntity.toUser(): User {
        return User(
            name = this.name,
            email = this.email,
            password = this.password,
            profileImageUri = this.profileImageUri
        )
    }

    private fun User.toUserEntity(): UserEntity {
        return UserEntity(
            email = this.email,
            name = this.name,
            password = this.password,
            profileImageUri = this.profileImageUri
        )
    }

    private fun PurchaseEntity.toPurchase(): Purchase {
        return Purchase(
            userEmail = this.userEmail,
            movieTitle = this.movieTitle,
            time = this.time,
            seatIds = this.seatIds,
            purchaseTimestamp = this.purchaseTimestamp
        )
    }

    private fun Purchase.toPurchaseEntity(): PurchaseEntity {
        return PurchaseEntity(
            userEmail = this.userEmail,
            movieTitle = this.movieTitle,
            time = this.time,
            seatIds = this.seatIds,
            purchaseTimestamp = this.purchaseTimestamp
        )
    }

    // --- Lógica de Usuario ---
    suspend fun addUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            // Verificar si el usuario ya existe
            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser != null) {
                return@withContext false
            }
            
            // Insertar nuevo usuario
            val result = userDao.insertUser(user.toUserEntity())
            // insertUser retorna -1 si hay conflicto, > 0 si es exitoso
            result > 0
        } catch (e: Exception) {
            false
        }
    }

    suspend fun findUser(email: String, password: String): User? = withContext(Dispatchers.IO) {
        try {
            userDao.findUser(email, password)?.toUser()
        } catch (e: Exception) {
            null
        }
    }

    fun loginUser(email: String) {
        prefs.edit().putString(KEY_LOGGED_IN_USER_EMAIL, email).apply()
    }

    fun logoutUser() {
        prefs.edit().remove(KEY_LOGGED_IN_USER_EMAIL).apply()
    }

    suspend fun getLoggedInUser(): User? = withContext(Dispatchers.IO) {
        try {
            val email = prefs.getString(KEY_LOGGED_IN_USER_EMAIL, null) ?: return@withContext null
            userDao.getUserByEmail(email)?.toUser()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun changePassword(email: String, newPassword: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val rowsAffected = userDao.updatePassword(email, newPassword)
            rowsAffected > 0
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateUserProfileImage(email: String, imageUri: String?) = withContext(Dispatchers.IO) {
        try {
            userDao.updateProfileImage(email, imageUri)
        } catch (e: Exception) {
            
        }
    }

    // --- Lógica de Compras ---
    suspend fun savePurchase(purchase: Purchase) = withContext(Dispatchers.IO) {
        try {
            purchaseDao.insertPurchase(purchase.toPurchaseEntity())
        } catch (e: Exception) {

        }
    }

    suspend fun getPurchaseHistory(userEmail: String): List<Purchase> = withContext(Dispatchers.IO) {
        try {
            purchaseDao.getPurchasesByUser(userEmail).map { it.toPurchase() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
