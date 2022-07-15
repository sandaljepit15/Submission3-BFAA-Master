package com.example.submission2.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteUsers: FavoriteUsers)

    @Delete
    fun delete(favoriteUsers: FavoriteUsers)

    @Query("SELECT count(*) From favoriteusers where FavoriteUsers.login = :login")
    fun selectedUsers(login:String): Int

    @Query("SELECT * from favoriteusers ORDER BY id DESC")
    fun getFavoriteUsers(): LiveData<List<FavoriteUsers>>
}
