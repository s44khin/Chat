package ru.s44khin.messenger.ui.main.streams.tabs

import android.os.Bundle
import android.view.View

class SubsStreamsFragment : TabStreamFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subsStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
            binding.shimmer.visibility = View.GONE
        }
        viewModel.searchSubsStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
        }
    }
}