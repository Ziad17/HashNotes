package com.practice.hashnotes

import android.app.Application
import android.view.View

class App : Application() {



     companion object{

     }

     fun View.Toast(msg:String)
     {
         android.widget.Toast.makeText(applicationContext,msg, android.widget.Toast.LENGTH_SHORT).show()
     }
    fun View.longToast(msg:String)
    {
        android.widget.Toast.makeText(applicationContext,msg, android.widget.Toast.LENGTH_LONG).show()
    }


}