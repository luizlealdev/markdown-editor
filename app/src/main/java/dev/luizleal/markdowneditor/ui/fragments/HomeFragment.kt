package dev.luizleal.markdowneditor.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.leinardi.android.speeddial.SpeedDialActionItem
import dev.luizleal.markdowneditor.R
import dev.luizleal.markdowneditor.databinding.FragmentHomeBinding
import dev.luizleal.markdowneditor.model.Note
import dev.luizleal.markdowneditor.ui.adapter.NoteListAdapter
import dev.luizleal.markdowneditor.ui.view.MainActivity
import dev.luizleal.markdowneditor.ui.viewmodel.NoteViewModel
import dev.luizleal.markdowneditor.utils.CommonUtils.Companion.copyText
import dev.luizleal.markdowneditor.utils.CommonUtils.Companion.deleteNote
import dev.luizleal.markdowneditor.utils.CommonUtils.Companion.insertNote
import dev.luizleal.markdowneditor.utils.CommonUtils.Companion.searchNote
import dev.luizleal.markdowneditor.utils.CommonUtils.Companion.shareText
import dev.luizleal.markdowneditor.utils.StringUtils.Companion.isAValidUrl
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var noteListAdapter: NoteListAdapter

    private val readStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openFilePicker()
            } else {
                Log.e("Permission err", "Permission Denied")
            }
        }

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                readFile(it)
            }
        }

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
        setupRecyclerView()

        val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setupMenu()
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

        viewModel.allNotes.observe(this) { items ->
            noteListAdapter.setItems(items)
            binding.textCentralNotice.visibility = View.GONE

            if (items.isEmpty()) {
                binding.textCentralNotice.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.none_note_created_message)
                }

            }

            binding.progressLoadingNotes.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        noteListAdapter = NoteListAdapter({ note ->
            openEditFragment(note, true)
        }, { note, parent ->
            showMoreActionPopupMenu(note, parent)
        })
        binding.recyclerviewNotes.apply {
            adapter = noteListAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }
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

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu_actionbar, menu)

                val searchButton = menu.findItem(R.id.search_button)
                val searchView = searchButton.actionView as SearchView
                searchView.apply {
                    isSubmitButtonEnabled = false
                    maxWidth = Integer.MAX_VALUE
                }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let { query ->
                            searchNote(viewModel, viewLifecycleOwner, query) { searchedNotes ->
                                noteListAdapter.setItems(searchedNotes)

                                binding.textCentralNotice.visibility = View.GONE

                                if (searchedNotes.isEmpty()) {
                                    binding.textCentralNotice.apply {
                                        visibility = View.VISIBLE
                                        text = getString(R.string.none_note_found_message)
                                    }
                                }
                            }
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupSpeedViewItemClicked() {
        val speedDialView = binding.speedviewNew

        speedDialView.setOnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_write_new -> {
                    openEditFragment()
                }

                R.id.fab_import_from_link -> {
                    showAlertDialogForImport()
                }

                R.id.fab_import_from_file -> {
                    requestPermissionAndOpenFilePicker()
                }
            }
            speedDialView.close()
            true
        }
    }

    private fun showMoreActionPopupMenu(note: Note, parent: View) {
        val popupMenu = PopupMenu(requireContext(), parent)

        popupMenu.menuInflater.inflate(R.menu.menu_note_item_holder, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    try {

                        deleteNote(viewModel, note)

                        Snackbar.make(
                            binding.root,
                            getString(R.string.note_deleted_message),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } catch (e: IOException) {
                        Log.e("Error while deleting note in db", e.toString())
                    }
                    true
                }

                R.id.share -> {
                    shareText(
                        requireActivity(),
                        getString(R.string.share_using),
                        getString(R.string.share),
                        note.text
                    )

                    true
                }

                R.id.copy -> {
                    copyText(requireActivity(), note.text)

                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showAlertDialogForImport() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_import_from_url, null)
        val textfield = dialogView.findViewById<TextInputEditText>(R.id.textfield_url)

        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialogStyle)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.positive_button)) { _, _ ->

                fetchDataFromUrl(textfield.text.toString().trim())
            }
            .setNegativeButton(getString(R.string.negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()

    }

    private fun fetchDataFromUrl(url: String) {
        if (url.isAValidUrl()) {
            val webClient = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            webClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.import_from_url_error),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (response.isSuccessful) {

                            val rawData = response.body?.string()
                            saveNote(rawData ?: "")

                        } else {

                            Snackbar.make(
                                binding.root,
                                getString(R.string.import_from_url_error_serverless),
                                Snackbar.LENGTH_SHORT
                            ).show()

                        }
                    }
                }

            })
        } else {

            Snackbar.make(
                binding.root,
                getString(R.string.invalid_url),
                Snackbar.LENGTH_SHORT
            ).show()

        }
    }

    private fun requestPermissionAndOpenFilePicker() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openFilePicker()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                Log.e("Permission err", "Permission Denied")
                readStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            else -> readStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openFilePicker() {
        filePickerLauncher.launch("*/*")
    }

    private fun readFile(uri: Uri) {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val stringBuilder = StringBuilder()
        reader.forEachLine { line ->
            stringBuilder.append(line).append('\n')
        }
        val fileContent = stringBuilder.toString()
        saveNote(fileContent)
    }

    private fun openEditFragment(note: Note? = null, isEditing: Boolean = false) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditorFragment(note, isEditing)
        this.findNavController().navigate(action)
    }

    private fun saveNote(text: String) {
        val currentDate = Calendar.getInstance()
        val note = Note(
            id = null,
            text = text,
            lastUpdateDay = currentDate.get(Calendar.DAY_OF_MONTH),
            lastUpdateMonth = currentDate.get(Calendar.MONTH),
        )

        insertNote(viewModel, note)
    }
}