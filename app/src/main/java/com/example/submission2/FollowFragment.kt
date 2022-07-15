package com.example.submission2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import submission2.databinding.FragmentFollowBinding



class FollowFragment: Fragment() {

    private lateinit var  followViewModel : FollowViewModel
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val adapter = Adapter(arrayListOf())



    companion object {
        private const val ARG_USERNAME = "USERNAME"
        private const val ARG_FOLLOW = "FOLLOW"

        fun newInstance(username: String?,follow: Int): FollowFragment {
            val fragment = FollowFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            args.putInt(ARG_FOLLOW,follow)

            fragment.arguments = args
            return fragment
        }
    }

    private var username: String? = null
    private var follow: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            username = requireArguments().getString(ARG_USERNAME)
            follow = requireArguments().getInt(ARG_FOLLOW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.rvFollow.visibility = View.VISIBLE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val followModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowViewModel::class.java]
        followViewModel = followModel
        showFollow()
        followModel.followUser().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.run { setData(it) }
            }
        })
        followModel.getLoading().observe(viewLifecycleOwner,{
            if (it == false){
                binding.progressbar.visibility = View.INVISIBLE
            } else {
                binding.progressbar.visibility = View.VISIBLE
            }
        })
        followModel.getError().observe(viewLifecycleOwner, {
            showError(it)
        })

        getFollow(username,follow)
    }

    private fun showFollow() {
        binding.rvFollow.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.adapter = adapter
        adapter.setOnListClick(object : Adapter.OnListClick {
            override fun onItemClicked(data: GithubUsers) {
               }
        })
    }

    private fun getFollow(username: String?,follow: Int){
        followViewModel.getUserFollow(username,follow)
    }
    private fun showError(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}