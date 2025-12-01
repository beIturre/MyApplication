package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para películas
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val director: String,
    val genre: String,
    val synopsis: String,
    val releaseDate: String,
    val availableTimes: String, // Almacenado como JSON string
    val imageUrl: String,
    val price: Long // Precio en pesos chilenos
)

