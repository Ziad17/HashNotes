package com.practice.hashnotes

import android.content.Context
import android.media.Image
import android.os.Debug
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practice.hashnotes.Model.Note
import java.util.ArrayList

class MainRecycleAdapter(val context: Context, _notes:List<Note>, val listner: OnCardViewClickListner): RecyclerView.Adapter<MainRecycleAdapter.CardViewHolder>() {


    private var notes:List<Note> =_notes
    //TODO: Change the way context is handled by App singleton

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(context).inflate(R.layout.note_card_item,parent,false))
    }
    public fun setNotes(_notes:List<Note>): Unit
    {
        notes=_notes
        notifyDataSetChanged()


    }

    override fun getItemCount(): Int= notes.size

    override fun onBindViewHolder( holder: CardViewHolder, position: Int) {

        val note=notes.get(position)
        holder.dateModified.text=note.dateModified
        holder.divider.visibility=if(note.Encrypted) VISIBLE else GONE
        holder.encrypted.visibility=if(note.Encrypted) VISIBLE else GONE
        holder.dateModified.text=note.dateModified ?: note.dateCreated!!
        holder.text.text=note.title
        holder.icon.setImageResource(R.mipmap.ic_launcher)
    }



    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateModified: TextView = itemView.findViewById(R.id.card_view_date_modified)
        val text: TextView=itemView.findViewById(R.id.card_view_text)
        val divider:View=itemView.findViewById(R.id.view_card_divider)
        val encrypted: ImageView=itemView.findViewById(R.id.card_view_encrypted)
        val icon: ImageView=itemView.findViewById(R.id.card_view_icon)
        init {
            itemView.setOnClickListener { view -> listner.onCardClick(notes.get(layoutPosition),view)}
        }
    }

}

public interface OnCardViewClickListner
{
    fun onCardClick(note: Note,v: View) : Unit
}