package ru.gb.kotlinapp.view.setting

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.gb.kotlinapp.databinding.SettingFragmentBinding
import ru.gb.kotlinapp.util.FAVORITE_STATE
import ru.gb.kotlinapp.util.IS_FAVORITE_STATE
import ru.gb.kotlinapp.viewmodel.SettingViewModel


class SettingFragment : Fragment() {
    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by lazy {
        ViewModelProvider(this)[SettingViewModel::class.java]
    }
    private var favoriteState: Boolean = false

    companion object {
        fun newInstance() = SettingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toggleFavorite: Switch = binding.favoriteSwitch

        loadFavoriteState(toggleFavorite)

        toggleFavorite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                saveFavoriteState(true)
            } else {
                // The toggle is disabled
                saveFavoriteState(false)
            }
        }
    }

    private fun loadFavoriteState(@SuppressLint("UseSwitchCompatOrMaterialCode") toggleFavorite: Switch) {
        activity?.let {
            toggleFavorite.isChecked = it.getSharedPreferences(FAVORITE_STATE, Context.MODE_PRIVATE)
                .getBoolean(IS_FAVORITE_STATE, false)
        }
    }

    private fun saveFavoriteState(favoriteState: Boolean) {
        activity?.let {
            with(
                it.getSharedPreferences(FAVORITE_STATE, Context.MODE_PRIVATE)
                    .edit()
            ) {
                putBoolean(IS_FAVORITE_STATE, favoriteState)
                apply()
            }
        }
    }
}