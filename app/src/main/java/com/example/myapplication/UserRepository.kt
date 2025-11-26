package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

// --- Clases de Datos ---
data class User(val name: String, val email: String, val password:  String)
data class Purchase(
    val userEmail: String,
    val movieTitle: String,
    val time: String,
    val seatIds: String,
    val purchaseTimestamp: Long
)

class UserRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USERS = "users"
        private const val KEY_LOGGED_IN_USER_EMAIL = "logged_in_user_email"
        private const val KEY_PURCHASES = "purchases"
    }

    // --- Lógica de Usuario ---
    fun addUser(user: User): Boolean {
        val users = getUsers().toMutableSet()
        if (users.any { it.email == user.email }) {
            return false
        }
        users.add(user)
        saveUsers(users)
        return true
    }

    fun findUser(email: String, password:  String): User? {
        return getUsers().find { it.email == email && it.password == password }
    }
    
    fun loginUser(email: String) {
        prefs.edit().putString(KEY_LOGGED_IN_USER_EMAIL, email).apply()
    }

    fun logoutUser() {
        prefs.edit().remove(KEY_LOGGED_IN_USER_EMAIL).apply()
    }

    fun getLoggedInUser(): User? {
        val email = prefs.getString(KEY_LOGGED_IN_USER_EMAIL, null) ?: return null
        return getUsers().find { it.email == email }
    }

    fun changePassword(email: String, newPassword: String): Boolean {
        val users = getUsers().toMutableSet()
        val userToUpdate = users.find { it.email == email }
        if (userToUpdate != null) {
            val updatedUser = userToUpdate.copy(password = newPassword)
            users.remove(userToUpdate)
            users.add(updatedUser)
            saveUsers(users)
            return true
        }
        return false // Usuario no encontrado
    }

    private fun getUsers(): Set<User> {
        val userStrings = prefs.getStringSet(KEY_USERS, emptySet()) ?: emptySet()
        return userStrings.mapNotNull { userJson ->
            try {
                val json = JSONObject(userJson)
                User(
                    name = json.getString("name"),
                    email = json.getString("email"),
                    password = json.getString("password")
                )
            } catch (e: Exception) { null }
        }.toSet()
    }

    private fun saveUsers(users: Set<User>) {
        val userStrings = users.map { user ->
            JSONObject().apply {
                put("name", user.name)
                put("email", user.email)
                put("password", user.password)
            }.toString()
        }.toSet()
        prefs.edit().putStringSet(KEY_USERS, userStrings).apply()
    }

    // --- Lógica de Compras ---
    fun savePurchase(purchase: Purchase) {
        val allPurchases = getAllPurchases().toMutableList()
        allPurchases.add(purchase)
        val purchaseStrings = allPurchases.map { 
            JSONObject().apply {
                put("userEmail", it.userEmail)
                put("movieTitle", it.movieTitle)
                put("time", it.time)
                put("seatIds", it.seatIds)
                put("purchaseTimestamp", it.purchaseTimestamp)
            }.toString()
        }.toSet()
        prefs.edit().putStringSet(KEY_PURCHASES, purchaseStrings).apply()
    }

    fun getPurchaseHistory(userEmail: String): List<Purchase> {
        return getAllPurchases()
            .filter { it.userEmail == userEmail }
            .sortedByDescending { it.purchaseTimestamp }
    }

    private fun getAllPurchases(): List<Purchase> {
        val purchaseStrings = prefs.getStringSet(KEY_PURCHASES, emptySet()) ?: emptySet()
        return purchaseStrings.mapNotNull { purchaseJson ->
            try {
                val json = JSONObject(purchaseJson)
                Purchase(
                    userEmail = json.getString("userEmail"),
                    movieTitle = json.getString("movieTitle"),
                    time = json.getString("time"),
                    seatIds = json.getString("seatIds"),
                    purchaseTimestamp = json.getLong("purchaseTimestamp")
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}