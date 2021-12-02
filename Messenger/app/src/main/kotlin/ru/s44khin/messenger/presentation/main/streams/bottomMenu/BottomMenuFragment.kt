package ru.s44khin.messenger.presentation.main.streams.bottomMenu

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.messenger.databinding.FragmentMenuBinding
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler

class BottomMenuFragment(
    private val name: String,
    private val date: String,
    private val description: String,
    private val menuHandler: MenuHandler,
    private val color: String? = null
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "MENU_FRAGMENT"

        fun newInstance(
            name: String,
            date: String,
            description: String,
            menuHandler: MenuHandler,
            color: String? = null
        ) = BottomMenuFragment(name, date, description, menuHandler, color)
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
        binding.streamNameMenu.text = name
        binding.streamDateMenu.text = date
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        if (description != "" && description != ".")
            binding.streamDescriptionMenu.text = description
        else
            binding.streamDescriptionMenu.visibility = View.GONE

        if (color != null) {
            binding.streamTagMenu.setTextColor(Color.parseColor(color))
            binding.streamNameMenu.setTextColor(Color.parseColor(color))
            binding.recyclerView.adapter = MenuAdapter(name, description, false, menuHandler, this)
        } else {
            binding.recyclerView.adapter = MenuAdapter(name, description, true, menuHandler, this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}