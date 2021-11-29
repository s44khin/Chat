package ru.s44khin.messenger.presentation.main.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import ru.s44khin.messenger.databinding.FragmentProfileBinding

class ProfileFragment(
    private val avatar: String,
    private val name: String,
    private val email: String
) : DialogFragment() {

    companion object {
        const val TAG = "PROFILE_FRAGMENT"
        fun newInstance(
            avatar: String,
            name: String,
            email: String
        ) = ProfileFragment(avatar, name, email)
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.avatar)
            .load(avatar)
            .circleCrop()
            .into(binding.avatar)

        binding.name.text = name
        binding.email.text = email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}