package ru.s44khin.messenger.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentStreamsBinding
import ru.s44khin.messenger.presentation.streams.adapters.PagerAdapter
import ru.s44khin.messenger.presentation.streams.createStreamFragment.CreateStreamFragment
import ru.s44khin.messenger.presentation.streams.elm.Effect
import ru.s44khin.messenger.presentation.streams.elm.Event
import ru.s44khin.messenger.presentation.streams.elm.State
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.AllStreamsFragment
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.SubsStreamsFragment
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class StreamsFragment : ElmFragment<Event, Effect, State>() {

    private val allStreamsFragment = AllStreamsFragment.newInstance()
    private val subsStreamsFragment = SubsStreamsFragment.newInstance()

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!
    override val initEvent = Event.Ui.LoadProfileDB

    override fun createStore() = MessengerApplication.instance.streamsComponent.streamStore

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
        initSearch()
        onCreateClick()
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
            mutableListOf(subsStreamsFragment, allStreamsFragment),
            childFragmentManager,
            lifecycle
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun initSearch() = binding.titleBar.search.doAfterTextChanged { text ->
        subsStreamsFragment.search(text.toString())
        allStreamsFragment.search(text.toString())
    }

    private fun onCreateClick() = binding.create.setOnClickListener {
        CreateStreamFragment.newInstance().show(parentFragmentManager, CreateStreamFragment.TAG)

        setFragmentResultListener(CreateStreamFragment.REQUEST_KEY) { _, bundle ->
            val name = bundle.getString(CreateStreamFragment.RESULT_NAME)
                ?: return@setFragmentResultListener
            val description = bundle.getString(CreateStreamFragment.RESULT_DESCRIPTION)
                ?: return@setFragmentResultListener

            store.accept(Event.Ui.CreateStream(name, description))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleEffect(effect: Effect) {
        if (effect is Effect.StreamCreated) {
            allStreamsFragment.update()
            subsStreamsFragment.update()
        }

        if (effect is Effect.CreateStreamError) {
            Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_LONG).show()
        }

        Toast.makeText(requireContext(), "ASD", Toast.LENGTH_LONG).show()
    }
}