package com.example.submission2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUsers::class], version = 1)
abstract class FavoriteUsersDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    companion object {
        @Volatile
        private var INSTANCE: FavoriteUsersDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): FavoriteUsersDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteUsersDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteUsersDatabase::class.java, "favorite_users_database")
                        .build()
                }
            }
            return INSTANCE as FavoriteUsersDatabase
        }
    }
}