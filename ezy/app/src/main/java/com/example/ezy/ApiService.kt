// ApiService.kt - Complete API Service for EzyCommerce Android App
package com.example.ezy

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

// Data Classes for API Responses and Requests

// Auth Data Classes
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = ""
)

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val avatar: String
)

data class AuthResponse(
    val message: String,
    val token: String,
    val user: User
)

data class TokenVerificationResponse(
    val valid: Boolean,
    val user: User?
)

// Product Data Classes
data class Product(
    val product_id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val original_price: Double,
    val category_name: String,
    val image_url: String,
    val stock: Int,
    val rating: Double,
    val review_count: Int,
    val is_featured: Int,
    val in_stock: Boolean,
    val badge: String?
)

data class ProductsResponse(
    val success: Boolean,
    val products: List<Product>,
    val pagination: Pagination
)

data class ProductResponse(
    val success: Boolean,
    val product: Product
)

data class Pagination(
    val current_page: Int,
    val total_pages: Int,
    val total_items: Int,
    val items_per_page: Int
)

// Category Data Classes
data class Category(
    val category_id: Int,
    val category_name: String
)

data class CategoriesResponse(
    val success: Boolean,
    val categories: List<Category>
)

// Cart Data Classes
data class CartItem(
    val cart_item_id: Int,
    val product_id: Int,
    val quantity: Int,
    val name: String,
    val price: Double,
    val image_url: String,
    val stock: Int,
    val item_total: Double,
    val discount_type: String,
    val discount_value: Double,
    val discount_amount: Double,
    val final_price: Double
)

data class CartResponse(
    val success: Boolean,
    val message: String,
    val cartItems: List<CartItem>? = null,
    val itemCount: Int? = null,
    val subtotal: String? = null,
    val totalDiscount: String? = null,
    val shippingCost: String? = null,
    val totalCost: String? = null,
    val cart_count: Int? = null
)

data class AddToCartRequest(
    val product_id: Int,
    val quantity: Int,
    val user_id: Int
)

data class UpdateQuantityRequest(
    val cart_item_id: Int,
    val quantity: Int,
    val user_id: Int
)

// Order Data Classes
data class CustomerDetails(
    val full_name: String,
    val address: String,
    val phone: String,
    val address_line1: String? = null,
    val address_line2: String? = null
)

data class PlaceOrderRequest(
    val payment_method: String,
    val customer_details: CustomerDetails,
    val user_id: Int
)

data class OrderResponse(
    val success: Boolean,
    val message: String,
    val order_id: Int? = null,
    val total_amount: Double? = null,
    val payment_method: String? = null
)

// User Dashboard Data Classes
data class DashboardStats(
    val totalOrders: Int,
    val completedOrders: Int,
    val wishlistItems: Int,
    val addressesCount: Int
)

data class Order(
    val id: Int,
    val status: String,
    val total: Double,
    val orderDate: String
)

data class DashboardResponse(
    val stats: DashboardStats,
    val recentOrders: List<Order>
)

data class OrdersResponse(
    val orders: List<Order>,
    val pagination: Pagination
)

// Address Data Classes
data class Address(
    val id: Int,
    val full_name: String,
    val address_line1: String,
    val address_line2: String,
    val city: String,
    val state: String,
    val zip_code: String,
    val country: String,
    val phone: String,
    val is_default: Int,
    val type: String = "home"
)

data class CreateAddressRequest(
    val full_name: String,
    val address_line1: String,
    val address_line2: String = "",
    val city: String = "",
    val state: String = "",
    val zip_code: String = "",
    val country: String = "",
    val phone: String,
    val type: String = "home"
)

// Profile Data Classes
data class ProfileResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val avatar: String,
    val created_at: String
)

data class UpdateProfileRequest(
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val phone: String? = null,
    val current_password: String? = null,
    val new_password: String? = null
)

// Wishlist Data Classes
data class WishlistItem(
    val wishlist_id: Int,
    val product_id: Int,
    val name: String,
    val price: Double,
    val image_url: String,
    val stock: Int,
    val added_at: String
)

data class WishlistResponse(
    val success: Boolean,
    val wishlist_items: List<WishlistItem>? = null,
    val items: List<WishlistItem>? = null,
    val pagination: Pagination? = null,
    val message: String? = null,
    val wishlist_count: Int? = null
)

data class AddToWishlistRequest(
    val product_id: Int
)

// Newsletter Data Classes
data class NewsletterRequest(
    val email: String
)

// Generic Response Classes
data class ApiResponse(
    val success: Boolean,
    val message: String,
    val error: String? = null
)

// API Client Configuration
object ApiClient {
    private const val BASE_URL = "https://ezyshop-mvvq.onrender.com/Customer/controllers/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: EzyCommerceApiService = retrofit.create(EzyCommerceApiService::class.java)
}

