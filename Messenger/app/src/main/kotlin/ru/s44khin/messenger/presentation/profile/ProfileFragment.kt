package ru.s44khin.messenger.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.databinding.FragmentProfileBinding
import ru.s44khin.messenger.presentation.profile.elm.Effect
import ru.s44khin.messenger.presentation.profile.elm.Event
import ru.s44khin.messenger.presentation.profile.elm.State
import ru.s44khin.messenger.utils.showSnackbar
import vivid.money.elmslie.android.base.ElmFragment

class ProfileFragment : ElmFragment<Event, Effect, State>() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override val initEvent = Event.Ui.LoadProfileFirst

    override fun createStore() = MessengerApplication.instance.profileComponent.profileStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun render(state: State) {
        binding.shimmer.isVisible = state.isLoadingDB
        binding.progressIndicator.isVisible = state.isLoadingNetwork

        if (state.profile != null) {
            Glide.with(binding.avatar)
                .load(state.profile.avatar)
                .circleCrop()
                .into(binding.avatar)

            binding.toolBar.title = state.profile.name
        }

        if (state.error != null)
            showSnackbar(requireContext(), binding.root, binding.progressIndicator)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}