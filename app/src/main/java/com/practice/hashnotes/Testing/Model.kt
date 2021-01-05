package com.practice.hashnotes.Testing

import android.os.Build
import androidx.annotation.RequiresApi
import com.practice.hashnotes.EncryptionManager

@RequiresApi(Build.VERSION_CODES.O)
    fun main()
    {

         var msg="hey\nthis is a test case for encryption\nplease hold"
        print(msg)
         println("")

         print(EncryptionManager.decrypt(EncryptionManager.encrypt(msg,"234567"),"234567"))

    }
