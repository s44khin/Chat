package ru.s44khin.coursework.ui.main.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import ru.s44khin.coursework.data.model.Profile
import ru.s44khin.coursework.databinding.FragmentProfileBinding
import ru.s44khin.coursework.ui.main.MainViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.profile.observe(viewLifecycleOwner) {
            initViews(it)
        }
    }

    private fun initViews(item: Profile) = binding.apply {
        profile.text = item.name
        status.text = item.status
        online.apply {
            text = if (item.online) "online" else "offline"
            setTextColor(if (item.online) Color.GREEN else Color.RED)
        }
        Glide.with(avatar)
            .load(item.avatar)
            .circleCrop()
            .into(avatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}