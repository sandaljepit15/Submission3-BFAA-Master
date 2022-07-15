package com.example.submission2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2.database.FavoriteUsers
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import submission2.R
import submission2.databinding.UserProfilBinding

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_USER = "username"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2)
    }



    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: UserProfilBinding
    private var _isFavorite: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profilModel = ViewModelProvider(this)[ProfileViewModel::class.java]


        profileViewModel = profilModel
        profilModel.getProfil().observe(this, {
            if (it != null) {
                showProfil(it)
            }
        })

        profilModel.getLoading().observe(this,{
            if (it == false){
                binding.progressbar.visibility = View.INVISIBLE
            } else {
                binding.progressbar.visibility = View.VISIBLE
            }
        })
        profilModel.getError().observe(this, {
            showMessage(it)
        })
        profilModel.getUserFavorite().observe(this, {
            isFavorite(it)
            _isFavorite = it
        })


        val user = intent.getParcelableExtra<GithubUsers>(SELECTED_USER)


        profileViewModel.getUserProfil(user!!.login)
        profileViewModel.checkedUserFavorite(user.login)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, user.login)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()



      binding.fabFavorite.setOnClickListener {
          profileViewModel.insertDeleteFavorite(FavoriteUsers(id=user.id,login=user.login,avatar_url = user.avatar_url),_isFavorite)
      }
    }


    private fun showProfil(user: GithubUsers?) {
        Glide.with(this)
            .load(user?.avatar_url)
            .apply(RequestOptions().override(25, 25))
            .into(binding.avatar)

        binding.apply {
            username.text = user?.login
            name.text = user?.name
            location.text = user?.location
            company.text = user?.company
            numberRepository.text = user?.public_repos
            numberFollowers.text = user?.followers
            numberFollowing.text = user?.following
        }
        supportActionBar?.apply {
            title = user?.login
        }

    }
    fun isFavorite(status: Boolean){
        if (status){
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}