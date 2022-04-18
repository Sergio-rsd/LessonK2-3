package ru.gb.kotlinapp.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.FragmentHistoryBinding
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.util.showSnackBar
import ru.gb.kotlinapp.viewmodel.AppState
import ru.gb.kotlinapp.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyFragmentRecyclerview.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAllHistoryQuery()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                with(binding) {
                    historyFragmentRecyclerview.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                }
                adapter.setData(appState.weatherData)
            }
            is AppState.Loading -> {
                with(binding) {
                    historyFragmentRecyclerview.visibility = View.GONE
                    loadingLayout.visibility = View.VISIBLE
                }
            }
            is AppState.Error -> {
                with(binding) {
                    historyFragmentRecyclerview.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE

                    historyFragmentRecyclerview.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getAllHistoryQuery() }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HistoryFragment()
    }
}