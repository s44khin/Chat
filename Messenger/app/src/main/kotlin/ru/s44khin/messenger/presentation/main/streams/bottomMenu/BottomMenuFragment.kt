package ru.s44khin.messenger.presentation.main.streams.bottomMenu

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.databinding.FragmentMenuBinding
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler
import ru.s44khin.messenger.utils.parse2

class BottomMenuFragment(
    private val stream: ResultStream,
    private val menuHandler: MenuHandler,
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "MENU_FRAGMENT"

        fun newInstance(stream: ResultStream, menuHandler: MenuHandler) =
            BottomMenuFragment(stream, menuHandler)
    }

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.streamNameMenu.text = stream.name
        binding.streamDateMenu.text = parse2(stream.date!!)

        if (stream.description != "" && stream.description != ".")
            binding.streamDescriptionMenu.text = stream.description
        else
            binding.streamDescriptionMenu.visibility = View.GONE

        if (stream.color != null)
            initSubs()
        else
            initUnSubs()
    }

    private fun initSubs() = binding.apply {
        val color = Color.parseColor(stream.color)

        streamTagMenu.setTextColor(color)
        streamNameMenu.setTextColor(color)

        menuSubscribe.visibility = View.GONE

        menuUnsubscribe.setOnClickListener {
            menuHandler.unsubscribe(stream.name)
            dismiss()
        }

        menuSetColor.setOnClickListener {
            ColorPickerDialogBuilder
                .with(context)
                .setTitle(context?.getString(R.string.set_color))
                .initialColor(color)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .lightnessSliderOnly()
                .setPositiveButton(context?.getText(R.string.сonfirm)) { _, lastSelectedColor, _ ->
                    val strColor =
                        java.lang.String.format("#%06X", 0xFFFFFF.and(lastSelectedColor))
                    menuHandler.setStreamColor(stream.streamId, strColor)
                    dismiss()
                }
                .build()
                .show()
        }
    }

    private fun initUnSubs() = binding.apply {
        menuUnsubscribe.visibility = View.GONE
        menuSetColor.visibility = View.GONE

        menuSubscribe.setOnClickListener {
            menuHandler.subscribe(stream.name, stream.description)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}