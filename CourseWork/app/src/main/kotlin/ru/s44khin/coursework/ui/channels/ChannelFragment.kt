package ru.s44khin.coursework.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.FragmentChannelsBinding
import ru.s44khin.coursework.ui.channels.allStreams.AllStreamsFragment
import ru.s44khin.coursework.ui.channels.subsStreams.SubsStreamsFragment

class ChannelFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }

    private fun initTabs() {
        val tabs = listOf(getString(R.string.subscribed), getString(R.string.allStreams))
        binding.viewPager.adapter = PagerAdapter(
            mutableListOf(AllStreamsFragment(), SubsStreamsFragment()),
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