package com.example.menus7.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [User::class, EmergencyContact::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun emergencyContactDao(): EmergencyContactDao

    companion object {
        private const val TAG = "AppDatabase"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    Log.d(TAG, "Initializing database...")
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                    .fallbackToDestructiveMigration() // This will recreate tables if schema changes
                    .allowMainThreadQueries() // Only for testing, remove in production
                    .build()
                    Log.d(TAG, "Database initialized successfully")
                    INSTANCE = instance
                    instance
                } catch (e: Exception) {
                    Log.e(TAG, "Error initializing database: ${e.message}", e)
                    // Create a dummy database to prevent app from crashing
                    try {
                        Log.d(TAG, "Attempting to create fallback database...")
                        val dummyInstance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "dummy_database"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                        Log.d(TAG, "Fallback database created successfully")
                        INSTANCE = dummyInstance
                        dummyInstance
                    } catch (e2: Exception) {
                        Log.e(TAG, "Failed to create fallback database: ${e2.message}", e2)
                        throw e2 // Re-throw if even the fallback fails
                    }
                }
            }
        }
    }
} 