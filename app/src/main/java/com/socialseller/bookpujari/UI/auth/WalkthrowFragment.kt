package com.socialseller.bookpujari.UI.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentWalkthrowBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class WalkthrowFragment : Fragment(R.layout.fragment_walkthrow) {

    private var _binding: FragmentWalkthrowBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var dataStore: DataStoreManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        checkAuthAndNavigate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWalkthrowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            requestOTPBtn.setOnClickListener {
                findNavController().navigate(R.id.action_WalkthrowFragment_to_loginFragment)
            }
            previousBtn.setOnClickListener {
                findNavController().navigate(R.id.action_WalkthrowFragment_to_registrationFragment)
            }
        }
    }

    private fun checkAuthAndNavigate() {

        lifecycleScope.launch {
            dataStore.getString(Constants.KEY_TOKEN).firstOrNull().let { token ->
                Log.d("token", "checkAuthAndNavigate: $token")
                if (token != null) {
                    dataStore.getString(Constants.KEY_USER_PROFESSION).firstOrNull().let { profession ->
                        Log.d("token", "professtion: $profession")
                        if(profession!=null){
                            findNavController().navigate(R.id.action_WalkthrowFragment_to_profileDetailsFragment)
                            //startActivity(Intent(this@AuthActivity, HomeActivity::class.java))
                        }else{
                            findNavController().navigate(R.id.action_WalkthrowFragment_to_profileDetailsFragment)
                        }
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}