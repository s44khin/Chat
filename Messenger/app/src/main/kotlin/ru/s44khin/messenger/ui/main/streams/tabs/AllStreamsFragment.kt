package ru.s44khin.messenger.ui.main.streams.tabs

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

class AllStreamsFragment : TabStreamFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) {
            showSnackbar()
        }

        viewModel.oldStreams.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                initRecyclerView(it)
                binding.shimmer.root.visibility = View.GONE
            }
        }

        viewModel.allStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
            binding.shimmer.root.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
        }

        viewModel.searchAllStreams.observe(viewLifecycleOwner) {
            initRecyclerView(it)
        }
    }
}