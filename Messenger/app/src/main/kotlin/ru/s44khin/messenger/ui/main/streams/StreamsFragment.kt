package ru.s44khin.messenger.ui.main.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentStreamsBinding
import ru.s44khin.messenger.ui.main.profile.ProfileViewModel
import ru.s44khin.messenger.ui.main.streams.tabs.AllStreamsFragment
import ru.s44khin.messenger.ui.main.streams.tabs.PagerAdapter
import ru.s44khin.messenger.ui.main.streams.tabs.SubsStreamsFragment

class StreamsFragment : Fragment() {

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!
    private val streamsViewModel: StreamsViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

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
        profileViewModel.oldProfile.observe(viewLifecycleOwner) {
            Glide.with(binding.titleBar.avatar)
                .load(it.avatar)
                .circleCrop()
                .into(binding.titleBar.avatar)
        }
        profileViewModel.newProfile.observe(viewLifecycleOwner) {
            Glide.with(binding.titleBar.avatar)
                .load(it.avatar)
                .circleCrop()
                .into(binding.titleBar.avatar)
        }
        binding.titleBar.search.doAfterTextChanged {
            streamsViewModel.searchAllStreams(it.toString())
            streamsViewModel.searchSubsStreams(it.toString())
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