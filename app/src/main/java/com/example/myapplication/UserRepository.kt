package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

/**
 * Un repositorio simple para manejar el guardado y la obtención del nombre de usuario.
 * Usa SharedPreferences para persistencia de datos simple.
 */
class UserRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USERNAME = "username"
    }

    /**
     * Guarda el nombre de usuario en SharedPreferences.
     */
    fun saveUser(username: String) {
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }

    /**
     * Obtiene el nombre de usuario guardado.
     * Devuelve null si no hay ninguno.
     */
    fun getUser(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    /**
     * Borra el nombre de usuario (para cerrar sesión).
     */
    fun clearUser() {
        prefs.edit().remove(KEY_USERNAME).apply()
    }
}