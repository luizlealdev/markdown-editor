package dev.luizleal.markdowneditor.ui.fragments

import android.content.res.Resources
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import dev.luizleal.markdowneditor.R
import dev.luizleal.markdowneditor.databinding.FragmentEditBinding
import dev.luizleal.markdowneditor.model.Note
import dev.luizleal.markdowneditor.ui.view.MainActivity
import dev.luizleal.markdowneditor.ui.viewmodel.NoteViewModel
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import org.commonmark.node.Paragraph
import java.util.Calendar

class EditFragment : Fragment(R.layout.fragment_edit) {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NoteViewModel

    private lateinit var markwon: Markwon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupMenu()
        setTextInMarkdownEditText()
        setEditTextOccupationScreen()
    }

    override fun onStart() {
        super.onStart()

        markwon = Markwon.builder(requireContext())
            .usePlugin(ImagesPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    builder.bulletWidth(9)
                    builder.blockQuoteColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.textColorSecondary
                        )
                    )
                }

                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    builder.setFactory(Paragraph::class.java) { _, _ ->
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.textMarkdownBaseColor
                            )
                        )
                    }
                }
            })
            .usePlugin(TablePlugin.create(requireContext()))
            .usePlugin(TaskListPlugin.create(requireContext()))
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setTextInMarkdownEditText() {
        val markdown = """ 
            # Markdown title
            
            **text bold**, _text italic_ 
            
            ## This is a list
            - item 1
            - item 2
            - item 3
            
            ## This is a code block
            ```kotlin
            fun main() {
                println("Hello World!")
            }
            ```
            """.trimIndent()

        binding.editNote.setText(markdown)
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

                    getString(R.string.more) -> {
                        showMoreActionsPopupMenu(
                            requireActivity().findViewById(R.id.more_actions)
                        )
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setEditTextOccupationScreen() {
        val windowHeight = Resources.getSystem().displayMetrics.heightPixels
        binding.editNote.minHeight = windowHeight - android.R.attr.actionBarSize
    }

    private fun setEditorToggleMode(isEditing: Boolean) {
        val editNote = binding.editNote
        val textMarkdown = binding.textMarkdown

        if (isEditing) {

            editNote.visibility = View.VISIBLE
            textMarkdown.visibility = View.GONE

        } else {

            editNote.visibility = View.GONE
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
                    builder.blockQuoteColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.textColorSecondary
                        )
                    )
                }

                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    builder.setFactory(Paragraph::class.java) { _, _ ->
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.textMarkdownBaseColor
                            )
                        )
                    }
                }
            })
            .usePlugin(TablePlugin.create(requireContext()))
            .usePlugin(TaskListPlugin.create(requireContext()))
            .build()


        val markdown = binding.editNote.text.toString().trimIndent()
        markwon.setMarkdown(binding.textMarkdown, markdown)
    }

    private fun showMoreActionsPopupMenu(parent: View) {
        val popupMenu = PopupMenu(requireContext(), parent)
        popupMenu.menuInflater.inflate(R.menu.more_action_items, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    saveNote(binding.editNote.text.toString())
                    true
                }

                else -> {
                    false
                }
            }
        }
        popupMenu.show()
    }

    private fun saveNote(text: String) {
        val currentDate = Calendar.getInstance()

        viewModel.insertNote(
            Note(
                text = text,
                lastUpdateDay = currentDate.get(Calendar.DAY_OF_MONTH),
                lastUpdateMonth = currentDate.get(Calendar.MONTH)
            )
        )
    }
}