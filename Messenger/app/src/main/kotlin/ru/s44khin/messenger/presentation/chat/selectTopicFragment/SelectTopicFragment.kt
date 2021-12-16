package ru.s44khin.messenger.presentation.chat.selectTopicFragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.databinding.FragmentSelectTopicBinding
import ru.s44khin.messenger.presentation.chat.MenuHandler

class SelectTopicFragment(
    private val menuHandler: MenuHandler,
    private val content: String,
    private val streamId: Int,
    @ColorInt private val color: Int?
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "SELECT_TOPIC_FRAGMENT"

        fun newInstance(
            menuHandler: MenuHandler,
            content: String,
            streamId: Int,
            @ColorInt color: Int?
        ) = SelectTopicFragment(menuHandler, content, streamId, color)
    }

    private var _binding: FragmentSelectTopicBinding? = null
    private val binding get() = _binding!!
    private val repository = MessengerApplication.instance.appComponent.repository
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectTopicBinding.inflate(inflater, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initColor()
        initTopicList()
        initButton()

        binding.selectTopicRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initTopicList() = repository.getTopics(streamId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                binding.shimmer.visibility = View.GONE
                binding.selectTopicRecyclerView.adapter =
                    SelectTopicAdapter(it.topics, content, this, menuHandler)
            },
            onError = { }
        )

    private fun initButton() {
        binding.selectTopicSend.setOnClickListener {
            menuHandler.sendMessageToTopic(content, binding.selectTopicName.text.toString())
            dismiss()
        }
    }

    private fun initColor() {
        if (color != null) {
            binding.selectTopicSend.setBackgroundColor(color)
            binding.selectTopicNameLayout.boxStrokeColor = color
            binding.selectTopicNameLayout.hintTextColor = ColorStateList.valueOf(color)
            binding.selectTopicNameLayout.boxStrokeErrorColor = ColorStateList.valueOf(Color.RED)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.dispose()
        _binding = null
    }
}