package ru.s44khin.coursework.ui.channels.allStreams

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Stream
import ru.s44khin.coursework.data.repository.MainRepository
import ru.s44khin.coursework.databinding.FragmentStreamsBinding
import ru.s44khin.coursework.ui.adapters.StreamAdapter

class AllStreamsFragment : Fragment(R.layout.fragment_streams) {

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!
    private val streams: List<Stream> by lazy {
        MainRepository().getAllStreams()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStreamsBinding.bind(view)

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = StreamAdapter(streams)
        }
    }
}