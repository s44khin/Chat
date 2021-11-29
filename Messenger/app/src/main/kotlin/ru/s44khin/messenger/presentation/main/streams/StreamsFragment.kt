package ru.s44khin.messenger.presentation.main.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentStreamsBinding
import ru.s44khin.messenger.presentation.main.ChildFragments
import ru.s44khin.messenger.presentation.main.streams.adapters.PagerAdapter
import ru.s44khin.messenger.presentation.main.streams.createStreamFragment.CreateStreamFragment
import ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.AllStreamsFragment
import ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.SubsStreamsFragment

class StreamsFragment : Fragment(), ChildFragments {

    companion object {
        const val TAG = "STREAMS_FRAGMENT"
        fun newInstance() = StreamsFragment()
    }

    private val allStreamsFragment = AllStreamsFragment.newInstance()
    private val subsStreamsFragment = SubsStreamsFragment.newInstance()

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!

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

    private fun initTabs() {
        val tabs = listOf(getString(R.string.subscribed), getString(R.string.allStreams))

        binding.viewPager.adapter = PagerAdapter(
            mutableListOf(subsStreamsFragment, allStreamsFragment),
            childFragmentManager,
            lifecycle
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    override fun search(text: String) {
        subsStreamsFragment.search(text)
        allStreamsFragment.search(text)
    }

    override fun update() {
        allStreamsFragment.update()
        subsStreamsFragment.update()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}