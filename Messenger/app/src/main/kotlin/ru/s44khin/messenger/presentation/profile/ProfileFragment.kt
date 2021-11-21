package ru.s44khin.messenger.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.FragmentProfileBinding
import ru.s44khin.messenger.di.GlobalDI
import ru.s44khin.messenger.presentation.profile.elm.Effect
import ru.s44khin.messenger.presentation.profile.elm.Event
import ru.s44khin.messenger.presentation.profile.elm.State
import vivid.money.elmslie.android.base.ElmFragment

class ProfileFragment : ElmFragment<Event, Effect, State>() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override val initEvent = Event.Ui.LoadProfile

    override fun createStore() = GlobalDI.INSTANCE.profileStoreFactory.provide()

    override fun render(state: State) {
        binding.shimmer.isVisible = state.isLoading
        binding.progressIndicator.isVisible = state.isLoading

        if (state.profile != null) {
            Glide.with(binding.avatar)
                .load(state.profile.avatar)
                .circleCrop()
                .into(binding.avatar)

            binding.toolBar.title = state.profile.name
        }

        if (state.error != null)
            showSnackbar()
    }

    private fun showSnackbar() {
        binding.progressIndicator.visibility = View.GONE

        val snackbar = Snackbar.make(
            binding.root,
            requireActivity().getString(R.string.internetError),
            Snackbar.LENGTH_SHORT
        )

        val view = snackbar.view
        view.translationY = -(58 * requireActivity().resources.displayMetrics.density)

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}