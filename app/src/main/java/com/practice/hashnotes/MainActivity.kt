package com.practice.hashnotes

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.icu.text.CaseMap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Note.NOTE
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anggrayudi.storage.file.createNewFileIfPossible
import com.anggrayudi.storage.media.MediaStoreCompat
import com.practice.hashnotes.DataBase.Info
import com.practice.hashnotes.DataBase.NoteDatabase
import com.practice.hashnotes.Model.Note
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.Permission
import java.util.jar.Manifest

//CRItiCAL FIXME:


//TODO: Move permissions to the File Manager
//TODO: replace the loading toast with progress bar
class MainActivity : AppCompatActivity(), OnCardViewClickListner, OnConfirmCreateNoteListener {
    lateinit var addNoteDialog: AddNoteDialog
    lateinit var db: NoteDatabase
    lateinit var adapter: MainRecycleAdapter
    lateinit var fileManager: FileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        initAdapter()
        initAddNoteDialog()
        initFloatingAction()
        initDB()
        initFileManager()
        test()


    }

    private fun initFileManager() {
        fileManager = FileManager(this, db)
    }

    private fun test() {
        // onConfirmCreateNote(Note(title="test",text = ""))

    }

    private fun initDB() {
        db = Room.databaseBuilder(this, NoteDatabase::class.java, Info.DB_NAME)
            .fallbackToDestructiveMigration().build()

    }

    private fun initAddNoteDialog() {
        addNoteDialog = AddNoteDialog(context = this, onConfirmCreateNoteListener = this)
    }

    private fun initFloatingAction() {
        floating_button.setOnClickListener { openUpAddNoteDialog() }
    }

    private fun openUpAddNoteDialog() {
        addNoteDialog.show()
    }


    private fun initAdapter() {
        main_recycle.layoutManager = LinearLayoutManager(this)
        adapter =
            MainRecycleAdapter(context = this, _notes = ArrayList<Note>(), listner = this)
        main_recycle.adapter = adapter
    }

    fun Toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onCardClick(note: Note, v: View) {
        openNoteCard(note)
    }

    private fun openNoteCard(note: Note) {



        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra(EditNoteActivity.NOTE_INTENT, fileManager.readExternalStorageFile(note))
        startActivity(intent)
    }


    private fun requestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
    }

    override fun onConfirmCreateNote(note: Note) {


        var success = false
        doAsync {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    if (fileManager.createExternalStorageFile(note)) {
                        success = true
                    }
                } catch (e: java.lang.Exception) {
                    Log.e("", "onConfirmCreateNote: ", e)
                }
            } else {
                requestPermission()
            }
            uiThread {
                if (success) {
                    Toast("Created Successfully")
                    updateAdapter()
                } else {
                    Toast("Failed")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateAdapter()


    }

    fun updateAdapter() {
        doAsync {
            Log.e(TAG, "updateAdapter: " + db.getDao().getAllNotes().size)
            adapter.setNotes(db.getDao().getAllNotes())
            uiThread {
                toast("Done")
            }

        }
    }




    val TAG = "MainActivity"


}