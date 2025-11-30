package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones de compras
 */
@Dao
interface PurchaseDao {
    
    @Insert
    suspend fun insertPurchase(purchase: PurchaseEntity)
    
    @Query("SELECT * FROM purchases WHERE userEmail = :userEmail ORDER BY purchaseTimestamp DESC")
    suspend fun getPurchasesByUser(userEmail: String): List<PurchaseEntity>
    
    @Query("SELECT * FROM purchases WHERE userEmail = :userEmail ORDER BY purchaseTimestamp DESC")
    fun getPurchasesByUserFlow(userEmail: String): Flow<List<PurchaseEntity>>
}

