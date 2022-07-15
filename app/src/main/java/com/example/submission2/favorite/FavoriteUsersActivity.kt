package com.example.submission2.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.Adapter
import com.example.submission2.GithubUsers
import com.example.submission2.ProfileActivity
import submission2.R
import submission2.databinding.ActivityFavoriteUsersBinding

class FavoriteUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUsersBinding
    private lateinit var adapter: Adapter
    private lateinit var viewModel: FavoriteUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_users)
        binding =
            ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvList.setHasFixedSize(true)
        adapter = Adapter(arrayListOf())

        val favoriteModel = ViewModelProvider(this)[FavoriteUsersViewModel::class.java]
        viewModel = favoriteModel

        viewModel.getFavoriteUsers()?.observe(this, { it ->
            if (it != null) {
                val favoriteList =it.map { GithubUsers(it.login,it.id, it.avatar_url,null,
                null,null,null,null,null) }
                adapter.run { setData(favoriteList) }
            }
        })

        viewModel.getLoading().observe(this, {
            if (it){
                binding.progressbar.visibility = View.VISIBLE
            } else{
                binding.progressbar.visibility = View.INVISIBLE
            }
        })
        viewModel.getNoResult().observe(this, {
            if (it){
                binding.tvNoResult.visibility = View.VISIBLE
            } else{
                binding.tvNoResult.visibility = View.INVISIBLE
            }
        })
        showFavorite()
        supportActionBar?.apply {
            title = resources.getString(R.string.favorite)
        }
    }
    private fun showFavorite() {
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter
        adapter.setOnListClick(object : Adapter.OnListClick {
            override fun onItemClicked(data: GithubUsers) {
                detailUser(data)
            }
        })
    }
    private fun detailUser(user: GithubUsers){
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.SELECTED_USER, user)
        startActivity(intent)
    }

}