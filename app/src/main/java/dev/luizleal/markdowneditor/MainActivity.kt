package dev.luizleal.markdowneditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.luizleal.markdowneditor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
    }
}