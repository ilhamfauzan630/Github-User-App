package com.zanacademy.mysecondsubmission.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zanacademy.mysecondsubmission.data.response.ItemsItem
import com.zanacademy.mysecondsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _follower = MutableLiveData<List<ItemsItem>>()
    val follower: LiveData<List<ItemsItem>> = _follower

    companion object {
        private const val TAG = "UserDetailActivity"
    }

    fun getFollower(login: String) {
        _isLoading.value = true
        val user = ApiConfig.getApiService().getFollowers(
            login
        )
        user.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _follower.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}" )
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }
}