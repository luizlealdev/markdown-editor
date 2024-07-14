package dev.luizleal.markdowneditor.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.luizleal.markdowneditor.MyApplication
import dev.luizleal.markdowneditor.databinding.ActivityMainBinding
import dev.luizleal.markdowneditor.ui.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val viewModel: NoteViewModel by viewModels {
        NoteViewModel.NoteViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}