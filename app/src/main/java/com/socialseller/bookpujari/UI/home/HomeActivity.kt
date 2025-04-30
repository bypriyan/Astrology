package com.socialseller.bookpujari.UI.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bypriyan.bustrackingsystem.utility.Constants
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.auth.AuthActivity
import com.socialseller.bookpujari.UI.details.PanditDetailsFragment
import com.socialseller.bookpujari.UI.home.chat.ChatFragment
import com.socialseller.bookpujari.databinding.ActivityHomeBinding
import com.socialseller.bookpujari.viewModel.UserViewModel
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val fragmentsMap = mapOf(
        R.id.homeFragment to HomeFragment(),
        R.id.chatFragment to ChatFragment(),
        R.id.likeFragment to LikedFragment(),
        R.id.profileFragment to ProfileFragment()
    )

    private var activeFragment: Fragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupFragments()
        observeUserDetail()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment = supportFragmentManager.findFragmentByTag(item.itemId.toString())
                ?: fragmentsMap[item.itemId]

            if (selectedFragment != null) {
                switchFragment(selectedFragment, item.itemId.toString())
                true
            } else false
        }
    }

    private fun setupFragments() {
        fragmentsMap.forEach { (menuId, fragment) ->
            supportFragmentManager.beginTransaction()
                .add(R.id.home_fragment_container, fragment, menuId.toString())
                .hide(fragment)
                .commit()
        }

        // Set HomeFragment as default
        val defaultFragment = fragmentsMap[R.id.homeFragment]!!
        activeFragment = defaultFragment

        // Explicitly show HomeFragment
        supportFragmentManager.beginTransaction()
            .show(defaultFragment)
            .commit()

        // Set selected item on bottom nav (in case it's not selected by default)
        binding.bottomNavigation.selectedItemId = R.id.homeFragment
    }


    private fun switchFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
    }

    private fun observeUserDetail() {
        val userViewModel: UserViewModel by viewModels()
        lifecycleScope.launch {
            userViewModel.getUserDetails.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        Log.d("uDetails", "observeUserDetail: ${response.data?.data}")
                    },
                    onError = {
                        Toast.makeText(this@HomeActivity, "Session Expired. Please Login Again", Toast.LENGTH_LONG).show()
                        userViewModel.cleadStoredData()
                        startActivity(Intent(this@HomeActivity, AuthActivity::class.java))
                        finish()
                    },
                    logTag = "udetails"
                )
            }
        }
    }


    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }
}
