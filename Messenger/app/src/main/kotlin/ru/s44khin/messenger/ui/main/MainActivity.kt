package ru.s44khin.messenger.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)
        initFragments()
    }

    private fun initFragments() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost)!!
        val navController = navHostFragment.findNavController()
        binding.navView.setupWithNavController(navController)
    }
}