package com.zanacademy.mysecondsubmission.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zanacademy.mysecondsubmission.data.local.entity.FavoriteRoomDatabase
import com.zanacademy.mysecondsubmission.data.local.entity.FavoriteUser
import com.zanacademy.mysecondsubmission.data.local.entity.FavoriteUserDao

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private val favoriteUserDao: FavoriteUserDao?
    private val favoriteUserDb: FavoriteRoomDatabase?

    init {
        favoriteUserDb = FavoriteRoomDatabase.getDatabase(application)
        favoriteUserDao = favoriteUserDb.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return favoriteUserDao?.getFavoriteUser()
    }
}