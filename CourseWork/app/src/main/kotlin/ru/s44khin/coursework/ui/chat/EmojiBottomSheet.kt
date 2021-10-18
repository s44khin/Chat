package ru.s44khin.coursework.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.FragmentEmojiBottomSheetBinding
import ru.s44khin.coursework.utils.emojiList

class EmojiBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EmojiBottomSheet"
    }

    private var binding: FragmentEmojiBottomSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_emoji_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentEmojiBottomSheetBinding.bind(view)

        binding!!.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = EmojiAdapter(emojiList)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}