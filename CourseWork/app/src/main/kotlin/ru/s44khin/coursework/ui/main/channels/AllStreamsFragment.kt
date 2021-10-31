package ru.s44khin.coursework.ui.main.channels

import android.os.Bundle
import android.view.View
import ru.s44khin.coursework.ui.main.channels.StreamsFragment

class AllStreamsFragment : StreamsFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
        }
    }
}