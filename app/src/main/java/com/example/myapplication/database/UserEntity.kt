package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para usuarios
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val name: String,
    val password: String,
    val profileImageUri: String? = null
)

