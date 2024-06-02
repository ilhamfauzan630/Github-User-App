package com.zanacademy.mysecondsubmission.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zanacademy.mysecondsubmission.R
import com.zanacademy.mysecondsubmission.data.response.ItemsItem
import com.zanacademy.mysecondsubmission.databinding.FragmentFollowBinding
import com.zanacademy.mysecondsubmission.ui.adapter.UserListAdapter
import com.zanacademy.mysecondsubmission.ui.viewmodel.FollowerViewModel
import com.zanacademy.mysecondsubmission.ui.viewmodel.FollowingViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var followerViewModel: FollowerViewModel
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        const val ARG_POSITION = "position_key"
        const val ARG_USERNAME = "username_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        var username = ""
        var position = 0

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }
        if (position == 1){
            followingViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelProvider.NewInstanceFactory()
            )[FollowingViewModel::class.java]

            followingViewModel.following.observe(viewLifecycleOwner) {following ->
                setUserData(following)
            }
            followingViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

            followingViewModel.getFollowing(username)
        } else {
            followerViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelProvider.NewInstanceFactory()
            )[FollowerViewModel::class.java]

            followerViewModel.follower.observe(viewLifecycleOwner) {follower ->
                setUserData(follower)
            }
            followerViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

            followerViewModel.getFollower(username)
        }
    }

    private fun setUserData(users: List<ItemsItem>) {
        val adapter = UserListAdapter()
        adapter.submitList(users)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}