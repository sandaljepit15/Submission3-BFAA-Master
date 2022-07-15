package com.example.submission2.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission2.database.FavoriteDao
import com.example.submission2.database.FavoriteUsers
import com.example.submission2.database.FavoriteUsersDatabase


class FavoriteUsersViewModel(application : Application) : AndroidViewModel(application) {
    private var favoriteDao: FavoriteDao?
    private var favoriteUsersDatabase: FavoriteUsersDatabase? = FavoriteUsersDatabase.getDatabase(application)
    private val _showLoading : MutableLiveData<Boolean> = MutableLiveData()
    private val _showNoResult : MutableLiveData<Boolean> = MutableLiveData()

    init {
        favoriteDao = favoriteUsersDatabase?.favoriteDao()
    }

    fun getFavoriteUsers() : LiveData<List<FavoriteUsers>>?{
        _showLoading.value = true
        val result = favoriteDao?.getFavoriteUsers()
        if (result != null){
            _showLoading.value = false
            _showNoResult.value = false
        } else {
            _showLoading.value = false
            _showNoResult.value = true
        }
        return favoriteDao?.getFavoriteUsers()
    }
    fun getLoading():LiveData<Boolean>{
        return _showLoading
    }

    fun getNoResult():LiveData<Boolean>{
        return _showNoResult
    }
}