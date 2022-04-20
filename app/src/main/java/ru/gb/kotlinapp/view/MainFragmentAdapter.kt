package ru.gb.kotlinapp.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.util.showIf

class MainFragmentAdapter(
    private var onItemViewClickListener: OnItemViewClickListener?,
) : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var weatherData: List<Weather> = listOf()
    // TODO city
    private var cityData: List<City> = listOf()

    @SuppressLint("NotifyDataSetChanged")
//    fun setWeather(data: List<Weather>) {
    fun setWeather(data: List<City>) {
//        weatherData = data
        cityData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // TODO city
//        fun bind(weather: Weather) {
        fun bind(city: City) {
            itemView.apply {
//                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
//                    weather.city.city
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                    city.city
//                findViewById<ImageView>(R.id.favoriteIconMain).showIf { weather.city.favorite }
                findViewById<ImageView>(R.id.favoriteIconMain).showIf { city.favorite }

                setOnClickListener {
//                    onItemViewClickListener?.onItemViewClick(weather)
                    onItemViewClickListener?.onItemViewClick(city)
                }
            }
        }
    }

    interface OnItemViewClickListener {
        // TODO city
//        fun onItemViewClick(weather: Weather)
        fun onItemViewClick(city: City)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_main_recycler_item,
                parent, false
            ) as View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // TODO city
//        holder.bind(weatherData[position])
        holder.bind(cityData[position])
    }

//    override fun getItemCount() = weatherData.size
    override fun getItemCount() = cityData.size
}