// Fixed API Interface - Replace your existing EzyCommerceApiService
interface EzyCommerceApiService {

    // Auth Endpoints (these work fine)
    @POST("AuthController.php?endpoint=login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("AuthController.php?endpoint=register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("AuthController.php?endpoint=logout")
    suspend fun logout(@Header("Authorization") token: String): ApiResponse

    @GET("AuthController.php?endpoint=verify")
    suspend fun verifyToken(@Header("Authorization") token: String): TokenVerificationResponse

    // Products Endpoints (these work fine)
    @GET("HomeController.php/products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 8,
        @Query("filter") filter: String = "all",
        @Query("sort") sort: String = "newest",
        @Query("category") category: String = "",
        @Query("search") search: String = ""
    ): ProductsResponse

    @GET("HomeController.php/products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): ProductResponse

    @GET("HomeController.php/categories")
    suspend fun getCategories(): CategoriesResponse

    // FIXED Cart Endpoints - Using @FormUrlEncoded and @Field
    @GET("CartController.php?action=fetchCart")
    suspend fun getCart(@Query("user_id") userId: Int): CartResponse

    @FormUrlEncoded
    @POST("CartController.php?action=addToCart")
    suspend fun addToCart(
        @Field("product_id") productId: Int,
        @Field("quantity") quantity: Int,
        @Field("user_id") userId: Int
    ): CartResponse

    @FormUrlEncoded
    @POST("CartController.php?action=updateQuantity")
    suspend fun updateCartQuantity(
        @Field("cart_item_id") cartItemId: Int,
        @Field("quantity") quantity: Int,
        @Field("user_id") userId: Int
    ): CartResponse

    // Remove from cart uses query parameters (as per your backend)
    @POST("CartController.php?action=removeFromCart")
    suspend fun removeFromCart(
        @Query("cart_item_id") cartItemId: Int,
        @Query("user_id") userId: Int
    ): CartResponse

    @POST("CartController.php?action=clearCart")
    suspend fun clearCart(@Query("user_id") userId: Int): CartResponse

    // FIXED Place Order - Your backend expects form fields, not nested JSON
    @FormUrlEncoded
    @POST("CartController.php?action=placeOrder")
    suspend fun placeOrder(
        @Field("payment_method") paymentMethod: String,
        @Field("customer_details[full_name]") fullName: String,
        @Field("customer_details[address]") address: String,
        @Field("customer_details[phone]") phone: String,
        @Field("user_id") userId: Int
    ): OrderResponse

    // Alternative place order method if the above doesn't work
    @FormUrlEncoded
    @POST("CartController.php?action=placeOrder")
    suspend fun placeOrderAlt(
        @Field("payment_method") paymentMethod: String,
        @Field("full_name") fullName: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("user_id") userId: Int
    ): OrderResponse

    // RESTful Cart Endpoints (Alternative) - Keep these as backup
    @GET("HomeController.php/customers/{customerId}/cart")
    suspend fun getCartRESTful(@Path("customerId") customerId: Int): CartResponse

    @GET("HomeController.php/customers/{customerId}/cart/count")
    suspend fun getCartCount(@Path("customerId") customerId: Int): CartResponse

    // User Dashboard & Profile Endpoints (these work fine)
    @GET("UserController.php?endpoint=dashboard")
    suspend fun getDashboard(@Header("Authorization") token: String): DashboardResponse

    @GET("UserController.php?endpoint=orders")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): OrdersResponse

    @GET("UserController.php?endpoint=profile")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileResponse

    @PUT("UserController.php?endpoint=profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): ProfileResponse

    // Address Endpoints (these work fine)
    @GET("UserController.php?endpoint=addresses")
    suspend fun getAddresses(@Header("Authorization") token: String): List<Address>

    @GET("UserController.php?endpoint=addresses")
    suspend fun getAddress(
        @Header("Authorization") token: String,
        @Query("id") addressId: Int
    ): Address

    @POST("UserController.php?endpoint=addresses")
    suspend fun createAddress(
        @Header("Authorization") token: String,
        @Body request: CreateAddressRequest
    ): ApiResponse

    @PUT("UserController.php?endpoint=addresses")
    suspend fun updateAddress(
        @Header("Authorization") token: String,
        @Query("id") addressId: Int,
        @Body request: CreateAddressRequest
    ): ApiResponse

    @DELETE("UserController.php?endpoint=addresses")
    suspend fun deleteAddress(
        @Header("Authorization") token: String,
        @Query("id") addressId: Int
    ): ApiResponse

    // Wishlist Endpoints (these work fine)
    @GET("UserController.php?endpoint=wishlist")
    suspend fun getWishlist(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): WishlistResponse

    @POST("UserController.php?endpoint=wishlist")
    suspend fun addToWishlist(
        @Header("Authorization") token: String,
        @Body request: AddToWishlistRequest
    ): WishlistResponse

    @DELETE("UserController.php?endpoint=wishlist")
    suspend fun removeFromWishlist(
        @Header("Authorization") token: String,
        @Query("id") productId: Int
    ): WishlistResponse

    // RESTful Wishlist Endpoints (Alternative)
    @GET("HomeController.php/users/{userId}/wishlist")
    suspend fun getWishlistRESTful(@Path("userId") userId: Int): WishlistResponse

    @GET("HomeController.php/users/{userId}/wishlist/count")
    suspend fun getWishlistCount(@Path("userId") userId: Int): WishlistResponse

    @POST("HomeController.php/users/{userId}/wishlist")
    suspend fun addToWishlistRESTful(
        @Path("userId") userId: Int,
        @Body request: AddToWishlistRequest
    ): WishlistResponse

    @DELETE("HomeController.php/users/{userId}/wishlist/{productId}")
    suspend fun removeFromWishlistRESTful(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): WishlistResponse

    // Newsletter Endpoint (this works fine)
    @POST("HomeController.php/newsletter/subscriptions")
    suspend fun subscribeNewsletter(@Body request: NewsletterRequest): ApiResponse
}

// Updated Repository Class with Fixed Cart Methods
class EzyCommerceRepository {
    private val apiService = ApiClient.apiService

    // Auth Methods (unchanged - these work)
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        username: String,
        email: String,
        password: String,
        firstName: String = "",
        lastName: String = "",
        phone: String = ""
    ): Result<AuthResponse> {
        return try {
            val response = apiService.register(
                RegisterRequest(username, email, password, firstName, lastName, phone)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyToken(token: String): Result<TokenVerificationResponse> {
        return try {
            val response = apiService.verifyToken("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Products Methods (unchanged - these work)
    suspend fun getProducts(
        page: Int = 1,
        limit: Int = 8,
        filter: String = "all",
        sort: String = "newest",
        category: String = "",
        search: String = ""
    ): Result<ProductsResponse> {
        return try {
            val response = apiService.getProducts(page, limit, filter, sort, category, search)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProduct(productId: Int): Result<ProductResponse> {
        return try {
            val response = apiService.getProduct(productId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategories(): Result<CategoriesResponse> {
        return try {
            val response = apiService.getCategories()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // FIXED Cart Methods
    suspend fun getCart(userId: Int): Result<CartResponse> {
        return try {
            val response = apiService.getCart(userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToCart(productId: Int, quantity: Int, userId: Int): Result<CartResponse> {
        return try {
            val response = apiService.addToCart(productId, quantity, userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCartQuantity(cartItemId: Int, quantity: Int, userId: Int): Result<CartResponse> {
        return try {
            val response = apiService.updateCartQuantity(cartItemId, quantity, userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromCart(cartItemId: Int, userId: Int): Result<CartResponse> {
        return try {
            val response = apiService.removeFromCart(cartItemId, userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun placeOrder(
        paymentMethod: String,
        customerDetails: CustomerDetails,
        userId: Int
    ): Result<OrderResponse> {
        return try {
            // Try the first method (nested field names)
            val response = try {
                apiService.placeOrder(
                    paymentMethod,
                    customerDetails.full_name,
                    customerDetails.address,
                    customerDetails.phone,
                    userId
                )
            } catch (e: Exception) {
                // If that fails, try the alternative method (flat field names)
                apiService.placeOrderAlt(
                    paymentMethod,
                    customerDetails.full_name,
                    customerDetails.address,
                    customerDetails.phone,
                    userId
                )
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // User Methods (unchanged - these work)
    suspend fun getDashboard(token: String): Result<DashboardResponse> {
        return try {
            val response = apiService.getDashboard("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrders(token: String, page: Int = 1, limit: Int = 10): Result<OrdersResponse> {
        return try {
            val response = apiService.getOrders("Bearer $token", page, limit)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(token: String): Result<ProfileResponse> {
        return try {
            val response = apiService.getProfile("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<ProfileResponse> {
        return try {
            val response = apiService.updateProfile("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Wishlist Methods (unchanged - these work)
    suspend fun getWishlist(token: String, page: Int = 1, limit: Int = 10): Result<WishlistResponse> {
        return try {
            val response = apiService.getWishlist("Bearer $token", page, limit)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToWishlist(token: String, productId: Int): Result<WishlistResponse> {
        return try {
            val response = apiService.addToWishlist("Bearer $token", AddToWishlistRequest(productId))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromWishlist(token: String, productId: Int): Result<WishlistResponse> {
        return try {
            val response = apiService.removeFromWishlist("Bearer $token", productId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Quick Test for Fixed Cart Operations
