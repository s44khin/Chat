package ru.s44khin.messenger.presentation.main.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentStreamsBinding
import ru.s44khin.messenger.presentation.main.Search
import ru.s44khin.messenger.presentation.main.streams.adapters.PagerAdapter
import ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.AllStreamsFragment
import ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.SubsStreamsFragment

class StreamsFragment : Fragment(), Search {

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
//        onCreateClick()
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

//    private fun onCreateClick() = binding.create.setOnClickListener {
//        CreateStreamFragment.newInstance().show(parentFragmentManager, CreateStreamFragment.TAG)
//
//        setFragmentResultListener(CreateStreamFragment.REQUEST_KEY) { _, bundle ->
//            val name = bundle.getString(CreateStreamFragment.RESULT_NAME)
//                ?: return@setFragmentResultListener
//            val description = bundle.getString(CreateStreamFragment.RESULT_DESCRIPTION)
//                ?: return@setFragmentResultListener
//
//            store.accept(Event.Ui.CreateStream(name, description))
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}