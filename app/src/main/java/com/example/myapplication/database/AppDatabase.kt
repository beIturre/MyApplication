package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Base de datos Room principal
 */
@Database(
    entities = [UserEntity::class, PurchaseEntity::class, MovieEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun movieDao(): MovieDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        // Migración de versión 1 a 2: Agregar tabla de películas
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS movies (
                        id INTEGER NOT NULL PRIMARY KEY,
                        title TEXT NOT NULL,
                        director TEXT NOT NULL,
                        genre TEXT NOT NULL,
                        synopsis TEXT NOT NULL,
                        releaseDate TEXT NOT NULL,
                        availableTimes TEXT NOT NULL,
                        imageUrl TEXT NOT NULL,
                        price INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cinemax_database"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration() // Solo para desarrollo - eliminar en producción
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

