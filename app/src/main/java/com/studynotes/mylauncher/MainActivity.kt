package com.studynotes.mylauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.studynotes.mylauncher.databinding.ActivityMainBinding
import com.studynotes.mylauncher.fragments.HomeScreenFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
    }

    private fun setUpViews() {
        setUpFragment(HomeScreenFragment())
    }

    private fun setUpFragment(fragment: Fragment?) {
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.container.id, it)
                .commit()
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.container.id)

        if (currentFragment is HomeScreenFragment) {
            super.onBackPressed()
        } else {
            setUpFragment(HomeScreenFragment())
        }
    }
}