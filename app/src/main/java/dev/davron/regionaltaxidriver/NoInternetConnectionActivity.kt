package dev.davron.regionaltaxidriver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.davron.regionaltaxidriver.databinding.ActivityNoInternetConnectionBinding

class NoInternetConnectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoInternetConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}