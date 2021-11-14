package ru.s44khin.messenger.ui.main.streams.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.databinding.FragmentTabStreamsBinding
import ru.s44khin.messenger.ui.main.MainViewModel

abstract class TabStreamFragment : Fragment() {

    private var _binding: FragmentTabStreamsBinding? = null
    protected val binding get() = _binding!!
    protected val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    protected fun initRecyclerView(streams: List<ResultStream>) = binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        adapter = StreamAdapter(streams)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}