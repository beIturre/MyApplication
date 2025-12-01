#  CinemaxExtreme - Aplicación de Cine

Una aplicación desarrollada para la asignatura Desarrollo de Aplicaciones Moviles Android desarrollada en Kotlin con Jetpack Compose para la gestión de reservas de cine, compra de entradas y productos de confitería. 



##  Características

###  Autenticación de Usuarios
- **Registro de usuarios** con validación de email en tiempo real
- **Inicio de sesión** seguro
- **Gestión de perfiles** con imagen de perfil personalizable
- **Cambio de contraseña**
- **Persistencia de sesión** - Los usuarios permanecen logueados

### Gestión de Películas
- **Catálogo de películas** con información detallada
- **Sinopsis, director, género y fecha de estreno**
- **Horarios disponibles** para cada película
- **Imágenes de póster** cargadas desde URLs

### Sistema de Reservas
- **Selección de asientos** interactiva
- **Visualización de disponibilidad** (Disponible, Seleccionado, Ocupado)
- **Múltiples horarios** por película
- **Confirmación de compra** con detalles completos

### Confitería
- **Catálogo de productos** (palomitas, refrescos, nachos, etc.)
- **Precios y descripciones**
- **Imágenes de productos**

### Sistema de Pago
- **Simulación de pago** con Transbank
- **Formulario de tarjeta** con validación
- **Transformaciones visuales** para números de tarjeta y fecha de expiración

### Historial y Perfil
- **Historial de compras** completo
- **Gestión de perfil de usuario**
- **Imagen de perfil** personalizable
- **Información de usuario** persistente



### Lenguaje y Framework
- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - Framework de UI declarativa


### Arquitectura y Patrones
- **MVVM (Model-View-ViewModel)** - Arquitectura de la aplicación
- **Repository Pattern** - Gestión de datos
- **State Management** - Gestión de estado con Compose

### Bibliotecas Principales
- **Retrofit 2.9.0** - Cliente HTTP para APIs REST
- **Gson** - Serialización/Deserialización JSON
- **Coroutines** - Programación asíncrona
- **Navigation Compose** - Navegación entre pantallas
- **Coil** - Carga y caché de imágenes
- **Material 3** - Componentes de diseño modernos
- **Lifecycle ViewModel** - Gestión del ciclo de vida
- **Room 2.6.1** - Base de datos SQLite con abstracción

### Almacenamiento
- **Room Database** - Base de datos SQLite persistente para usuarios y compras
- **SharedPreferences** - Solo para persistencia de sesión (email del usuario logueado)
- **SQLite** - Motor de base de datos subyacente

## Requisitos Previos

### Para Desarrollo
- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK 11** o superior
- **Android SDK** con API Level 24 (Android 7.0) mínimo
- **Gradle** 8.13 o superior (incluido en el proyecto)

### Para Ejecución
- **Dispositivo Android** con Android 7.0 (API 24) o superior
- **O Emulador Android** configurado


##  Configuración

### Configurar API de Validación de Email

La aplicación incluye validación de email en tiempo real. Para habilitarla completamente:

1. **Obtener API Key de Abstract API** (Gratis):
   - Visita: https://www.abstractapi.com/api/email-verification-validation-api
   - Regístrate y obtén tu API key gratuita (100 validaciones/mes)

2. **Configurar la API Key**:
   - Abre el archivo: `app/src/main/java/com/example/myapplication/EmailValidationService.kt`
   - En la línea 54, reemplaza `"TU_API_KEY_AQUI"` con tu API key:
   ```kotlin
   private val API_KEY = "tu_api_key_aqui"
   ```

3. **Sin API Key**:
   - La aplicación funcionará con validación local básica (regex)
   - Funcionalidad limitada pero operativa

### Permisos

La aplicación requiere el siguiente permiso (ya configurado en `AndroidManifest.xml`):
- **INTERNET** - Para validación de email y carga de imágenes



### Archivos Principales

#### `MainActivity.kt`
- Punto de entrada de la aplicación
- Gestiona la navegación entre pantallas de autenticación y principal
- Inicializa el `AuthViewModel`

#### `AuthViewModel.kt`
- Gestiona el estado de autenticación
- Maneja la lógica de registro, login y logout
- Integra la validación de email
- Gestiona el historial de compras
- Maneja cambios de contraseña y perfil

#### `UserRepository.kt`
- Capa de acceso a datos
- Gestiona usuarios y compras usando **Room Database** (SQLite)
- Operaciones asíncronas con Coroutines
- Funciones de conversión entre entidades Room y modelos de dominio
- Operaciones CRUD para usuarios y compras

