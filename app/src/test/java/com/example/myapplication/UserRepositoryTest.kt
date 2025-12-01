package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios para UserRepository
 * Utiliza MockK para mockear SharedPreferences y Context
 */
class UserRepositoryTest {

    private lateinit var mockContext: Context
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        // Configurar mocks
        mockkStatic(SharedPreferences::class)

        mockContext = mockk<Context>(relaxed = true)
        mockSharedPreferences = mockk<SharedPreferences>(relaxed = true)
        mockEditor = mockk<SharedPreferences.Editor>(relaxed = true)

        // Configurar comportamiento de mocks
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.putStringSet(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs



        userRepository = UserRepository(mockContext)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test agregar usuario exitosamente`() = runTest {
        // Usuario nuevo y SharedPreferences vacío
        val newUser = User(
            name = "Juan Pérez",
            email = "juan@example.com",
            password = "password123"
        )

        every { mockSharedPreferences.getStringSet(any(), any()) } returns emptySet()

        // Agregar usuario
        val result = userRepository.addUser(newUser)

        // Debe retornar true y guardar el usuario
        assertTrue("El usuario debería agregarse exitosamente", result)
        verify(exactly = 1) { mockEditor.putStringSet(any(), any()) }
        verify(exactly = 1) { mockEditor.apply() }
    }

    @Test
    fun `test agregar usuario con email duplicado falla`() = runTest { // Usar runTest
        // Usuario existente y nuevo usuario con mismo email
        val existingUser = User(
            name = "Usuario Existente",
            email = "existente@example.com",
            password = "pass123"
        )

        val newUser = User(
            name = "Nuevo Usuario",
            email = "existente@example.com", // Mismo email
            password = "newpass456"
        )

        // Simular usuario existente en SharedPreferences
        val existingUserJson = """
            {"name":"Usuario Existente","email":"existente@example.com","password":"pass123","profileImageUri":null}
        """.trimIndent()

        every { mockSharedPreferences.getStringSet(any(), any()) } returns setOf(existingUserJson)

        // Intentar agregar usuario con email duplicado
        val result = userRepository.addUser(newUser)

        // Debe retornar false
        assertFalse("No debería permitir agregar usuario con email duplicado", result)
    }

    @Test
    fun `test encontrar usuario con credenciales correctas`() = runTest { // Usar runTest
        // Usuario existente
        val existingUser = User(
            name = "Test User",
            email = "test@example.com",
            password = "correctPassword"
        )

        // Simular usuario en SharedPreferences
        val userJson = """
            {"name":"Test User","email":"test@example.com","password":"correctPassword","profileImageUri":null}
        """.trimIndent()

        every { mockSharedPreferences.getStringSet(any(), any()) } returns setOf(userJson)

        // Buscar usuario con credenciales correctas
        val foundUser = userRepository.findUser("test@example.com", "correctPassword")

        // Debe encontrar el usuario
        assertNotNull("Debería encontrar el usuario", foundUser)
        assertEquals("El email debe coincidir", "test@example.com", foundUser?.email)
        assertEquals("El nombre debe coincidir", "Test User", foundUser?.name)
    }

    @Test
    fun `test encontrar usuario con credenciales incorrectas retorna null`() = runTest { // Usar runTest
        // Usuario existente
        val userJson = """
            {"name":"Test User","email":"test@example.com","password":"correctPassword","profileImageUri":null}
        """.trimIndent()

        every { mockSharedPreferences.getStringSet(any(), any()) } returns setOf(userJson)

        // Buscar con contraseña incorrecta
        val foundUser = userRepository.findUser("test@example.com", "wrongPassword")

        // No debe encontrar el usuario
        assertNull("No debería encontrar usuario con contraseña incorrecta", foundUser)
    }

    @Test
    fun `test cambiar contraseña exitosamente`() = runTest { // Use runTest
        // Usuario existente
        val userJson = """
            {"name":"Test User","email":"test@example.com","password":"oldPassword","profileImageUri":null}
        """.trimIndent()

        every { mockSharedPreferences.getStringSet(any(), any()) } returns setOf(userJson)

        //  Cambiar contraseña
        val result = userRepository.changePassword("test@example.com", "newPassword123")

        // retorna true y actualizar la contraseña
        assertTrue("El cambio de contraseña debería ser exitoso", result)
        verify(exactly = 1) { mockEditor.putStringSet(any(), any()) }
        verify(exactly = 1) { mockEditor.apply() }
    }
}

