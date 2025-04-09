package com.socialseller.clothcrew.utility

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
abstract  class MyActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate: calling activity activity")
        // Handle back button clicks
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    fun setupBackButton(view: View) {
        view.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
    }

    fun clearDataStore(){
        lifecycleScope.launch {
            dataStoreManager.clear()
        }

    }

}