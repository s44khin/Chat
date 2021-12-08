package ru.s44khin.messenger.presentation.main.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.databinding.FragmentStreamsBinding
import ru.s44khin.messenger.di.DaggerAppComponent
import ru.s44khin.messenger.presentation.main.ChildFragments
import ru.s44khin.messenger.presentation.main.streams.adapters.PagerAdapter
import ru.s44khin.messenger.presentation.main.streams.addNewStreamFragment.AddNewStreamFragment
import ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.AllStreamsFragment
import ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.SubsStreamsFragment
import javax.inject.Inject

class StreamsFragment : Fragment(), ChildFragments, NewStreamHandler {

    companion object {
        const val TAG = "STREAMS_FRAGMENT"
        fun newInstance() = StreamsFragment()
    }

    private val allStreamsFragment = AllStreamsFragment.newInstance()
    private val subsStreamsFragment = SubsStreamsFragment.newInstance()

    private var _binding: FragmentStreamsBinding? = null
    private val binding get() = _binding!!

    private val disposeBag = CompositeDisposable()

    lateinit var repository: ZulipRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamsBinding.inflate(inflater, container, false)
        repository = MessengerApplication.instance.appComponent.repository
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
        initCreateStream()
    }

    private fun initTabs() {
        val tabs = listOf(getString(R.string.subscribed), getString(R.string.allStreams))

        binding.viewPager.adapter = PagerAdapter(
            mutableListOf(subsStreamsFragment, allStreamsFragment),
            childFragmentManager,
            lifecycle
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    override fun search(text: String) {
        subsStreamsFragment.search(text)
        allStreamsFragment.search(text)
    }

    override fun update() {
        allStreamsFragment.update()
        subsStreamsFragment.update()
    }

    override fun createNewStream(name: String, description: String) {
        repository.subscribeToStream(name, description)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onSuccess = { update() },
                onError = {}
            )
            .addTo(disposeBag)

    }

    private fun initCreateStream() = binding.newStream.setOnClickListener {
        AddNewStreamFragment.newInstance(this).show(parentFragmentManager, AddNewStreamFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.dispose()
        _binding = null
    }
}