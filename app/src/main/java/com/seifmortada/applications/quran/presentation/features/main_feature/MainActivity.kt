package com.seifmortada.applications.quran.presentation.features.main_feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.entryFragment) // Corrected ID

        binding.homeBottomNavView.setupWithNavController(navController)



    }
}