package ru.s44khin.messenger.presentation.main.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
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
import ru.s44khin.messenger.presentation.main.ChildFragments
import ru.s44khin.messenger.presentation.main.members.adapter.MembersAdapter
import ru.s44khin.messenger.presentation.main.members.adapter.MembersDiffUtilCallback
import ru.s44khin.messenger.presentation.main.members.elm.Effect
import ru.s44khin.messenger.presentation.main.members.elm.Event
import ru.s44khin.messenger.presentation.main.members.elm.State
import ru.s44khin.messenger.presentation.main.profile.ProfileFragment
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class MembersFragment : ElmFragment<Event, Effect, State>(), ChildFragments, OnClick {

    companion object {
        const val TAG = "MEMBERS_FRAGMENT"
        fun newInstance() = MembersFragment()
    }

    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!
    private val disposeBag = CompositeDisposable()
    override val initEvent = Event.Ui.LoadMembersFirst
    private val adapter = MembersAdapter(this, emptyList())
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
            updateRecyclerView(state.members)
            stockMembers = state.members
        }

        if (state.error != null)
            showSnackbar(requireContext(), binding.root, binding.progressIndicator) {
                update()
            }
    }

    override fun search(text: String) {
        Observable.fromCallable { stockMembers }
            .map { members ->
                members.filter {
                    it.name.contains(text, true)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { updateRecyclerView(it) },
                onError = { }
            )
            .addTo(disposeBag)

    }

    private fun updateRecyclerView(list: List<Profile>) {
        val recyclerViewState = binding.recyclerView.layoutManager?.onSaveInstanceState()
        val membersDiffUtilCallback = MembersDiffUtilCallback(adapter.members, list)
        val diffUtilResult = DiffUtil.calculateDiff(membersDiffUtilCallback, true)
        adapter.members = list
        diffUtilResult.dispatchUpdatesTo(adapter)
        binding.recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    override fun update() {
        store.accept(Event.Ui.LoadMembersNetwork)
    }

    override fun onClick(avatar: String, name: String, email: String) {
        ProfileFragment.newInstance(avatar, name, email)
            .show(parentFragmentManager, ProfileFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposeBag.dispose()
    }
}