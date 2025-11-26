package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

// Clase de datos para representar a un usuario
data class User(val name: String, val email: String, val password:  String)

class UserRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USERS = "users"
        private const val KEY_LOGGED_IN_USER_EMAIL = "logged_in_user_email"
    }

    /**
     * Agrega un nuevo usuario al conjunto de usuarios guardados.
     * Devuelve true si el usuario se agregó correctamente, false si el email ya existe.
     */
    fun addUser(user: User): Boolean {
        val users = getUsers().toMutableSet()
        if (users.any { it.email == user.email }) {
            return false // El email ya está registrado
        }
        users.add(user)
        saveUsers(users)
        return true
    }

    /**
     * Busca un usuario por su email y contraseña.
     * Devuelve el objeto User si las credenciales son correctas, de lo contrario null.
     */
    fun findUser(email: String, password:  String): User? {
        return getUsers().find { it.email == email && it.password == password }
    }
    
    /**
     * Guarda el email del usuario que ha iniciado sesión.
     */
    fun loginUser(email: String) {
        prefs.edit().putString(KEY_LOGGED_IN_USER_EMAIL, email).apply()
    }

    /**
     * Limpia el email del usuario que ha iniciado sesión.
     */
    fun logoutUser() {
        prefs.edit().remove(KEY_LOGGED_IN_USER_EMAIL).apply()
    }

    /**
     * Obtiene el usuario que ha iniciado sesión.
     * Devuelve null si no hay ninguno.
     */
    fun getLoggedInUser(): User? {
        val email = prefs.getString(KEY_LOGGED_IN_USER_EMAIL, null) ?: return null
        return getUsers().find { it.email == email }
    }


    /**
     * Obtiene todos los usuarios guardados.
     */
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
            } catch (e: Exception) {
                null // Ignorar JSON mal formado
            }
        }.toSet()
    }

    /**
     * Guarda un conjunto de usuarios en SharedPreferences.
     */
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
}
