package dev.luizleal.markdowneditor.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import dev.luizleal.markdowneditor.R
import dev.luizleal.markdowneditor.databinding.FragmentEditBinding
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin

class EditFragment : Fragment(R.layout.fragment_edit) {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()

        setupMenu()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actionbar_edit_items, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.title) {

                    getString(R.string.preview) -> {
                        setEditorToggleMode(false)
                        setMarkdown()

                        menuItem.apply {
                            title = getString(R.string.editor)
                            setIcon(R.drawable.ic_edit)
                        }
                        true
                    }

                    getString(R.string.editor) -> {
                        setEditorToggleMode(true)
                        menuItem.apply {
                            title = getString(R.string.preview)
                            setIcon(R.drawable.ic_note)
                        }
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setEditorToggleMode(isEditing: Boolean) {
        val editNote = binding.editNote
        val textMarkdown = binding.textMarkdown

        if (isEditing) {

            editNote.visibility = View.VISIBLE
            textMarkdown.visibility = View.INVISIBLE

        } else {

            editNote.visibility = View.INVISIBLE
            textMarkdown.visibility = View.VISIBLE

        }
    }

    private fun setMarkdown() {

        val markwon = Markwon.builder(requireContext())
            .usePlugin(ImagesPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    builder.bulletWidth(9)
                }
            })
            .usePlugin(TablePlugin.create(requireContext()))
            .usePlugin(TaskListPlugin.create(requireContext()))
            .build()

        val markdown = binding.editNote.text.toString().trimIndent()
        markwon.setMarkdown(binding.textMarkdown, markdown)
    }
}