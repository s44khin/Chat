package ru.s44khin.coursework.ui.main.channels

import android.os.Bundle
import android.view.View

class SubsStreamsFragment : StreamsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subsStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
            binding.shimmer.visibility = View.GONE
        }
    }
}