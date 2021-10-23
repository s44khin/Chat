package ru.s44khin.coursework.ui.channels

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.FragmentChannelsBinding
import ru.s44khin.coursework.ui.channels.allStreams.AllStreamsFragment
import ru.s44khin.coursework.ui.channels.subscribed.SubscribedFragment

class ChannelFragment : Fragment(R.layout.fragment_channels) {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChannelsBinding.bind(view)

        val tabs = listOf(getString(R.string.subscribed), getString(R.string.allStreams))
        binding.viewPager.adapter = PagerAdapter(
            mutableListOf(SubscribedFragment(), AllStreamsFragment()),
            childFragmentManager,
            lifecycle
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}