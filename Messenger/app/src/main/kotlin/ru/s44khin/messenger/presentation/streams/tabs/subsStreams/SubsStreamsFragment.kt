package ru.s44khin.messenger.presentation.streams.tabs.subsStreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentTabStreamsBinding
import ru.s44khin.messenger.di.GlobalDI
import ru.s44khin.messenger.presentation.streams.adapters.StreamAdapter
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.Effect
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.Event
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.State
import vivid.money.elmslie.android.base.ElmFragment

class SubsStreamsFragment : ElmFragment<Event, Effect, State>() {

    companion object {
        const val TAG = "SubsStreamsFragment"
        fun newInstance() = SubsStreamsFragment()
    }

    private var _binding: FragmentTabStreamsBinding? = null
    private val binding get() = _binding!!
    private val adapter = StreamAdapter()
    override val initEvent = Event.Ui.LoadStreams

    override fun createStore() = GlobalDI.INSTANCE.subsStreamsStoreFactory.provide()

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
        binding.shimmer.root.isVisible = state.isLoading
        binding.progressIndicator.isVisible = state.isLoading

        adapter.streams = state.subsStreams

        if (state.error != null)
            showSnackbar()
    }

    private fun showSnackbar() {
        binding.progressIndicator.visibility = View.GONE

        val snackbar = Snackbar.make(
            binding.root,
            requireActivity().getString(R.string.internetError),
            Snackbar.LENGTH_SHORT
        )

        val view = snackbar.view
        view.translationY = -(58 * requireActivity().resources.displayMetrics.density)

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}