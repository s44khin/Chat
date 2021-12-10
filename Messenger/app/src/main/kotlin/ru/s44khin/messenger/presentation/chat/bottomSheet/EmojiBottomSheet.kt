package ru.s44khin.messenger.presentation.chat.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.messenger.databinding.FragmentEmojiBottomSheetBinding
import ru.s44khin.messenger.presentation.chat.MenuHandler

class EmojiBottomSheet(
    private val content: String,
    private val isMyMessage: Boolean,
    private val menuHandler: MenuHandler
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EMOJI_BOTTOM_SHEET"

        fun newInstance(
            content: String,
            isMyMessage: Boolean,
            menuHandler: MenuHandler
        ) = EmojiBottomSheet(content, isMyMessage, menuHandler)
    }

    private var _binding: FragmentEmojiBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmojiBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            recyclerView.apply {
                layoutManager = GridLayoutManager(context, 5)
                adapter = EmojiAdapter(this@EmojiBottomSheet)
            }

            content.text = this@EmojiBottomSheet.content
        }

        initMenu()
    }

    private fun initMenu() = binding.apply {
        if (!(isMyMessage)) {
            menuDelete.visibility = View.GONE
            menuChangeTopic.visibility = View.GONE
            menuEdit.visibility = View.GONE
        }

        menuCopy.setOnClickListener {
            menuHandler.copyTextToClipboard(this@EmojiBottomSheet.content)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}