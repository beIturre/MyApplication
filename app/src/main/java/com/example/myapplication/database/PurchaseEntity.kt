package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para compras
 */
@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userEmail: String,
    val movieTitle: String,
    val time: String,
    val seatIds: String,
    val purchaseTimestamp: Long
)

