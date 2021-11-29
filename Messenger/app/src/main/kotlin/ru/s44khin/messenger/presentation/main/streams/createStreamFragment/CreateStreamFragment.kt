package ru.s44khin.messenger.presentation.main.streams.createStreamFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentDialogBinding

class CreateStreamFragment : DialogFragment() {

    companion object {
        const val TAG = "DIALOG_FRAGMENT"
        const val REQUEST_KEY = "REQUEST_DIALOG"
        const val RESULT_NAME = "RESULT_DIALOG_NAME"
        const val RESULT_DESCRIPTION = "RESULT_DIALOG_DESCRIPTION"
        fun newInstance() = CreateStreamFragment()
    }

    private var _binding: FragmentDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = resources.getDimension(R.dimen.dialogWidth)
        val height = resources.getDimension(R.dimen.dialogHeight)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(width.toInt(), height.toInt())

        binding.createButton.setOnClickListener {
            if (binding.name.text.isNullOrEmpty() && binding.description.text.isNullOrEmpty()) {
                binding.view.error = getString(R.string.necessarily)
                binding.view2.error = getString(R.string.necessarily)
            } else if (binding.name.text.isNullOrEmpty()) {
                binding.view.error = getString(R.string.necessarily)
            } else if (binding.description.text.isNullOrEmpty()) {
                binding.view2.error = getString(R.string.necessarily)
            } else {
                setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(
                        RESULT_NAME to binding.name.text.toString(),
                        RESULT_DESCRIPTION to binding.description.text.toString()
                    )
                )
                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}