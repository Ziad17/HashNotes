package com.practice.hashnotes

import android.app.ActionBar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.practice.hashnotes.DataBase.Info
import com.practice.hashnotes.DataBase.NoteDatabase
import com.practice.hashnotes.Model.Note
import kotlinx.android.synthetic.main.edit_note_activity.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class EditNoteActivity : AppCompatActivity() ,
    OnConfirmAccessEncryptedListener{
    lateinit var note: Note
    lateinit var db: NoteDatabase
    lateinit var fileManager: FileManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note_activity)
        init()
    }

    private fun init() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        bindData(intent.getParcelableExtra<Note>(NOTE_INTENT)!!)
        initDB()
        initFileManager()
        enableSaving()
    }

    private fun initDB() {
        db = Room.databaseBuilder(this, NoteDatabase::class.java, Info.DB_NAME).build()
    }

    private fun initFileManager() {
        fileManager = FileManager(this, db)
    }

    private fun bindData(_note: Note) {
        note = _note

        if (note.Encrypted) {
            AccessEncryptedDialog(
                context = this,
                onConfirmAccessEncryptedListener = this,
                noteToOpen = note,
                key = note.EncryptionKey
            ).show()
            return
        }
        edit_note_text.setText(note.text)
        supportActionBar?.title = note.title
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun enableSaving() {
        save_button.setOnClickListener {
            saveNote(edit_note_text.text.toString())
            toast("hey")
        }
    }


    fun saveNote(_text: String) {
        var success = false
        if(note.Encrypted)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                note.text=EncryptionManager.encrypt(_text,note.EncryptionKey!!)
            }

        }
        else{
            note.text = _text
        }
        note.dateModified = SimpleDateFormat("dd-M-yyyy[hh:mm:ss]").format(Date())
        doAsync {
            if (fileManager.updateExternalStorageFile(note)) success = true



            uiThread {
                if (success) {
                    toast("Updated Successfully")
                    finish()

                } else {
                    toast("Failed")
                }
            }
        }


    }

    override fun onConfirmAccessEncrypted(_note: Note, key: String) {
        note = _note
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            edit_note_text.setText(EncryptionManager.decrypt(note.text!!,note.EncryptionKey!!))
        }
        supportActionBar?.title = note.title
    }

    override fun onCancelAccess() {
        finish()
    }


    companion object {
        const val NOTE_INTENT: String = "NOTE"
    }
}