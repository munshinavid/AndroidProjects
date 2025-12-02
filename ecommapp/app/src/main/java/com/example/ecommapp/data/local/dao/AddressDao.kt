package com.example.ecommapp.data.local.dao

import androidx.room.*
import com.example.ecommapp.data.local.entities.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM addresses WHERE userId = :userId")
    fun getUserAddresses(userId: Int): Flow<List<Address>>

    @Insert
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultAddress(userId: Int)
}