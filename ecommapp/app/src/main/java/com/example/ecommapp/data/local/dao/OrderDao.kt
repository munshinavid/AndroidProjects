package com.example.ecommapp.data.local.dao

import androidx.room.*
import com.example.ecommapp.data.local.entities.Order
import com.example.ecommapp.data.local.entities.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Order): Long

    @Insert
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    fun getUserOrders(userId: Int): Flow<List<Order>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItems(orderId: Int): Flow<List<OrderItem>>
}