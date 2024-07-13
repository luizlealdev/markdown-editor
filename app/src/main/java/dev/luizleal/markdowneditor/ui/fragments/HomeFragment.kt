package dev.luizleal.markdowneditor.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.leinardi.android.speeddial.SpeedDialActionItem
import dev.luizleal.markdowneditor.R
import dev.luizleal.markdowneditor.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setSpeedViewItem(getString(R.string.import_from_file), R.id.fab_import_from_file, R.drawable.ic_upload_file)
        setSpeedViewItem(getString(R.string.import_from_url), R.id.fab_import_from_link, R.drawable.ic_add_link)
        setSpeedViewItem(getString(R.string.new_markdown), R.id.fab_write_new, R.drawable.ic_text_increase)
    }

    private fun setSpeedViewItem(label: String, idRef: Int, iconRef: Int) {
        binding.speedviewNew.addActionItem(
            SpeedDialActionItem.Builder(idRef, iconRef)
                .setFabBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightPrimary))
                .setFabImageTintColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setLabel(label)
                .create()
        )
    }
}