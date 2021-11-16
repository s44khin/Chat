package ru.s44khin.messenger.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.ActivityMainBinding
import ru.s44khin.messenger.ui.main.members.MembersViewModel
import ru.s44khin.messenger.ui.main.profile.ProfileViewModel
import ru.s44khin.messenger.ui.main.streams.StreamsViewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val membersViewModel: MembersViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val streamsViewModel: StreamsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)
        downloadData()
        initFragments()
    }

    private fun downloadData() {
        membersViewModel.apply {
            getOldMembers()
            getNewMembers()
        }
        profileViewModel.apply {
            getOldProfile()
            getNewProfile()
        }
        streamsViewModel.getAllStreams()
        streamsViewModel.getSubsStreams()
    }

    private fun initFragments() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost)!!
        val navController = navHostFragment.findNavController()
        binding.navView.setupWithNavController(navController)
    }
}