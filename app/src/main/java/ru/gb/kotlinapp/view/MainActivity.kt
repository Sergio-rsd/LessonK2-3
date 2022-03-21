package ru.gb.kotlinapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        savedInstanceState ?: run{
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}