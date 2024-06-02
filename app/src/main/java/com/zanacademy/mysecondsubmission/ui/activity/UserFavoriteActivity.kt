package com.zanacademy.mysecondsubmission.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zanacademy.mysecondsubmission.data.response.ItemsItem
import com.zanacademy.mysecondsubmission.databinding.ActivityUserFavoriteBinding
import com.zanacademy.mysecondsubmission.ui.adapter.UserListAdapter
import com.zanacademy.mysecondsubmission.ui.viewmodel.FavoriteViewModel

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private val adapter = UserListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "List Favorite"

        viewModel = ViewModelProvider(
            this,
//            ViewModelProvider.NewInstanceFactory()
        )[FavoriteViewModel::class.java]


        viewModel.getFavoriteUser()?.observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl!!, followersUrl = "", followingUrl = "", id = it.id)
                items.add(item)
            }
            adapter.submitList(items)
        }

        binding.apply {
            rvFavorite.setHasFixedSize(true)
            rvFavorite.layoutManager = LinearLayoutManager(this@UserFavoriteActivity)
            rvFavorite.adapter = adapter
        }


    }
}