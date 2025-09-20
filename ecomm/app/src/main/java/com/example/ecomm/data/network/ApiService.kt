// File: data/network/ApiService.kt
package com.example.ecomm.data.network

import com.example.ecomm.data.models.*
import retrofit2.Response
import retrofit2.http.*

private const val BASE_URL = "https://munshinavid.lovestoblog.com/ezycommerce/Customer/controllers/HomeController.php/"

interface ApiService {

    // --- Products ---
    @GET("${BASE_URL}products")
    suspend fun getProducts(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("filter") filter: String? = null,
        @Query("sort") sort: String? = null,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null
    ): Response<List<Product>>

    @GET("${BASE_URL}products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Response<Product>

    // --- Customer Cart ---
    @GET("${BASE_URL}customers/{customerId}/cart")
    suspend fun getCart(@Path("customerId") customerId: Int): Response<List<CartItem>>

    @GET("${BASE_URL}customers/{customerId}/cart/count")
    suspend fun getCartCount(@Path("customerId") customerId: Int): Response<Int>

    @POST("${BASE_URL}customers/{customerId}/cart")
    suspend fun addToCart(
        @Path("customerId") customerId: Int,
        @Body request: AddToCartRequest
    ): Response<Unit>

    @PUT("${BASE_URL}customers/{customerId}/cart/{itemId}")
    suspend fun updateCartItem(
        @Path("customerId") customerId: Int,
        @Path("itemId") itemId: Int,
        @Body request: UpdateCartRequest
    ): Response<Unit>

    @DELETE("${BASE_URL}customers/{customerId}/cart/{itemId}")
    suspend fun removeFromCart(
        @Path("customerId") customerId: Int,
        @Path("itemId") itemId: Int
    ): Response<Unit>

    // --- User Wishlist ---
    @GET("${BASE_URL}users/{userId}/wishlist")
    suspend fun getWishlist(@Path("userId") userId: Int): Response<List<Product>>

    @GET("${BASE_URL}users/{userId}/wishlist/count")
    suspend fun getWishlistCount(@Path("userId") userId: Int): Response<Int>

    @POST("${BASE_URL}users/{userId}/wishlist")
    suspend fun addToWishlist(
        @Path("userId") userId: Int,
        @Body request: WishlistRequest
    ): Response<Unit>

    @DELETE("${BASE_URL}users/{userId}/wishlist/{productId}")
    suspend fun removeFromWishlist(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): Response<Unit>

    // --- Newsletter ---
    @POST("${BASE_URL}newsletter/subscriptions")
    suspend fun subscribeNewsletter(@Body request: NewsletterRequest): Response<Unit>
}

// --- Request Models ---
data class AddToCartRequest(val product_id: Int, val quantity: Int)
data class UpdateCartRequest(val quantity: Int)
data class WishlistRequest(val product_id: Int)
data class NewsletterRequest(val email: String)
