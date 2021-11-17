package ru.s44khin.messenger.ui.main.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentPeopleBinding

class MembersFragment : Fragment() {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MembersViewModel by activityViewModels()

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

        viewModel.error.observe(viewLifecycleOwner) {
            showSnackbar()
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.oldMembers.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = MembersAdapter(it)
            binding.shimmer.visibility = View.GONE
        }

        viewModel.newMembers.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = MembersAdapter(it)
            binding.shimmer.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
        }
    }

    private fun showSnackbar() {
        binding.progressIndicator.visibility = View.GONE

        val snackbar = Snackbar.make(
            binding.root,
            requireActivity().getString(R.string.internetError),
            Snackbar.LENGTH_SHORT
        )

        val view = snackbar.view
        view.translationY = -(58 * requireActivity().resources.displayMetrics.density)

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}