package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import io.mockk.*import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios para la funcionalidad de compras en UserRepository
 */
class PurchaseRepositoryTest {

    private lateinit var mockContext: Context
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        mockkStatic(SharedPreferences::class)

        mockContext = mockk<Context>(relaxed = true)
        mockSharedPreferences = mockk<SharedPreferences>(relaxed = true)
        mockEditor = mockk<SharedPreferences.Editor>(relaxed = true)

        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putStringSet(any(), any()) } returns mockEditor
        every { mockEditor.apply() } just Runs
        every { mockSharedPreferences.getStringSet(any(), any()) } returns emptySet()

        userRepository = UserRepository(mockContext)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test guardar compra exitosamente`() = runTest {
        // Given - Una nueva compra
        val purchase = Purchase(
            userEmail = "usuario@example.com",
            movieTitle = "Kimetsu No Yaiba: Tren Infinito",
            time = "20:00",
            seatIds = "A1,A2",
            purchaseTimestamp = System.currentTimeMillis()
        )

        // Simula que al principio no hay compras para este usuario
        every { mockSharedPreferences.getStringSet(any(), any()) } returns emptySet()

        // When - Guardar la compra
        userRepository.savePurchase(purchase)

        // Then - Debe guardarse correctamente
        verify(exactly = 1) { mockEditor.putStringSet(any(), any()) }
        verify(exactly = 1) { mockEditor.apply() }
    }

    @Test
    // MODIFICADO: Añadir runTest para poder llamar a la función suspend getPurchaseHistory
    fun `test obtener historial de compras ordenado por fecha`() = runTest {
        val purchase1 = Purchase(
            userEmail = "usuario@example.com",
            movieTitle = "Película 1",
            time = "15:00",
            seatIds = "A1",
            purchaseTimestamp = 1000L // Más antigua
        )

        val purchase2 = Purchase(
            userEmail = "usuario@example.com",
            movieTitle = "Película 2",
            time = "20:00",
            seatIds = "B2",
            purchaseTimestamp = 3000L // Más reciente
        )

        val purchase3 = Purchase(
            userEmail = "usuario@example.com",
            movieTitle = "Película 3",
            time = "18:00",
            seatIds = "C3",
            purchaseTimestamp = 2000L // Intermedia
        )

        // Simular compras en SharedPreferences
        val purchase1Json = """
            {"userEmail":"usuario@example.com","movieTitle":"Película 1","time":"15:00","seatIds":"A1","purchaseTimestamp":1000}
        """.trimIndent()

        val purchase2Json = """
            {"userEmail":"usuario@example.com","movieTitle":"Película 2","time":"20:00","seatIds":"B2","purchaseTimestamp":3000}
        """.trimIndent()

        val purchase3Json = """
            {"userEmail":"usuario@example.com","movieTitle":"Película 3","time":"18:00","seatIds":"C3","purchaseTimestamp":2000}
        """.trimIndent()

        every {
            mockSharedPreferences.getStringSet(any(), any())
        } returns setOf(purchase1Json, purchase2Json, purchase3Json)

        // When - Obtener historial (ahora se llama dentro de runTest)
        val history = userRepository.getPurchaseHistory("usuario@example.com")

        // Then - Debe estar ordenado por fecha descendente (más reciente primero)
        assertEquals("Debe haber 3 compras", 3, history.size)
        assertEquals("La primera debe ser la más reciente", "Película 2", history[0].movieTitle)
        assertEquals("La segunda debe ser la intermedia", "Película 3", history[1].movieTitle)
        assertEquals("La tercera debe ser la más antigua", "Película 1", history[2].movieTitle)
    }

    @Test
    // MODIFICADO: Añadir runTest para poder llamar a la función suspend getPurchaseHistory
    fun `test obtener historial de compras filtra por email de usuario`() = runTest {
        val purchase1Json = """
            {"userEmail":"usuario1@example.com","movieTitle":"Película Usuario 1","time":"15:00","seatIds":"A1","purchaseTimestamp":1000}
        """.trimIndent()

        val purchase2Json = """
            {"userEmail":"usuario2@example.com","movieTitle":"Película Usuario 2","time":"20:00","seatIds":"B2","purchaseTimestamp":2000}
        """.trimIndent()

        val purchase3Json = """
            {"userEmail":"usuario1@example.com","movieTitle":"Otra Película Usuario 1","time":"18:00","seatIds":"C3","purchaseTimestamp":3000}
        """.trimIndent()

        every {
            mockSharedPreferences.getStringSet(any(), any())
        } returns setOf(purchase1Json, purchase2Json, purchase3Json)

        // When - Obtener historial de usuario1 (ahora se llama dentro de runTest)
        val history = userRepository.getPurchaseHistory("usuario1@example.com")

        // Then - Solo debe retornar compras de usuario1
        assertEquals("Debe haber 2 compras del usuario1", 2, history.size)
        assertTrue("Todas las compras deben ser del usuario1",
            history.all { it.userEmail == "usuario1@example.com" })
        assertFalse("No debe incluir compras de otros usuarios",
            history.any { it.userEmail == "usuario2@example.com" })
    }
}

