package com.seifmortada.applications.quran.core.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.core.navigation.QuranAppNavGraph
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import com.seifmortada.applications.quran.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuranAppTheme {
                QuranAppNavGraph()
            }
        }
    }
}