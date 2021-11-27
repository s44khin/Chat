package ru.s44khin.messenger.presentation.streams.tabs.allStreams

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
import ru.s44khin.messenger.presentation.streams.SearchStream
import ru.s44khin.messenger.presentation.streams.adapters.StreamAdapter
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.Effect
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.Event
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.State
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class AllStreamsFragment : ElmFragment<Event, Effect, State>(), SearchStream {

    companion object {
        const val TAG = "AllStreamsFragment"
        fun newInstance() = AllStreamsFragment()
    }

    private var _binding: FragmentTabStreamsBinding? = null
    private val binding get() = _binding!!
    private val disposeBag = CompositeDisposable()
    private val adapter = StreamAdapter()
    private var stockStreams: List<ResultStream>? = null
    override val initEvent = Event.Ui.LoadStreamsFirst

    override fun createStore() = MessengerApplication.instance.allStreamsComponent.allStreamStore

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
            showSnackbar(requireContext(), binding.root, binding.progressIndicator)
    }

    override fun search(text: String) {
        if (text.isNotEmpty() && stockStreams != null) {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}