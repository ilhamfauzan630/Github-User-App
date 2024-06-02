package com.zanacademy.mysecondsubmission.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zanacademy.mysecondsubmission.data.local.entity.FavoriteRoomDatabase
import com.zanacademy.mysecondsubmission.data.local.entity.FavoriteUser
import com.zanacademy.mysecondsubmission.data.local.entity.FavoriteUserDao
import com.zanacademy.mysecondsubmission.data.response.DetailUserResponse
import com.zanacademy.mysecondsubmission.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application): AndroidViewModel(application) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userdetail = MutableLiveData<DetailUserResponse>()
    val userdetail: LiveData<DetailUserResponse> = _userdetail

    private val favoriteUserDao: FavoriteUserDao?
    private val favoriteUserDb: FavoriteRoomDatabase?

    init {
        favoriteUserDb = FavoriteRoomDatabase.getDatabase(application)
        favoriteUserDao = favoriteUserDb.favoriteUserDao()
    }

    companion object {
        private const val TAG = "UserDetailActivity"
    }

    fun findDetailUser(login: String) {
        _isLoading.value = true
        val user = ApiConfig.getApiService().getDetailUser(
            login
        )
        user.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userdetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}" )
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun addUserToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(id, username, avatarUrl)
            favoriteUserDao?.insert(user)
        }
    }

    suspend fun checkUser(id: Int) = favoriteUserDao?.checkListUser(id)

    fun deleteUserFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteUserDao?.delete(id)
        }
    }
}