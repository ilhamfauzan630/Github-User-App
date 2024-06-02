package com.zanacademy.mysecondsubmission.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zanacademy.mysecondsubmission.data.response.ItemsItem
import com.zanacademy.mysecondsubmission.databinding.ItemListGituserBinding
import com.zanacademy.mysecondsubmission.ui.activity.UserDetailActivity

class UserListAdapter: ListAdapter<ItemsItem, UserListAdapter.MyViewHolder>(DIFF_CALLBACK)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListGituserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userLIst = getItem(position)
        holder.bind(userLIst)
    }

    class MyViewHolder(private val binding: ItemListGituserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(userList: ItemsItem){
            binding.tvItemUsername.text = userList.login
            Glide.with(itemView)
                .load(userList.avatarUrl)
                .circleCrop()
                .into(binding.tvItemUserprofile)

            itemView.setOnClickListener {
                val moveWithDataUser = Intent(itemView.context, UserDetailActivity::class.java)
                moveWithDataUser.putExtra("login_key", userList.login)
                moveWithDataUser.putExtra("id_user", userList.id)
                moveWithDataUser.putExtra("avatar_url", userList.avatarUrl)
                itemView.context.startActivity(moveWithDataUser)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}