package ru.s44khin.messenger.presentation.main

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.ActivityMainBinding
import ru.s44khin.messenger.presentation.main.elm.Effect
import ru.s44khin.messenger.presentation.main.elm.Event
import ru.s44khin.messenger.presentation.main.elm.State
import ru.s44khin.messenger.presentation.main.members.MembersFragment
import ru.s44khin.messenger.presentation.main.profile.ProfileFragment
import ru.s44khin.messenger.presentation.main.streams.StreamsFragment
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.core.store.Store

class MainActivity : ElmActivity<Event, Effect, State>() {

    private val streamsFragment = StreamsFragment.newInstance()
    private val membersFragment = MembersFragment.newInstance()

    override val initEvent: Event = Event.Ui.LoadProfile

    override fun createStore(): Store<Event, Effect, State> =
        MessengerApplication.instance.mainComponent.mainStore

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)
        initNavigation()
        initSearch()
    }

    private fun initNavigation() {
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navStreams -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainerView.id, streamsFragment)
                        .commit()
                    true
                }
                R.id.navMembers -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainerView.id, membersFragment)
                        .commit()
                    true
                }
                else -> {
                    error("Unknown menu item")
                }
            }
        }

        binding.navView.selectedItemId = R.id.navStreams
    }

    private fun initSearch() = binding.titleBar.search.doAfterTextChanged { text ->
        streamsFragment.search(text.toString())
        membersFragment.search(text.toString())
    }

    override fun render(state: State) {
        if (state.profile != null) {
            Glide.with(binding.titleBar.avatar)
                .load(state.profile.avatar)
                .circleCrop()
                .into(binding.titleBar.avatar)

            initProfile(state.profile.avatar, state.profile.name, state.profile.email)
        }
    }

    private fun initProfile(
        avatar: String,
        name: String,
        email: String
    ) = binding.titleBar.avatarCard.setOnClickListener {
        ProfileFragment.newInstance(avatar, name, email)
            .show(supportFragmentManager, ProfileFragment.TAG)
    }
}