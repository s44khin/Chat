package ru.s44khin.coursework.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.coursework.databinding.FragmentEmojiBottomSheetBinding
import ru.s44khin.coursework.ui.adapters.EmojiAdapter
import ru.s44khin.coursework.utils.emojiList

class EmojiBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EmojiBottomSheet"
    }

    private lateinit var binding: FragmentEmojiBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmojiBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = EmojiAdapter(emojiList, this@EmojiBottomSheet)
        }
    }
}