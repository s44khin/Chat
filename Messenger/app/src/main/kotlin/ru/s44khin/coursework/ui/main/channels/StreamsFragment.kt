package ru.s44khin.coursework.ui.main.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.data.model.Stream
import ru.s44khin.coursework.databinding.FragmentStreamsBinding
import ru.s44khin.coursework.ui.main.MainViewModel

abstract class StreamsFragment : Fragment() {

    private var _binding: FragmentStreamsBinding? = null
    protected val binding get() = _binding!!
    protected val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun initRecyclerView(streams: List<Stream>) = binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        adapter = StreamAdapter(streams)
    }
}