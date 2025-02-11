package com.example.comp4342mobilecomputinggroupproject.activities.Admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.databinding.ActivityAdminBinding

class AdminMainActivity: AppCompatActivity() {

    val binding by lazy {
        ActivityAdminBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.adminHostFragment)
        binding.adminBottomNavigation.setupWithNavController(navController)

    }
}