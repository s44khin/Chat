package ru.s44khin.messenger.ui.main.streams.tabs

import android.os.Bundle
import android.view.View

class SubsStreamsFragment : TabStreamFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.oldStreams.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                initRecyclerView(it)
                binding.shimmer.root.visibility = View.GONE
            }
        }

        viewModel.subsStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
            binding.shimmer.root.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
        }

        viewModel.searchSubsStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
        }
    }
}