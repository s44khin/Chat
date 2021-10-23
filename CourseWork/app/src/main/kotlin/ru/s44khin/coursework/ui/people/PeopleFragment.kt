package ru.s44khin.coursework.ui.people

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Profile
import ru.s44khin.coursework.data.repository.MainRepository
import ru.s44khin.coursework.databinding.FragmentPeopleBinding
import ru.s44khin.coursework.ui.adapters.PeopleAdapter

class PeopleFragment : Fragment(R.layout.fragment_people) {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val peoples: List<Profile> by lazy {
        MainRepository().getPeoples()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPeopleBinding.bind(view)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = PeopleAdapter(peoples)
        }
    }
}