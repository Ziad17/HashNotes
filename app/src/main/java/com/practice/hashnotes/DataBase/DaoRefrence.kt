package com.practice.hashnotes.DataBase

import androidx.room.*
import com.practice.hashnotes.Model.Note


@Dao
interface DaoRefrence {
    @Insert
    fun createNote(vararg note: Note)

    @Delete
    fun removeNote(vararg note: Note)

    @Update
    fun updateNote(note: Note)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE NOTE_ID=:noteId")
    fun getNoteById(noteId: Int): Note
}