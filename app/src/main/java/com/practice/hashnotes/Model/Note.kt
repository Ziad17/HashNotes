package com.practice.hashnotes.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practice.hashnotes.Constants
import com.practice.hashnotes.DataBase.Info
import java.text.SimpleDateFormat
import java.util.*



//FIXME : Remove Null Assignments as it's for debugging
@Entity(tableName = Info.NOTE_TABLE_NAME)
data class Note(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = Info.NOTE_ID) val id:Int?=null,
                @ColumnInfo(name = Info.NOTE_TITLE)val title: String?,
                @ColumnInfo(name = Info.NOTE_TEXT)var text:String?,
                @ColumnInfo(name = Info.NOTE_TYPE)val type: String?="text/plain",
                @ColumnInfo(name = Info.NOTE_DATE_CREATED) val dateCreated: String? =SimpleDateFormat("dd-M-yyyy[hh:mm:ss]").format(Date()),
                @ColumnInfo(name = Info.NOTE_DATE_MODIFIED)var dateModified: String?=null,
                @ColumnInfo(name = Info.NOTE_ENCRYPTION_KEY)val EncryptionKey:String?=null,
                @ColumnInfo(name = Info.NOTE_ENCRYPTED)val Encrypted: Boolean= EncryptionKey != null,

                @ColumnInfo(name = Info.NOTE_FILE_LOCATION) val Location: String?=if(Encrypted)Constants.ENC_FILES_LOCATION else Constants.NORMAL_FILES_LOCATION)
    : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(type)
        parcel.writeString(dateCreated)
        parcel.writeString(dateModified)
        parcel.writeString(EncryptionKey)
        parcel.writeByte(if (Encrypted) 1 else 0)
        parcel.writeString(Location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }


}