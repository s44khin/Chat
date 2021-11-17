package ru.s44khin.messenger.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

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
        viewModel.error.observe(viewLifecycleOwner) {
            showSnackbar()
        }
        viewModel.oldProfile.observe(viewLifecycleOwner) {
            initViews(it)
            binding.shimmer.visibility = View.GONE
        }
        viewModel.newProfile.observe(viewLifecycleOwner) {
            initViews(it)
            binding.shimmer.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
        }
    }

    private fun initViews(newProfile: Profile) = binding.apply {
        toolBar.title = newProfile.name
        Glide.with(avatar)
            .load(newProfile.avatar)
            .circleCrop()
            .into(avatar)
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