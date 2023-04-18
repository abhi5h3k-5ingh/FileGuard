package com.example.fileguard

import android.media.MediaCodec.MetricsConstants.MODE
import android.media.metrics.TranscodingSession
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.view.animation.Transformation
import androidx.annotation.RequiresApi
import java.io.*
import java.security.KeyStore
import java.security.MessageDigest
import java.util.Base64.getEncoder
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.random.Random

class CryptoManager{

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher
        get() = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setKeySize(KEY_SIZE * 8) // key size in bits
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream){
        val cipher = encryptCipher
        val iv = cipher.iv
        outputStream.use {
            it.write(iv)
            // write the payload in chunks to make sure to support larger data amounts (this would otherwise fail silently and result in corrupted data being read back)
            ////////////////////////////////////
            val inputStream = ByteArrayInputStream(bytes)
            val buffer = ByteArray(CHUNK_SIZE)
            while (inputStream.available() > CHUNK_SIZE) {
                inputStream.read(buffer)
                val ciphertextChunk = cipher.update(buffer)
                it.write(ciphertextChunk)
            }
            // the last chunk must be written using doFinal() because this takes the padding into account
            val remainingBytes = inputStream.readBytes()
            val lastChunk = cipher.doFinal(remainingBytes)
            it.write(lastChunk)
//            return lastChunk
            //////////////////////////////////
        }
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val iv = ByteArray(KEY_SIZE)
            it.read(iv)
            val cipher = getDecryptCipherForIv(iv)
            val outputStream = ByteArrayOutputStream()

            // read the payload in chunks to make sure to support larger data amounts (this would otherwise fail silently and result in corrupted data being read back)
            ////////////////////////////////////
            val buffer = ByteArray(CHUNK_SIZE)
            while (inputStream.available() > CHUNK_SIZE) {
                inputStream.read(buffer)
                val ciphertextChunk = cipher.update(buffer)
                outputStream.write(ciphertextChunk)
            }
            // the last chunk must be read using doFinal() because this takes the padding into account
            val remainingBytes = inputStream.readBytes()
            val lastChunk = cipher.doFinal(remainingBytes)
            outputStream.write(lastChunk)
            //////////////////////////////////
            outputStream.toByteArray()
        }
    }

    companion object {
        private const val CHUNK_SIZE = 1024 * 4 // bytes
        private const val KEY_SIZE = 16 // bytes
        private const val ALIAS = "my_alias"
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    fun hash(bytes :ByteArray): String {
   //     val bytes = s.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

}
//class CryptoManager {
//    private val keystore: KeyStore=KeyStore.getInstance("AndroidKeyStore").apply{
//        load(null)
//    }
//
//    private val encryptCipher=Cipher.getInstance(TRANSFORMATION).apply{
//        init(Cipher.ENCRYPT_MODE, getKey())
//    }
//
//    private fun getDecryptCipherForIv(iv: ByteArray): Cipher{
//        return Cipher.getInstance(TRANSFORMATION).apply {
//            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
//        }
//    }
//
//    private fun getKey(): SecretKey{
//        val existingKey=keystore.getEntry("secret1", null) as? KeyStore.SecretKeyEntry
//        return existingKey?.secretKey?:createKey()
//    }
//
//    private fun createKey(): SecretKey{
//        return KeyGenerator.getInstance(ALGORITHM).apply {
//            init(
//                KeyGenParameterSpec.Builder(
//                    "secret1",
//                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//                )
//                    .setBlockModes(MODE)
//                    .setEncryptionPaddings(PADDING)
//                    .setUserAuthenticationRequired(false)
//                    .setRandomizedEncryptionRequired(false)
//                    .build()
//            )
//        }.generateKey()
//    }
//
//
//    fun encrypt(input: ByteArray, outputStream: OutputStream): ByteArray{
//        val encryptedBytes=encryptCipher.doFinal(input)
//        outputStream.use {
//            it.write(encryptCipher.iv.size)
//            it.write(encryptCipher.iv)
//            it.write(encryptedBytes.size)
//            it.write(encryptedBytes)
//            Log.d("encrypt", "IV_size:" +encryptCipher.iv.size)
//            Log.d("encrypt", "IV: " + String(encryptCipher.iv, 0, 16))
//        }
//        return encryptedBytes
//    }
//
//    fun decrypt(inputStream: InputStream): ByteArray{
//        return inputStream.use {
//            val ivsize = it.read()
//            val iv = ByteArray(ivsize)
//            it.read(iv)
//            Log.d("decrypt", "IV_size: " + iv.size)
//
//            val encryptedBytesSize = it.read()
//            val encryptedBytes = ByteArray(encryptedBytesSize)
//            it.read(encryptedBytes)
//            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
//        }
//    }
//
//    companion object{
//        private const val ALGORITHM=KeyProperties.KEY_ALGORITHM_AES
//        private const val MODE=KeyProperties.BLOCK_MODE_CBC
//        private const val PADDING=KeyProperties.ENCRYPTION_PADDING_PKCS7
//        private const val TRANSFORMATION="$ALGORITHM/$MODE/$PADDING"
//
//    }
//
//
//}