package ru.s44khin.messenger.presentation.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentMembersBinding
import ru.s44khin.messenger.di.GlobalDI
import ru.s44khin.messenger.presentation.members.adapter.MembersAdapter
import ru.s44khin.messenger.presentation.members.elm.Effect
import ru.s44khin.messenger.presentation.members.elm.Event
import ru.s44khin.messenger.presentation.members.elm.State
import vivid.money.elmslie.android.base.ElmFragment

class MembersFragment : ElmFragment<Event, Effect, State>() {

    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!

    private val adapter = MembersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            adapter = this@MembersFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override val initEvent = Event.Ui.LoadMembersDB

    override fun createStore() = GlobalDI.INSTANCE.membersStoreFactory

    override fun render(state: State) {
        binding.shimmer.isVisible = state.isLoadingDB
        binding.progressIndicator.isVisible = state.isLoadingNetwork

        if (state.members != null)
            adapter.members = state.members

        if (state.error != null)
            showSnackbar()
    }

    private fun showSnackbar() {
        binding.progressIndicator.visibility = View.GONE

        val snackbar = Snackbar.make(
            binding.root,
            requireActivity().getString(R.string.internetError),
            Snackbar.LENGTH_INDEFINITE
        )

        snackbar.setAction("Update") {
            createStore().accept(Event.Ui.LoadMembersNetwork)
        }

        val view = snackbar.view
        view.translationY = -(58 * requireActivity().resources.displayMetrics.density)

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}