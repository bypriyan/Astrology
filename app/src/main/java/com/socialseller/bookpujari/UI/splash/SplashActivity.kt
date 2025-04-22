package com.socialseller.bookpujari.UI.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.auth.AuthActivity
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ Always call super first
        super.onCreate(savedInstanceState)

        // ✅ Install splash screen after super.onCreate
        val splashScreen = installSplashScreen()

        // ✅ Keep splash screen until auth status is loaded
        var keepScreen = true
        splashScreen.setKeepOnScreenCondition { keepScreen }

        // ✅ Observe ViewModel
        viewModel.authStatus.observe(this) { (token, profession) ->
            keepScreen = false

            if (!token.isNullOrEmpty() && !profession.isNullOrEmpty() && profession != "null") {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
            }

            finish() // Close SplashActivity
        }

        // ✅ Optional: Set a minimal view to avoid layout issues
        setContentView(View(this))
    }
}