#### `EmailValidationService.kt`
- Servicio de validación de email
- Integración con Abstract API
- Validación local como fallback
- Manejo de errores de red

#### `AppScreens.kt`
- Todas las pantallas de la UI
- Componentes Compose reutilizables
- Navegación entre pantallas
- Lógica de presentación

##  Funcionalidades Detalladas

### 1. Autenticación

#### Registro
- Campos: Nombre, Email, Contraseña
- **Validación de email en tiempo real** con API externa
- Validación de campos obligatorios
- Prevención de emails duplicados
- Indicador de carga durante validación

#### Inicio de Sesión
- Autenticación con email y contraseña
- Mensajes de error descriptivos
- Persistencia de sesión

#### Perfil de Usuario
- Visualización de información del usuario
- **Imagen de perfil personalizable**
- Cambio de contraseña
- Historial de compras
- Cerrar sesión

### 2. Catálogo de Películas

- Lista de películas disponibles
- Información detallada:
  - Título, director, género
  - Sinopsis completa
  - Fecha de estreno
  - Horarios disponibles
  - Póster de la película

### 3. Selección de Asientos

- Visualización de sala de cine
- 8 filas (A-H) x 8 asientos por fila
- Estados visuales:
  - **Gris claro**: Disponible
  - **Azul**: Seleccionado
  - **Gris oscuro**: Ocupado
- Selección múltiple de asientos
- Leyenda de estados

### 4. Proceso de Pago

- Formulario de tarjeta de crédito
- Validación de campos:
  - Número de tarjeta (16 dígitos)
  - Fecha de expiración (MM/AA)
  - CVV (3 dígitos)
- Transformaciones visuales:
  - Formato de tarjeta: `1234 5678 9012 3456`
  - Formato de fecha: `MM/AA`
- Simulación de pago con Transbank

### 5. Confirmación de Compra

- Resumen de la compra:
  - Película seleccionada
  - Hora de la función
  - Asientos seleccionados
- Icono de éxito
- Navegación de vuelta al catálogo

### 6. Confitería

- Catálogo de productos en grid
- Información por producto:
  - Nombre
  - Precio
  - Imagen
- Botón para añadir al carrito (preparado para futura implementación)

### 7. Historial de Compras

- Lista de todas las compras realizadas
- Información por compra:
  - Título de la película
  - Fecha y hora de compra
  - Hora de la función
  - Asientos seleccionados
- Ordenado por fecha (más reciente primero)

##  Arquitectura

### Patrón MVVM

```
┌─────────────────┐
│   UI (Compose)   │
│  (AppScreens.kt) │
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  ViewModel       │
│ (AuthViewModel)  │
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  Repository      │
│(UserRepository)  │
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  Data Source    │
│  (Room Database)│
│    (SQLite)     │
└─────────────────┘
```

### Flujo de Datos

1. **UI** → Observa estados del ViewModel
2. **ViewModel** → Procesa lógica de negocio
3. **Repository** → Accede a datos
4. **Data Source** → Almacena/recupera datos
5. **ViewModel** → Actualiza estados
6. **UI** → Recompone con nuevos datos

### Gestión de Estado

- **State Management**: Usando `mutableStateOf` y `State` de Compose
- **Estado de Autenticación**: `AuthState` (sealed class)
- **Estados de Carga**: `isLoading` para operaciones asíncronas
- **Mensajes**: `statusMessage` para feedback al usuario

## Uso de la Aplicación

### Primer Uso

1. **Registro**:
   - Abre la aplicación
   - Toca "¿No tienes una cuenta? Regístrate"
   - Completa el formulario:
     - Nombre
     - Email (se validará automáticamente)
     - Contraseña
   - Toca "Registrarse"
   - Espera la validación del email
   - Serás logueado automáticamente

2. **Explorar Películas**:
   - Navega a la pestaña "Películas"
   - Toca una película para ver detalles
   - Selecciona un horario disponible

3. **Reservar Asientos**:
   - Selecciona los asientos deseados
   - Toca "Confirmar Selección"

4. **Pagar**:
   - Completa el formulario de pago
   - Toca "Ir al Pago"
   - Confirma la compra

5. **Ver Historial**:
   - Ve a "Perfil"
   - Toca "Historial de compras"

### Funciones Adicionales

- **Cambiar Contraseña**: Perfil → Cambiar contraseña
- **Actualizar Foto de Perfil**: Perfil → Toca la imagen
- **Cerrar Sesión**: Perfil → Botón "Cerrar sesión"

## Base de Datos Room (SQLite)

La aplicación utiliza **Room** como capa de abstracción sobre SQLite para una persistencia de datos robusta y eficiente.

