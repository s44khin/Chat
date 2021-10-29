package ru.s44khin.coursework.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        initFragments()
    }

    private fun initFragments() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost)!!
        val navController = navHostFragment.findNavController()
        binding.navView.setupWithNavController(navController)
    }
}