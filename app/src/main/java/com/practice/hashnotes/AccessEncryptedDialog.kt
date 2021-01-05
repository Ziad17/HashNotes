package com.practice.hashnotes

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.practice.hashnotes.Model.Note
import kotlinx.android.synthetic.main.access_encrypted_dialog.*
import kotlinx.android.synthetic.main.add_note_dialog.*

class AccessEncryptedDialog(context: Context, val onConfirmAccessEncryptedListener: OnConfirmAccessEncryptedListener,val key:String?,val noteToOpen: Note) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.access_encrypted_dialog)
        init()
    }
    private fun init() {


        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        access_encrypted_confirm_button.setOnClickListener { checkKey() }
        access_encrypted_back_button.setOnClickListener {
            onConfirmAccessEncryptedListener.onCancelAccess()
            this.hide()
        }
    }

    private fun checkKey() {
        if (access_encrypted_key!!.text.isNotEmpty()) {
            if(access_encrypted_key!!.text.toString() == key) {
                onConfirmAccessEncryptedListener.onConfirmAccessEncrypted(
                    noteToOpen,
                    access_encrypted_key.text.toString()
                )
                access_encrypted_key.setText("")
                this.hide()
            }
            else access_encrypted_key.error = "The Key Is Wrong"

        } else access_encrypted_key.error = "Please Enter A Key"
    }

    override fun onBackPressed() {
        onConfirmAccessEncryptedListener.onCancelAccess()
        super.onBackPressed()
    }


    override fun onDetachedFromWindow() {
        onConfirmAccessEncryptedListener.onCancelAccess()
        super.onDetachedFromWindow()

    }




}

interface OnConfirmAccessEncryptedListener {
    fun onConfirmAccessEncrypted(note: Note,key: String)
    fun onCancelAccess()
}