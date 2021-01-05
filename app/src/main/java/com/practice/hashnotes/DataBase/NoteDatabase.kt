package com.practice.hashnotes.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practice.hashnotes.Model.Note

@Database(entities = [Note::class],version = Info.DB_VERSION,exportSchema = false)
abstract class NoteDatabase : RoomDatabase()
{
    abstract fun getDao(): DaoRefrence

}