### Arquitectura de Base de Datos

```
AppDatabase (Singleton)
├── UserDao
│   ├── insertUser()
│   ├── getUserByEmail()
│   ├── findUser()
│   ├── updatePassword()
│   └── updateProfileImage()
└── PurchaseDao
    ├── insertPurchase()
    └── getPurchasesByUser()
```

### Entidades

#### UserEntity
- **Tabla**: `users`
- **Campos**:
  - `email` (Primary Key) - Email del usuario
  - `name` - Nombre completo
  - `password` - Contraseña encriptada
  - `profileImageUri` - URI de la imagen de perfil (opcional)

#### PurchaseEntity
- **Tabla**: `purchases`
- **Campos**:
  - `id` (Primary Key, AutoGenerate) - ID único de la compra
  - `userEmail` - Email del usuario que realizó la compra
  - `movieTitle` - Título de la película
  - `time` - Hora de la función
  - `seatIds` - IDs de los asientos seleccionados
  - `purchaseTimestamp` - Timestamp de la compra

### Características

-  **Type-safe queries** - Las queries se verifican en tiempo de compilación
-  **Operaciones asíncronas** - Todas las operaciones usan Coroutines
-  **Thread-safe** - Room maneja automáticamente los hilos
-  **Migraciones** - Fácil actualización del esquema de base de datos
-  **Mejor rendimiento** - Optimizado para SQLite
-  **Sin código SQL manual** - Room genera el código automáticamente

### Ubicación de la Base de Datos

La base de datos se almacena en:
```
/data/data/com.example.myapplication/databases/cinemax_database
```

### Operaciones Principales

#### Usuarios
```kotlin
// Agregar usuario
suspend fun addUser(user: User): Boolean

// Buscar usuario
suspend fun findUser(email: String, password: String): User?

// Obtener usuario logueado
suspend fun getLoggedInUser(): User?

// Cambiar contraseña
suspend fun changePassword(email: String, newPassword: String): Boolean

// Actualizar imagen de perfil
suspend fun updateUserProfileImage(email: String, imageUri: String?)
```

#### Compras
```kotlin
// Guardar compra
suspend fun savePurchase(purchase: Purchase)

// Obtener historial
suspend fun getPurchaseHistory(userEmail: String): List<Purchase>
```

### Migración de SharedPreferences a Room

La aplicación migró de SharedPreferences a Room para:
- Mejor rendimiento con grandes volúmenes de datos
- Queries más complejas y eficientes
- Type-safety en tiempo de compilación
- Soporte nativo para relaciones entre tablas
- Facilidad para futuras migraciones de esquema

**Nota**: SharedPreferences se mantiene solo para almacenar el email del usuario logueado (sesión), ya que es más adecuado para datos simples y pequeños.

### Configuración

La base de datos se configura automáticamente al iniciar la aplicación. No requiere configuración adicional.

**Versión de la base de datos**: 1  
**Nombre de la base de datos**: `cinemax_database`

##  API de Validación de Email

### Servicio Integrado

La aplicación utiliza **Abstract API** para validación de email en tiempo real.

### Características

-  Validación de formato
-  Verificación de entregabilidad
-  Detección de emails desechables
-  Validación SMTP
-  Fallback a validación local si la API falla

### Configuración

