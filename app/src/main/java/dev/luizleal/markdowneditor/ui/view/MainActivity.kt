package dev.luizleal.markdowneditor.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.leinardi.android.speeddial.SpeedDialActionItem
import dev.luizleal.markdowneditor.R
import dev.luizleal.markdowneditor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setSpeedViewItem("Import from file", R.id.fab_import_from_file, R.drawable.ic_upload_file)
        setSpeedViewItem("Import from URL", R.id.fab_import_from_link, R.drawable.ic_add_link)
        setSpeedViewItem("New Markdown", R.id.fab_write_new, R.drawable.ic_text_increase)
    }

    private fun setSpeedViewItem(label: String, idRef: Int, iconRef: Int) {
        binding.speedviewNew.addActionItem(
            SpeedDialActionItem.Builder(idRef, iconRef)
                .setFabBackgroundColor(ContextCompat.getColor(this, R.color.lightPrimary))
                .setFabImageTintColor(ContextCompat.getColor(this, R.color.white))
                .setLabel(label)
                .create()
        )
    }
}