package ru.s44khin.messenger.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentStreamsBinding
import ru.s44khin.messenger.di.GlobalDI
import ru.s44khin.messenger.presentation.streams.adapters.PagerAdapter
import ru.s44khin.messenger.presentation.streams.elm.Effect
import ru.s44khin.messenger.presentation.streams.elm.Event
import ru.s44khin.messenger.presentation.streams.elm.State
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.AllStreamsFragment
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.SubsStreamsFragment
import vivid.money.elmslie.android.base.ElmFragment

class StreamsFragment : ElmFragment<Event, Effect, State>() {

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!
    override val initEvent = Event.Ui.LoadProfileNetwork

    override fun createStore() = GlobalDI.INSTANCE.streamsStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }

    override fun render(state: State) {
        if (state.profile != null) {
            Glide.with(binding.titleBar.avatar)
                .load(state.profile.avatar)
                .circleCrop()
                .into(binding.titleBar.avatar)
        }
    }

    private fun initTabs() {
        val tabs = listOf(getString(R.string.subscribed), getString(R.string.allStreams))

        binding.viewPager.adapter = PagerAdapter(
            mutableListOf(SubsStreamsFragment.newInstance(), AllStreamsFragment.newInstance()),
            childFragmentManager,
            lifecycle
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}