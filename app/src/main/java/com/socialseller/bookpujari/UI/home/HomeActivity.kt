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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bypriyan.bustrackingsystem.utility.Constants
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.auth.AuthActivity
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
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeUserDetail()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun observeUserDetail() {
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

}