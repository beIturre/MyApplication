package com.example.myapplication

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios para EmailValidationService
 * Prueba la validación local de emails usando regex
 */
class EmailValidationServiceTest {

    private lateinit var emailValidationService: EmailValidationService

    @Before
    fun setUp() {
        emailValidationService = EmailValidationService()
    }

    @Test
    fun `test validar email con formato válido`() = runBlocking {
        // Given - Emails válidos
        val validEmails = listOf(
            "usuario@example.com",
            "test.email@domain.co.uk",
            "user+tag@example.org",
            "user_name@example-domain.com",
            "123@test.com"
        )

        // When & Then - Todos deben ser válidos
        validEmails.forEach { email ->
            val result = emailValidationService.validateEmail(email)
            assertEquals(
                "El email '$email' debería ser válido",
                EmailValidationResult.VALID,
                result
            )
        }
    }

    @Test
    fun `test validar email con formato inválido`() = runBlocking {
        // Given - Emails inválidos
        val invalidEmails = listOf(
            "email-sin-arroba.com",
            "@dominio.com",
            "usuario@",
            "usuario@dominio",
            "usuario espacio@dominio.com",
            "",
            "solo-texto",
            "usuario@@dominio.com",
            "@@dominio.com"
        )

        // When & Then - Todos deben ser inválidos
        invalidEmails.forEach { email ->
            val result = emailValidationService.validateEmail(email)
            assertEquals(
                "El email '$email' debería ser inválido",
                EmailValidationResult.INVALID,
                result
            )
        }
    }
}

