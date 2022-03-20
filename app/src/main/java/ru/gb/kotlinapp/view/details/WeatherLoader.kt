package ru.gb.kotlinapp.view.details

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.gb.kotlinapp.BuildConfig
import ru.gb.kotlinapp.model.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors

private const val REQUEST_API_KEY = "X-Yandex-API-Key"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000

@RequiresApi(Build.VERSION_CODES.N)
class WeatherLoader(
    private val listener: WeatherLoaderListener,
    private val lat: Double,
    private val lon: Double
) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() {

        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}")

            val handler = Handler()

            Thread {
                lateinit var urlConnection: HttpURLConnection
                try {
                    urlConnection = (uri.openConnection() as HttpURLConnection).apply {
                        requestMethod = REQUEST_GET
                        readTimeout = REQUEST_TIMEOUT

                        addRequestProperty(
                            REQUEST_API_KEY,
                            BuildConfig.WEATHER_API_KEY
                        )
                    }


                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val response = getLines(bufferedReader)

                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(response, WeatherDTO::class.java)

                    handler.post {
                        listener.onLoaded(weatherDTO)
                    }

                } catch (e: Exception) {
                    Log.e("", "Failed connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()

        } catch (e: MalformedURLException) {
            Log.e("", "Failed URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }
}