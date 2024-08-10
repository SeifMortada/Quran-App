package com.seifmortada.applications.quran.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.databinding.ActivityMainBinding
import com.seifmortada.applications.quran.domain.model.response.quran.QuranResponse
import com.seifmortada.applications.quran.ui.fragment.quran_fragment.QuranFragmentViewModel
import org.koin.android.ext.android.inject
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: BaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.entryFragment) // Corrected ID

        binding.homeBottomNavView.setupWithNavController(navController)



    }
}