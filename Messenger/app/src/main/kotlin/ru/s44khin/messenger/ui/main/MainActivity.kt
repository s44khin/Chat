package ru.s44khin.messenger.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.ActivityMainBinding
import ru.s44khin.messenger.utils.emojiList
import ru.s44khin.messenger.utils.getEmojis

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        emojiList = getEmojis(resources)
        setContentView(binding.root)
        initFragments()
    }

    private fun initFragments() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost)!!
        val navController = navHostFragment.findNavController()
        binding.navView.setupWithNavController(navController)
    }
}