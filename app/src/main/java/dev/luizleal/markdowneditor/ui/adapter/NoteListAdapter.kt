package dev.luizleal.markdowneditor.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.luizleal.markdowneditor.databinding.NoteItemHolderBinding
import dev.luizleal.markdowneditor.model.Note
import dev.luizleal.markdowneditor.utils.CalendarUtils.Companion.getMouthName

class NoteListAdapter(): RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {
    private var items: List<Note> = ArrayList()

    fun setItems(items: List<Note>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteItemHolderBinding.inflate(inflater, parent, false)

        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class NoteViewHolder(binding: NoteItemHolderBinding): RecyclerView.ViewHolder(binding.root) {
        private var title = binding.textTitle
        private var date = binding.textDate

        fun bind(note: Note) {
            title.text = note.text.lineSequence().first()

            val mouthName = getMouthName(note.lastUpdateMonth)
            val formatedDate =  "${note.lastUpdateDay} $mouthName"
            date.text = formatedDate
        }
    }

}