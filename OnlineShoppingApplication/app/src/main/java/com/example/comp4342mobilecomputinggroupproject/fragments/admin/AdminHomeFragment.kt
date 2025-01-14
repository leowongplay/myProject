package com.example.comp4342mobilecomputinggroupproject.fragments.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.activities.Admin.AdminCategoryManActivity
import com.example.comp4342mobilecomputinggroupproject.activities.Admin.AdminProductManActivity
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentAdminHomeBinding


class AdminHomeFragment: Fragment(R.layout.fragment_admin_home) {

    private lateinit var binding: FragmentAdminHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //init binding
        binding = FragmentAdminHomeBinding.inflate(inflater)

        //constrain layout userMCard OnClickListener
        binding.userMCard.setOnClickListener(){

        }

        //constrain layout productMCard OnClickListener
        binding.productMCard.setOnClickListener(){
            println("productMCard on click")

            val intent = Intent(
                requireContext(),
                AdminProductManActivity::class.java
            )
            startActivity(intent)

        }

        //constrain layout categoryMCard OnClickListener
        binding.categoryMCard.setOnClickListener(){
            println("categoryMCard on click")

            val intent = Intent(
                requireContext(),
                AdminCategoryManActivity::class.java
            )
            startActivity(intent)

        }

        //constrain layout adminMCard OnClickListener
        binding.adminMCard.setOnClickListener(){

        }
        
        //return
        return binding.root
    }

}