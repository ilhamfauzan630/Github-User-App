package com.zanacademy.mysecondsubmission.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zanacademy.mysecondsubmission.R
import com.zanacademy.mysecondsubmission.data.response.ItemsItem
import com.zanacademy.mysecondsubmission.databinding.ActivityMainBinding
import com.zanacademy.mysecondsubmission.ui.activity.DarkModeActivity
import com.zanacademy.mysecondsubmission.ui.activity.UserFavoriteActivity
import com.zanacademy.mysecondsubmission.ui.adapter.UserListAdapter
import com.zanacademy.mysecondsubmission.ui.settingpreferences.SettingPreferences
import com.zanacademy.mysecondsubmission.ui.settingpreferences.SettingPreferencesViewModel
import com.zanacademy.mysecondsubmission.ui.settingpreferences.ViewModelFactory
import com.zanacademy.mysecondsubmission.ui.settingpreferences.dataStore
import com.zanacademy.mysecondsubmission.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)

        mainViewModel.userList.observe(this) { userlist ->
            setUserData(userlist)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.findUser(searchView.text.toString())
                    false
                }
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val darkModeViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingPreferencesViewModel::class.java]

        darkModeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setUserData(users: List<ItemsItem>) {
        val adapter = UserListAdapter()
        adapter.submitList(users)
        binding.rvUserList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite -> {
                Intent(this, UserFavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.dark_theme -> {
                Intent(this, DarkModeActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}