Ver sección [Configuración](#-configuración) para detalles.



##  Tests Unitarios

El proyecto incluye una suite completa de tests unitarios para garantizar la calidad y confiabilidad del código.

### Resumen de Tests

Se han implementado **10 tests unitarios** distribuidos en 3 archivos de test:

#### 1. EmailValidationServiceTest.kt (2 tests)
-  `test validar email con formato válido` - Verifica que emails válidos sean aceptados
-  `test validar email con formato inválido` - Verifica que emails inválidos sean rechazados

#### 2. UserRepositoryTest.kt (5 tests)
-  `test agregar usuario exitosamente` - Verifica el registro de nuevos usuarios
-  `test agregar usuario con email duplicado falla` - Previene emails duplicados
-  `test encontrar usuario con credenciales correctas` - Autenticación exitosa
-  `test encontrar usuario con credenciales incorrectas retorna null` - Seguridad de autenticación
-  `test cambiar contraseña exitosamente` - Actualización de contraseña

#### 3. PurchaseRepositoryTest.kt (3 tests)
-  `test guardar compra exitosamente` - Persistencia de compras
-  `test obtener historial de compras ordenado por fecha` - Ordenamiento correcto
-  `test obtener historial de compras filtra por email de usuario` - Filtrado por usuario
### Tecnologías de Testing

- **JUnit 4** - Framework de testing estándar
- **MockK 1.13.8** - Biblioteca de mocking para Kotlin (permite mockear componentes de Android)
- **Kotlin Coroutines Test 1.7.3** - Testing de código asíncrono

### Ejecutar los Tests

#### Desde Android Studio
1. Click derecho en la carpeta `app/src/test`
2. Selecciona **"Run 'Tests in 'myapplication''"**
3. O ejecuta un test individual haciendo click derecho en el método `@Test` específico

#### Desde Terminal/CMD
```bash
# Ejecutar todos los tests
.\gradlew.bat test

# Ejecutar un test específico
.\gradlew.bat test --tests "EmailValidationServiceTest"

# Ejecutar tests de un paquete específico
.\gradlew.bat test --tests "com.example.myapplication.*"
```

### Cobertura de Tests

Los tests cubren las siguientes áreas:

-  **Validación de email** - Formato válido/inválido, casos edge
-  **Gestión de usuarios** - Crear, buscar, autenticar usuarios
-  **Prevención de duplicados** - Validación de emails únicos
-  **Cambio de contraseña** - Actualización segura de credenciales
-  **Gestión de compras** - Guardar, listar, filtrar compras
-  **Ordenamiento de datos** - Verificación de orden cronológico

### Detalles de Implementación

#### EmailValidationServiceTest
Prueba la validación local de emails usando regex. Como la API key no está configurada por defecto, estos tests verifican el comportamiento de fallback cuando la API externa no está disponible.

**Casos de prueba:**
- Emails válidos: `usuario@example.com`, `test.email@domain.co.uk`, `user+tag@example.org`, etc.
- Emails inválidos: sin @, sin dominio, formato incorrecto, espacios, caracteres especiales, etc.

#### UserRepositoryTest
Utiliza **MockK** para simular `SharedPreferences` y `Context` de Android, permitiendo tests unitarios puros sin necesidad de un dispositivo o emulador.

**Mocks utilizados:**
- `Context` - Contexto de Android simulado
- `SharedPreferences` - Almacenamiento local simulado
- `SharedPreferences.Editor` - Editor de preferencias simulado

**Ventajas:**
- Tests rápidos (no requieren Android Runtime)
- Aislamiento completo de dependencias
- Fácil de mantener y extender

#### PurchaseRepositoryTest
Verifica la funcionalidad completa de compras, incluyendo:
- Persistencia correcta de datos
- Ordenamiento por fecha (más reciente primero)
- Filtrado por usuario (privacidad de datos)

### Estructura de Tests

```
app/src/test/java/com/example/myapplication/
├── EmailValidationServiceTest.kt    # Tests de validación de email
├── UserRepositoryTest.kt           # Tests del repositorio de usuarios
└── PurchaseRepositoryTest.kt        # Tests de gestión de compras
```

### Mejoras Futuras de Testing

- [ ] Tests para `AuthViewModel` (lógica de negocio)
- [ ] Tests de integración (flujos completos)
- [ ] Tests de UI con Compose Testing
- [ ] Tests de rendimiento y carga
- [ ] Aumentar cobertura de código al 80%+
- [ ] Tests de accesibilidad
- [ ] Tests de seguridad (validación de datos sensibles)

### Notas Importantes

- Los tests utilizan `runBlocking` para código asíncrono (Coroutines)
- MockK permite mockear componentes de Android sin necesidad de Robolectric
- Los tests son independientes y pueden ejecutarse en cualquier orden
- Cada test tiene `@Before` (setup) y `@After` (teardown) apropiados
- Los tests verifican tanto casos exitosos como casos de error



## Contribución

Las contribuciones son bienvenidas. Por favor:

1. **Fork** el proyecto
2. Crea una **rama** para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. Abre un **Pull Request**



## Notas de Desarrollo

### Datos de Prueba

- Las películas y productos son datos de ejemplo
- Los usuarios se almacenan en **Room Database** (SQLite)
- Las compras persisten entre sesiones en la base de datos
- La sesión del usuario se guarda en SharedPreferences

### Mejoras Futuras

- [ ] Integración con backend real
- [ ] Autenticación con Firebase
- [ ] Sistema de carrito de compras funcional
- [ ] Notificaciones push
- [ ] Sistema de favoritos
- [ ] Búsqueda de películas
- [ ] Filtros y categorías
- [ ] Sistema de reseñas
- [ ] Integración con pasarela de pago real


##  Autores

Axel Prado Carvajal y Benjamín Iturre

---

**Versión**: 1.0  
**Última actualización**: 2024  
**Estado**: En desarrollo activo

