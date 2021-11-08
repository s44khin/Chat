package ru.s44khin.coursework.ui.main.channels

import android.os.Bundle
import android.view.View

class AllStreamsFragment : StreamsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
            binding.shimmer.visibility = View.GONE
        }
    }
}