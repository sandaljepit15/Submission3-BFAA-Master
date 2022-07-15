package com.example.submission2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission2.api.RetrofitConfig
import com.example.submission2.database.FavoriteDao
import com.example.submission2.database.FavoriteUsersDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission2.database.FavoriteUsers

class ProfileViewModel(application: Application) : AndroidViewModel(application){


    private val userProfil = MutableLiveData<GithubUsers>()
    private val _showLoading : MutableLiveData<Boolean> = MutableLiveData()
    private val _message : MutableLiveData<String> = MutableLiveData()
    private val favoriteUsersDatabase: FavoriteUsersDatabase = FavoriteUsersDatabase.getDatabase(application)
    private var favoriteDao: FavoriteDao? = favoriteUsersDatabase.favoriteDao()
    private val _userFavorit : MutableLiveData<Boolean> = MutableLiveData()

    fun getUserProfil(username: String?){
        _showLoading.value = true
        val client = RetrofitConfig.apiService.getUsersProfil(username)
        client.enqueue(object : retrofit2.Callback<GithubUsers>{

            override fun onResponse(call: Call<GithubUsers>, response: Response<GithubUsers>) {
                if (response.isSuccessful) {
                    userProfil.value = response.body()
                    _showLoading.value = false
                }else{
                    _showLoading.value = false
                    _message.value = "failed to load data"
                }
            }

            override fun onFailure(call: Call<GithubUsers>, t: Throwable) {
                _showLoading.value = false
                _message.value = t.message.toString()
            }


        } )
    }

    fun getProfil(): LiveData<GithubUsers> {
        return userProfil

    }    fun getLoading():LiveData<Boolean>{
        return _showLoading
    }
    fun getError():LiveData<String>{
        return _message
    }

    fun insertDeleteFavorite(userFavorit: FavoriteUsers, deleteInsert: Boolean) = viewModelScope.launch(Dispatchers.IO){
        if (deleteInsert) {
            favoriteDao?.delete(userFavorit)
            checkedUserFavorite(userFavorit.login)
            _message.postValue("Remove favorite")
        } else {
            favoriteDao?.insert(userFavorit)
            checkedUserFavorite(userFavorit.login)
            _message.postValue( "Add to favorite")
        }

    }

    fun getUserFavorite():LiveData<Boolean>{
        return _userFavorit
    }

    fun checkedUserFavorite(login: String) = viewModelScope.launch(Dispatchers.IO){
        val result= favoriteDao?.selectedUsers(login)
        if (result == 0){
            _userFavorit.postValue(false)
        } else{
            _userFavorit.postValue(true)
        }
    }
}