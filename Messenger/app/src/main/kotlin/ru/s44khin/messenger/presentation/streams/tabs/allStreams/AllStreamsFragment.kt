package ru.s44khin.messenger.presentation.streams.tabs.allStreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentTabStreamsBinding
import ru.s44khin.messenger.di.GlobalDI
import ru.s44khin.messenger.presentation.streams.adapters.StreamAdapter
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.Effect
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.Event
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.State
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class AllStreamsFragment : ElmFragment<Event, Effect, State>() {

    companion object {
        const val TAG = "AllStreamsFragment"
        fun newInstance() = AllStreamsFragment()
    }

    private var _binding: FragmentTabStreamsBinding? = null
    private val binding get() = _binding!!
    private val adapter = StreamAdapter()
    override val initEvent = Event.Ui.LoadStreamsFirst

    override fun createStore() = GlobalDI.INSTANCE.allStreamsStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun render(state: State) {
        binding.shimmer.root.isVisible = state.isLoadingDB
        binding.progressIndicator.isVisible = state.isLoadingNetwork

        if (state.allStreams != null)
            adapter.streams = state.allStreams

        if (state.error != null)
            showSnackbar(requireContext(), binding.root, binding.progressIndicator)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}