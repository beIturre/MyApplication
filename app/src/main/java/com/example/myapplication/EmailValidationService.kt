package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Modelo de respuesta de la API de validación de email
// Compatible con Abstract API y otras APIs de validación de email
data class EmailValidationResponse(
    val email: String,
    val autocorrect: String? = null,
    val deliverability: String? = null,
    val quality_score: Double? = null,
    // Abstract API devuelve objetos con value y text, otras APIs pueden devolver booleanos directamente
    val is_valid_format: Any? = null,
    val is_free_email: Any? = null,
    val is_disposable_email: Any? = null,
    val is_role_email: Any? = null,
    val is_catchall_email: Any? = null,
    val is_mx_found: Any? = null,
    val is_smtp_valid: Any? = null
)

// Helper para extraer el valor booleano de diferentes formatos
private fun getBooleanValue(value: Any?): Boolean {
    return when (value) {
        is Boolean -> value
        is Map<*, *> -> (value["value"] as? Boolean) ?: false
        else -> false
    }
}

enum class EmailValidationResult {
    VALID,
    INVALID,
    UNKNOWN
}

// Interfaz de la API
interface EmailValidationApi {
    @GET("email/validate")
    suspend fun validateEmail(
        @Query("api_key") apiKey: String,
        @Query("email") email: String
    ): EmailValidationResponse
}

// Servicio de validación de email
class EmailValidationService {
    // Nota: Reemplaza esta API key con tu propia clave de Abstract API
    // Puedes obtener una gratis en: https://www.abstractapi.com/api/email-verification-validation-api
    // O usar otra API de validación de email
    private val API_KEY = "TU_API_KEY_AQUI" // TODO: Reemplazar con tu API key
    
    private val baseUrl = "https://emailvalidation.abstractapi.com/v1/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val api = retrofit.create(EmailValidationApi::class.java)
    
    /**
     * Valida un email usando la API externa
     * @param email El email a validar
     * @return Resultado de la validación con mensaje descriptivo
     */
    suspend fun validateEmail(email: String): EmailValidationResult {
        return try {
            // Si no hay API key configurada, hacer validación básica local
            if (API_KEY == "TU_API_KEY_AQUI" || API_KEY.isEmpty()) {
                return validateEmailLocally(email)
            }
            
            val response = api.validateEmail(API_KEY, email)
            
            // Extraer valores booleanos del formato de respuesta
            val isValidFormat = getBooleanValue(response.is_valid_format)
            val isDisposable = getBooleanValue(response.is_disposable_email)
            
            // Verificar múltiples criterios de validación
            when {
                !isValidFormat -> {
                    EmailValidationResult.INVALID
                }
                isDisposable -> {
                    EmailValidationResult.INVALID // Rechazar emails desechables
                }
                response.deliverability == "UNDELIVERABLE" -> {
                    EmailValidationResult.INVALID
                }
                isValidFormat && 
                (response.deliverability == "DELIVERABLE" || response.deliverability == null) -> {
                    EmailValidationResult.VALID
                }
                else -> {
                    EmailValidationResult.UNKNOWN
                }
            }
        } catch (e: Exception) {
            // En caso de error de red, hacer validación local básica
            validateEmailLocally(email)
        }
    }
    
    /**
     * Validación básica local usando regex
     * Se usa como fallback cuando la API no está disponible
     */
    private fun validateEmailLocally(email: String): EmailValidationResult {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return if (emailRegex.matches(email)) {
            EmailValidationResult.VALID
        } else {
            EmailValidationResult.INVALID
        }
    }
}

