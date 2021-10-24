package ru.s44khin.coursework.ui.channels.allStreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.data.model.Stream
import ru.s44khin.coursework.data.repository.MainRepository
import ru.s44khin.coursework.databinding.FragmentStreamsBinding
import ru.s44khin.coursework.ui.adapters.StreamAdapter

class AllStreamsFragment() : Fragment() {

    private lateinit var binding: FragmentStreamsBinding
    private val streams: List<Stream> by lazy {
        MainRepository().getAllStreams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = StreamAdapter(streams)
        }
    }
}