package ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import ru.s44khin.messenger.presentation.main.streams.adapters.StreamDiffUtilCallback
import ru.s44khin.messenger.presentation.main.streams.addNewStreamFragment.AddNewStreamFragment
import ru.s44khin.messenger.presentation.main.streams.bottomMenu.BottomMenuFragment
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler
import ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm.Effect
import ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm.Event
import ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm.State
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class SubsStreamsFragment : ElmFragment<Event, Effect, State>(), ChildFragments, MenuHandler {

    companion object {
        const val TAG = "SUBS_STREAMS_FRAGMENT"
        fun newInstance() = SubsStreamsFragment()
    }

    private var _binding: FragmentTabStreamsBinding? = null
    private val binding get() = _binding!!
    private val disposeBag = CompositeDisposable()
    private val adapter = StreamAdapter(this, emptyList())
    private var stockStreams: List<ResultStream>? = null
    override val initEvent = Event.Ui.LoadStreamsFirst

    override fun createStore() = MessengerApplication.instance.subsStreamsComponent.subsStreamsStore

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
        initCreateStream()
    }

    override fun onResume() {
        store.accept(Event.Ui.LoadStreamsNetwork)
        super.onResume()
    }

    override fun render(state: State) {
        binding.shimmer.root.isVisible = state.isLoadingDB
        binding.progressIndicator.isVisible = state.isLoadingNetwork

        if (state.subsStreams != null) {
            updateRecyclerView(state.subsStreams)
            stockStreams = state.subsStreams
        }

        if (state.error != null)
            showSnackbar(requireContext(), binding.root, binding.progressIndicator) {
                update()
            }
    }

    override fun search(text: String) {
        Observable.fromCallable { stockStreams }
            .map { streams ->
                streams.filter {
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

    private fun updateRecyclerView(list: List<ResultStream>) {
        val recyclerViewState = binding.recyclerView.layoutManager?.onSaveInstanceState()
        val streamDiffUtilCallback = StreamDiffUtilCallback(adapter.streams, list)
        val diffUtilResult = DiffUtil.calculateDiff(streamDiffUtilCallback, true)
        adapter.streams = list
        diffUtilResult.dispatchUpdatesTo(adapter)
        binding.recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    override fun update() {
        store.accept(Event.Ui.LoadStreamsNetwork)
    }

    override fun unsubscribe(streamName: String) {
        store.accept(Event.Ui.UnsubscribeFromStream(streamName))
    }

    override fun subscribe(streamName: String, description: String) {
        store.accept(Event.Ui.LoadStreamsNetwork)
    }

    override fun setStreamColor(streamId: Int, color: String) {
        store.accept(Event.Ui.SetStreamColor(streamId, color))
    }

    override fun pinToTop(streamId: Int) {
        store.accept(Event.Ui.PinStreamToTop(streamId))
    }

    override fun unpinFromTop(streamId: Int) {
        store.accept(Event.Ui.UnpinStreamFromTop(streamId))
    }

    override fun showMenu(stream: ResultStream) {
        BottomMenuFragment.newInstance(stream, this).show(
            parentFragmentManager,
            BottomMenuFragment.TAG
        )
    }

    override fun createNewStream(name: String, description: String) {
        store.accept(Event.Ui.CreateNewStream(name, description))
    }

    private fun initCreateStream() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.newStream.hide()
                } else if (dy < 0) {
                    binding.newStream.show()
                }
            }
        })

        binding.newStream.setOnClickListener {
            AddNewStreamFragment.newInstance(this)
                .show(parentFragmentManager, AddNewStreamFragment.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposeBag.dispose()
    }
}