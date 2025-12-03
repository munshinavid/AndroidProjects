package com.example.ecommapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ecommapp.data.local.dao.*
import com.example.ecommapp.data.local.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Product::class,
        CartItem::class,
        WishlistItem::class,
        Order::class,
        OrderItem::class,
        Address::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun orderDao(): OrderDao
    abstract fun addressDao(): AddressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ecomm_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(db: AppDatabase) {
            val userDao = db.userDao()
            val productDao = db.productDao()

            // Insert test user
            userDao.insert(
                User(
                    email = "1",
                    password = "1",
                    name = "Test User",
                    phone = "1234567890"
                )
            )

            // Insert dummy products
            val products = listOf(
                Product(
                    name = "Wireless Headphones",
                    description = "Premium noise-cancelling wireless headphones with 30-hour battery life",
                    price = 2999.0,
                    category = "Electronics",
                    imageRes = "product1",
                    rating = 4.5f,
                    reviewCount = 234,
                    discount = 20
                ),
                Product(
                    name = "Smart Watch",
                    description = "Fitness tracker with heart rate monitor and GPS",
                    price = 4999.0,
                    category = "Electronics",
                    imageRes = "product2",
                    rating = 4.3f,
                    reviewCount = 189,
                    discount = 15
                ),
                Product(
                    name = "Running Shoes",
                    description = "Lightweight running shoes with breathable mesh",
                    price = 3499.0,
                    category = "Fashion",
                    imageRes = "product3",
                    rating = 4.7f,
                    reviewCount = 456,
                    discount = 25
                ),
                Product(
                    name = "Backpack",
                    description = "Water-resistant laptop backpack with multiple compartments",
                    price = 1299.0,
                    category = "Fashion",
                    imageRes = "product4",
                    rating = 4.4f,
                    reviewCount = 312,
                    discount = 10
                ),
                Product(
                    name = "Coffee Maker",
                    description = "Automatic drip coffee maker with thermal carafe",
                    price = 5499.0,
                    category = "Home",
                    imageRes = "product5",
                    rating = 4.6f,
                    reviewCount = 178,
                    discount = 30
                ),
                Product(
                    name = "Yoga Mat",
                    description = "Non-slip exercise mat with carrying strap",
                    price = 899.0,
                    category = "Sports",
                    imageRes = "product6",
                    rating = 4.8f,
                    reviewCount = 523,
                    discount = 0
                ),
                Product(
                    name = "Bluetooth Speaker",
                    description = "Portable waterproof speaker with 360° sound",
                    price = 1999.0,
                    category = "Electronics",
                    imageRes = "product1",
                    rating = 4.5f,
                    reviewCount = 267,
                    discount = 15
                ),
                Product(
                    name = "Sunglasses",
                    description = "UV protection polarized sunglasses",
                    price = 1599.0,
                    category = "Fashion",
                    imageRes = "product2",
                    rating = 4.2f,
                    reviewCount = 145,
                    discount = 20
                ),
                Product(
                    name = "Water Bottle",
                    description = "Insulated stainless steel water bottle 750ml",
                    price = 699.0,
                    category = "Sports",
                    imageRes = "product3",
                    rating = 4.6f,
                    reviewCount = 389,
                    discount = 0
                ),
                Product(
                    name = "Desk Lamp",
                    description = "LED desk lamp with adjustable brightness",
                    price = 1899.0,
                    category = "Home",
                    imageRes = "product4",
                    rating = 4.4f,
                    reviewCount = 221,
                    discount = 10
                ),
                Product(
                    name = "Phone Case",
                    description = "Shockproof protective phone case",
                    price = 499.0,
                    category = "Electronics",
                    imageRes = "product5",
                    rating = 4.3f,
                    reviewCount = 567,
                    discount = 5
                ),
                Product(
                    name = "Notebook Set",
                    description = "Premium quality ruled notebooks pack of 3",
                    price = 399.0,
                    category = "Home",
                    imageRes = "product6",
                    rating = 4.7f,
                    reviewCount = 412,
                    discount = 0
                ),
                Product(
                    name = "Wireless Headphones",
                    description = "Over-ear Bluetooth headphones with noise cancellation",
                    price = 2499.0,
                    category = "Electronics",
                    imageRes = "product1",
                    rating = 4.5f,
                    reviewCount = 872,
                    discount = 10
                ),
                Product(
                    name = "Ceramic Coffee Mug",
                    description = "Handcrafted 350ml mug, microwave safe",
                    price = 299.0,
                    category = "Kitchen",
                    imageRes = "product2",
                    rating = 4.8f,
                    reviewCount = 245,
                    discount = 5
                ),
                Product(
                    name = "Smart Fitness Band",
                    description = "Track steps, heart rate and sleep with style",
                    price = 1899.0,
                    category = "Electronics",
                    imageRes = "product3",
                    rating = 4.4f,
                    reviewCount = 659,
                    discount = 15
                ),
                Product(
                    name = "Cotton T-Shirt",
                    description = "Soft and breathable 100% cotton round neck T-shirt",
                    price = 599.0,
                    category = "Fashion",
                    imageRes = "product4",
                    rating = 4.6f,
                    reviewCount = 511,
                    discount = 20
                ),
                Product(
                    name = "Sports Water Bottle",
                    description = "Durable BPA-free bottle with flip-top cap, 1L",
                    price = 499.0,
                    category = "Fitness",
                    imageRes = "product5",
                    rating = 4.3f,
                    reviewCount = 378,
                    discount = 10
                ),
                Product(
                    name = "Wireless Mouse",
                    description = "Ergonomic wireless mouse with long battery life",
                    price = 799.0,
                    category = "Electronics",
                    imageRes = "product3",
                    rating = 4.5f,
                    reviewCount = 614,
                    discount = 15
                ),
                Product(
                    name = "Classic Wrist Watch",
                    description = "Elegant stainless steel analog watch for men",
                    price = 1799.0,
                    category = "Fashion",
                    imageRes = "product4",
                    rating = 4.7f,
                    reviewCount = 432,
                    discount = 25
                ),
                Product(
                    name = "Desk Lamp",
                    description = "Adjustable LED desk lamp with touch control",
                    price = 1299.0,
                    category = "Home",
                    imageRes = "product5",
                    rating = 4.4f,
                    reviewCount = 293,
                    discount = 10
                ),
                Product(
                    name = "Yoga Mat",
                    description = "Anti-slip lightweight yoga mat for everyday fitness",
                    price = 899.0,
                    category = "Fitness",
                    imageRes = "product1",
                    rating = 4.6f,
                    reviewCount = 521,
                    discount = 5
                ),
                Product(
                    name = "Laptop Backpack",
                    description = "Water-resistant 15.6 inch laptop backpack with USB port",
                    price = 1499.0,
                    category = "Accessories",
                    imageRes = "product1",
                    rating = 4.5f,
                    reviewCount = 641,
                    discount = 18
                ),
                Product(
                    name = "Scented Candle Set",
                    description = "Pack of 3 lavender-scented candles for a cozy home",
                    price = 699.0,
                    category = "Home",
                    imageRes = "product2",
                    rating = 4.8f,
                    reviewCount = 355,
                    discount = 12
                )
            )
            productDao.insertAll(products)
        }
    }
}