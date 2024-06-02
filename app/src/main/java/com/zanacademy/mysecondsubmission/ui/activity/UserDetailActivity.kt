package com.zanacademy.mysecondsubmission.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zanacademy.mysecondsubmission.R
import com.zanacademy.mysecondsubmission.data.response.DetailUserResponse
import com.zanacademy.mysecondsubmission.databinding.ActivityUserDetailBinding
import com.zanacademy.mysecondsubmission.ui.adapter.SectionPageAdapter
import com.zanacademy.mysecondsubmission.ui.viewmodel.UserDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userDetailViewModel: UserDetailViewModel

    companion object {
        const val LOGIN = "login_key"
        const val ID_USER = "id_user"
        const val AVATARURL = "avatar_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following,
            R.string.follower
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(LOGIN)
        val id = intent.getIntExtra(ID_USER, 0)
        val avatarURL = intent.getStringExtra(AVATARURL)

        supportActionBar?.hide()

        userDetailViewModel = ViewModelProvider(
            this,
//            ViewModelProvider.NewInstanceFactory()
        )[UserDetailViewModel::class.java]

        userDetailViewModel.userdetail.observe(this) { detailUser ->
            setDataUser(detailUser)
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (name != null) {
            userDetailViewModel.findDetailUser(name)
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = userDetailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    }else {
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (name != null) {
                    if (avatarURL != null) {
                        userDetailViewModel.addUserToFavorite(name, id, avatarURL)
                    }
                }
            }else{
                userDetailViewModel.deleteUserFromFavorite(id)
            }
            binding.toggleFavorite.isChecked = _isChecked
        }

        val sectionsPagerAdapter = SectionPageAdapter(this)
        if (name != null) {
            sectionsPagerAdapter.username = name
        }
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

    }

    private fun setDataUser(detailUser: DetailUserResponse) {
        binding.login.text = detailUser.login
        binding.name.text = detailUser.name
        binding.following.text = resources.getString(R.string.following_amount, detailUser.following)
        binding.follower.text = resources.getString(R.string.follower_amount, detailUser.followers)

        Glide.with(this@UserDetailActivity)
            .load(detailUser.avatarUrl)
            .circleCrop()
            .into(binding.avatar)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}