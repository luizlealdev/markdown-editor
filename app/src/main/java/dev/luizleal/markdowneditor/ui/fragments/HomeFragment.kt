package dev.luizleal.markdowneditor.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.leinardi.android.speeddial.SpeedDialActionItem
import dev.luizleal.markdowneditor.R
import dev.luizleal.markdowneditor.databinding.FragmentHomeBinding
import dev.luizleal.markdowneditor.ui.view.MainActivity
import dev.luizleal.markdowneditor.ui.viewmodel.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onStart() {
        super.onStart()

        setSpeedViewItem(
            getString(R.string.import_from_file),
            R.id.fab_import_from_file,
            R.drawable.ic_upload_file
        )
        setSpeedViewItem(
            getString(R.string.import_from_url),
            R.id.fab_import_from_link,
            R.drawable.ic_add_link
        )
        setSpeedViewItem(
            getString(R.string.new_markdown),
            R.id.fab_write_new,
            R.drawable.ic_text_increase
        )

        setupSpeedViewItemClicked()
    }

    private fun setSpeedViewItem(label: String, idRef: Int, iconRef: Int) {
        binding.speedviewNew.addActionItem(
            SpeedDialActionItem.Builder(idRef, iconRef)
                .setFabBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightPrimary
                    )
                )
                .setFabImageTintColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setLabel(label)
                .create()
        )
    }

    private fun setupSpeedViewItemClicked() {
        binding.speedviewNew.setOnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_write_new -> {
                    this.findNavController().navigate(R.id.action_homeFragment_to_editorFragment)
                    true
                }

                R.id.fab_import_from_link -> {
                    showAlertDialogForImport()
                    true
                }
//                R.id.fab_import_from_file -> {
//
//                }
                else -> false
            }
        }
    }

    private fun showAlertDialogForImport() {
        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_import_from_url)
            .setPositiveButton(getString(R.string.positive_button)) { _, _ ->

            }
            .setNegativeButton(getString(R.string.negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()

    }
}