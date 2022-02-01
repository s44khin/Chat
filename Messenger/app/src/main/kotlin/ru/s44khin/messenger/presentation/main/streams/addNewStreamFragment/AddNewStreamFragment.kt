package ru.s44khin.messenger.presentation.main.streams.addNewStreamFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentAddNewStreamBinding
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler

class AddNewStreamFragment(
    private val menuHandler: MenuHandler
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CREATE_NEW_STREAM_FRAGMENT"
        fun newInstance(menuHandler: MenuHandler) = AddNewStreamFragment(menuHandler)
    }

    private var _binding: FragmentAddNewStreamBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.BottomSheetNormalTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewStreamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initButton()
    }

    private fun initButton() = binding.button.setOnClickListener {
        menuHandler.createNewStream(
            binding.name.text.toString(),
            binding.description.text.toString()
        )
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}