package ru.gb.kotlinapp.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.MainFragmentBinding
import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.getRussianCities
import ru.gb.kotlinapp.model.getWorldCities
import ru.gb.kotlinapp.util.*
import ru.gb.kotlinapp.view.details.DetailsFragment
import ru.gb.kotlinapp.viewmodel.AppState
import ru.gb.kotlinapp.viewmodel.MainViewModel
import java.io.File

private const val IS_WORLD_KEY = "IS_WORLD_KEY"
private const val LIST_OF_TOWNS = "LIST_OF_TOWNS"
private const val CITY_EXIST = "CITY_EXIST"
private const val IS_CITY_KEY = "IS_CITY_KEY"

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var isDataSetRus: Boolean = true

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val managerWork = WorkManager.getInstance(requireContext())

        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }

        val observer = Observer<AppState> {
            renderData(it)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        showListOfTowns()
    }

    private fun saveListOfTowns() {
        activity?.let {
            with(it.getSharedPreferences(LIST_OF_TOWNS, Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, !isDataSetRus)
                apply()
            }
        }
    }

    private fun showListOfTowns() {
        activity?.let {
            if (it.getSharedPreferences(LIST_OF_TOWNS, Context.MODE_PRIVATE)
                    .getBoolean(IS_WORLD_KEY, false)
            ) {
                changeWeatherDataSet()
            } else {
                if (showFavoriteCities() == true) {
                    viewModel.getWeatherFromLocalSourceRusFavorite()
                } else {
                    viewModel.getWeatherFromLocalSourceRus()
                }
            }
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            if (showFavoriteCities() == true) {
                viewModel.getWeatherFromLocalSourceWorldFavorite()
            } else {
                viewModel.getWeatherFromLocalSourceWorld()
            }
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            if (showFavoriteCities() == true) {
                viewModel.getWeatherFromLocalSourceRusFavorite()
            } else {
                viewModel.getWeatherFromLocalSourceRus()
            }
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }.also { isDataSetRus = !isDataSetRus }
        saveListOfTowns()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                appState.weatherData
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.apply {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    mainFragmentRootView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getWeatherFromLocalSourceRus() }
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkingCitiesOnLoadOrAdd()
    }

    private fun checkingCitiesOnLoadOrAdd() {
        if (!preferenceFileExist(CITY_EXIST)) {
            activity?.let {
                with(
                    it.getSharedPreferences(CITY_EXIST, Context.MODE_PRIVATE)
                        .edit()
                ) {
                    putString(IS_CITY_KEY, EXIST_CITY)
                    apply()
                }
            }
//  скопировать из базы в Entity города после первого запуска после установки
            val cityList: MutableList<City> = gatherCities()
            for (city in cityList) {
                viewModel.saveCityToEntity(city)
            }
        } else {
// проверка на совпадение городов в Weather и в базе Entity
            activity?.let {
                with(
                    it.getSharedPreferences(CITY_EXIST, Context.MODE_PRIVATE)
                        .edit()
                ) {
                    putString(IS_CITY_KEY, ADD_CITY)
                    apply()
                }
            }
        }
        /*
         val cityList: MutableList<City> = gatherCities()
         for (city in cityList) {
             viewModel.saveCityToEntity(city)
         }
         */
    }

    private fun preferenceFileExist(fileName: String): Boolean {
        val filePath = File(
            context?.applicationInfo?.dataDir + SHARE_PREF
                    + fileName + SUFFIX_XML
        )
        return filePath.exists()
    }

    private fun gatherCities(): MutableList<City> {
        val cityList = mutableListOf<City>()
        for (cityItem in getWorldCities()) {
            cityList.add(cityItem.city)
        }
        for (cityItem in getRussianCities()) {
            cityList.add(cityItem.city)
        }

        return cityList
    }

    private fun showFavoriteCities(): Boolean? {
        val favoriteState = false
        return activity?.let {
            it.getSharedPreferences(FAVORITE_STATE, Context.MODE_PRIVATE)
                .getBoolean(IS_FAVORITE_STATE, favoriteState)
        }
    }
}