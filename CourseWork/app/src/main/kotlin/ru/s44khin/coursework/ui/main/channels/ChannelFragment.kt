package ru.s44khin.coursework.ui.main.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.FragmentChannelsBinding
import ru.s44khin.coursework.ui.main.MainViewModel

class ChannelFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

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

        binding.toolBar.search.doAfterTextChanged { text ->
            viewModel.searchSubsStreams(text.toString())
            viewModel.searchAllStreams(text.toString())
        }
    }

    private fun initTabs() {
        val tabs = listOf(getString(R.string.subscribed), getString(R.string.allStreams))
        binding.viewPager.adapter = PagerAdapter(
            mutableListOf(SubsStreamsFragment(), AllStreamsFragment()),
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