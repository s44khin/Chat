package ru.s44khin.messenger.presentation.main.streams.tabs.allStreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.databinding.FragmentTabStreamsBinding
import ru.s44khin.messenger.presentation.main.ChildFragments
import ru.s44khin.messenger.presentation.main.streams.adapters.StreamAdapter
import ru.s44khin.messenger.presentation.main.streams.bottomMenu.BottomMenuFragment
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler
import ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.elm.Effect
import ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.elm.Event
import ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.elm.State
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class AllStreamsFragment : ElmFragment<Event, Effect, State>(), ChildFragments, MenuHandler {

    companion object {
        const val TAG = "ALL_STREAMS_FRAGMENT"
        fun newInstance() = AllStreamsFragment()
    }

    private var _binding: FragmentTabStreamsBinding? = null
    private val binding get() = _binding!!
    private val disposeBag = CompositeDisposable()
    private val adapter = StreamAdapter(this)
    private var stockStreams: List<ResultStream>? = null
    override val initEvent = Event.Ui.LoadStreamsFirst

    override fun createStore() = MessengerApplication.instance.allStreamsComponent.allStreamsStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun render(state: State) {
        binding.shimmer.root.isVisible = state.isLoadingDB
        binding.progressIndicator.isVisible = state.isLoadingNetwork

        if (state.allStreams != null) {
            adapter.streams = state.allStreams
            stockStreams = state.allStreams.toList()
        }

        if (state.error != null)
            showSnackbar(requireContext(), binding.root, binding.progressIndicator) {
                update()
            }
    }

    override fun search(text: String) {
        Observable.fromCallable { stockStreams }
            .map { streams -> streams.filter { it.name.contains(text, true) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { adapter.streams = it },
                onError = { }
            )
            .addTo(disposeBag)
    }

    override fun update() {
        store.accept(Event.Ui.LoadStreamsNetwork)
    }

    override fun unsubscribe(streamName: String) {

    }

    override fun subscribe(streamName: String, description: String) {
        store.accept(Event.Ui.SubscribeToStream(streamName, description))
    }

    override fun setStreamColor(streamId: Int, color: String) {
        store.accept(Event.Ui.LoadStreamsNetwork)
    }

    override fun showMenu(
        streamId: Int,
        name: String,
        date: String,
        description: String,
        color: String?
    ) {
        BottomMenuFragment.newInstance(streamId, name, date, description, this, color)
            .show(parentFragmentManager, BottomMenuFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposeBag.dispose()
    }
}