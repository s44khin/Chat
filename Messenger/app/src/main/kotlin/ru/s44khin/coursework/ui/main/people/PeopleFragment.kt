package ru.s44khin.coursework.ui.main.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.data.model.Profile
import ru.s44khin.coursework.databinding.FragmentPeopleBinding
import ru.s44khin.coursework.ui.main.MainViewModel

class PeopleFragment : Fragment() {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.people.observe(viewLifecycleOwner) {
            initRecyclerView(it)
            binding.shimmer.visibility = View.GONE
        }
    }

    private fun initRecyclerView(people: List<Profile>) = binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = PeopleAdapter(people)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}