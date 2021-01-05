package com.practice.hashnotes

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Toast
import com.practice.hashnotes.Model.Note
import kotlinx.android.synthetic.main.add_note_dialog.*

class AddNoteDialog(context: Context, val onConfirmCreateNoteListener: OnConfirmCreateNoteListener) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note_dialog)
        init()
    }
    private fun init() {
        checkIfSDKallowsEncryption()

        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        add_note_confirm_button.setOnClickListener { checkAndSaveNote() }
        add_note_back_button.setOnClickListener { this.hide() }
        add_note_checkbox.setOnCheckedChangeListener { _, b ->
            if(b) {
                add_note_key_container.visibility=VISIBLE
            } else{add_note_key_container.visibility=GONE}
        }
    }

    private fun checkIfSDKallowsEncryption() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O)
        {
            add_note_checkbox.isEnabled=false
        }
    }

    private fun checkAndSaveNote() {
        if (add_note_title!!.text.isNotEmpty()) {
            onConfirmCreateNoteListener.onConfirmCreateNote(
                    Note(title = add_note_title.text.toString(),
                            text = "",
                            EncryptionKey = if(add_note_checkbox.isChecked)  add_note_key.text.toString() else null))
            add_note_title.setText("")
            add_note_checkbox.isChecked=false
            add_note_key.setText("")
            this.hide()

        } else add_note_title.error = "Please Enter A Title"
    }

}

interface OnConfirmCreateNoteListener {
    fun onConfirmCreateNote(note: Note)
}