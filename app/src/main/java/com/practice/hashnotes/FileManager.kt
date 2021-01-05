package com.practice.hashnotes

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.anggrayudi.storage.file.createNewFileIfPossible
import com.anggrayudi.storage.media.MediaStoreCompat
import com.practice.hashnotes.DataBase.NoteDatabase
import com.practice.hashnotes.Model.Note
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileManager(val context: Context,val db:NoteDatabase)
{
    fun createExternalStorageFile(note: Note): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return try {
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, note.title)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, note.type)
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS+note.Location)
                context.contentResolver.insert(MediaStore.Files.getContentUri("extrnal"), contentValues)
                db.getDao().createNote(note)
                true
            } catch (e: Exception) {
                Log.e("s", "createFile: ", e)
                false
            }

        } else {
            return try {
                val folderToCreate= File(Environment.getExternalStorageDirectory().path,note.Location)
                if(!folderToCreate.exists())
                {folderToCreate.mkdirs()}
                val fileToCreate: File = File(folderToCreate.path, File.separator+note.title!!.trim()+".txt")
                if(!fileToCreate.exists())
                {fileToCreate.createNewFileIfPossible()}
                val output: FileOutputStream = FileOutputStream(fileToCreate)
                output.write(note.text!!.toByteArray())
                output.close()
                db.getDao().createNote(note)
                true
            }
            catch (e: Exception)
            {
                Log.e(context.toString(), "createExternalStorageFile: ",e )
                false
            }
        }
    }

    fun updateExternalStorageFile(note: Note): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return try {
                val singleFile =
                    MediaStoreCompat.fromRelativePath(context, note.Location + note.title + ".txt").first()
                val stream = singleFile.openOutputStream(append = false)
                stream!!.write(note.text?.toByteArray())
                stream.close()
                db.getDao().updateNote(note)

                true
            } catch (e: Exception) {
                Log.e("s", "updateFile: ", e)
                false
            }
        } else {
            return try {
                val fileToUpdate: File = File(Environment.getExternalStorageDirectory().path+note.Location,note.title!!.trim()+".txt")
                val output: FileOutputStream= FileOutputStream(fileToUpdate)
                output.bufferedWriter().use { bufferedWriter ->bufferedWriter.write(note.text)  }
            //    output.write(note.text!!.toByteArray())
                output.close()
                db.getDao().updateNote(note)
                true
            }
            catch (e: Exception)
            {
                Log.e(context.toString(), "updateExternalStorageFile: ",e )
                false
            }
        }
    }

    fun readExternalStorageFile(note: Note): Note? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return try {
                val singleFile =
                    MediaStoreCompat.fromRelativePath(context, note.Location + note.title + ".txt")
                        .first()
                val stream = singleFile.openInputStream()!!
                note.text = stream.bufferedReader().use { buffer -> buffer.readText() }
                stream.close()
                note
            } catch (e: Exception) {
                Log.e("s", "updateFile: ", e)
                null
            }
        }
        else {
            return try {
                val fileToRead: File = File(Environment.getExternalStorageDirectory().path+note.Location,note.title+".txt")
                val inputStream: FileInputStream = FileInputStream(fileToRead)
                note.text = inputStream.bufferedReader().use { buffer -> buffer.readText() }
                inputStream.close()
                note
            }
            catch (e: Exception)
            {
                Log.e(context.toString(), "readExternalStorageFile: ",e )
                null
            }
        }
    }
}