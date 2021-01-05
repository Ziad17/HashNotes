package com.practice.hashnotes


import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionManager
{
    @RequiresApi(Build.VERSION_CODES.O)
    public fun encrypt(msg: String, pass: String): String
    {
        val secretKeySpec=SecretKeySpec(passwordPadding(pass).toByteArray(),Constants.ENC_ALGORITHM)
        val iv= ByteArray(16)
        val charArray=pass.toCharArray()
        for (i in 0 until charArray.size)
        {iv[i]=0.toByte()
            }

        val ivParameterSpec=IvParameterSpec(iv)
        val cipher=Cipher.getInstance(Constants.DEC_SCHEMA)
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec)
        val encryptedValue=cipher.doFinal(msg.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedValue)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(msg: String, pass: String): String
    {
        val secretKeySpec=SecretKeySpec(passwordPadding(pass).toByteArray(),Constants.DEC_ALGORITHM)
        val iv=ByteArray(16)
        val charArray=pass.toCharArray()
        for (i in 0 until charArray.size)
        {iv[i]=0.toByte()}
        val ivParameterSpec=IvParameterSpec(iv)
       val cipher=Cipher.getInstance(Constants.ENC_SCHEMA)
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec)
        val decryptedValue=cipher.doFinal(Base64.getDecoder().decode(msg))
        return String(decryptedValue)
    }



    fun passwordPadding( password:String)=password.padEnd(16,Constants.PADDING_STRING.toCharArray().first())








}