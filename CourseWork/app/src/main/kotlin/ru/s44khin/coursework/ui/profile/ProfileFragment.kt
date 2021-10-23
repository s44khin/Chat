package ru.s44khin.coursework.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Profile
import ru.s44khin.coursework.data.repository.MainRepository
import ru.s44khin.coursework.databinding.FragmentProfileBinding

class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profile: Profile by lazy {
        MainRepository().getProfile()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        binding.profile.text = profile.name
        binding.status.text = profile.status
        binding.online.apply {
            text = if (profile.online) "online" else "offline"
            setTextColor(if (profile.online) Color.GREEN else Color.RED)
        }
        Glide.with(binding.avatar)
            .load(profile.avatar)
            .centerCrop()
            .into(binding.avatar)
    }
}