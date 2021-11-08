package ru.s44khin.messenger.ui.main.streams.tabs

import android.os.Bundle
import android.view.View

class AllStreamsFragment : TabStreamFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
            binding.shimmer.visibility = View.GONE
        }
    }
}