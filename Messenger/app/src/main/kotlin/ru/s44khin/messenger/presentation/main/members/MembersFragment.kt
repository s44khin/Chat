package ru.s44khin.messenger.presentation.main.members

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.databinding.FragmentMembersBinding
import ru.s44khin.messenger.presentation.main.Search
import ru.s44khin.messenger.presentation.main.members.adapter.MembersAdapter
import ru.s44khin.messenger.presentation.main.members.elm.Effect
import ru.s44khin.messenger.presentation.main.members.elm.Event
import ru.s44khin.messenger.presentation.main.members.elm.State
import ru.s44khin.messenger.presentation.main.streams.StreamsFragment
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class MembersFragment : ElmFragment<Event, Effect, State>(), Search {

    companion object {
        const val TAG = "MEMBERS_FRAGMENT"
        fun newInstance() = MembersFragment()
    }

    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!
    private val disposeBag = CompositeDisposable()
    override val initEvent = Event.Ui.LoadMembersFirst
    private val adapter = MembersAdapter()
    private var stockMembers: List<Profile>? = null

    override fun createStore() = MessengerApplication.instance.membersComponent.membersStore

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

    override fun render(state: State) {
        binding.shimmer.isVisible = state.isLoadingDB
        binding.progressIndicator.isVisible = state.isLoadingNetwork

        if (state.members != null) {
            adapter.members = state.members
            stockMembers = state.members
        }

        if (state.error != null)
            showSnackbar(requireContext(), binding.root, binding.progressIndicator)
    }

    override fun search(text: String) {
        Observable.fromCallable { stockMembers }
            .map { members -> members.filter { it.name.contains(text, true) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { adapter.members = it },
                onError = { }
            )
            .addTo(disposeBag)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposeBag.dispose()
    }
}