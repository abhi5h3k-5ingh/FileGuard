package com.example.fileguard

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.*

class MainActivity : AppCompatActivity() {
    val cm=CryptoManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtEncFileName=findViewById<EditText>(R.id.txtEncFileName)
        var txtEncrypted=findViewById<TextView>(R.id.encryptedData)
        val btnView=findViewById<Button>(R.id.btnView)
        val btnEncrypt=findViewById<Button>(R.id.buttonEncrypt)
        val btnDigest=findViewById<Button>(R.id.buttonDigest)
        val txtDecFileName=findViewById<EditText>(R.id.txtDecFileName)
        val txtDecrypted=findViewById<TextView>(R.id.txtDec)
        val btnView1=findViewById<Button>(R.id.btnView1)
        val btnDecrypt=findViewById<Button>(R.id.buttonDecrypt)
        val btnVerify=findViewById<Button>(R.id.buttonVerify)


        btnView.setOnClickListener{
            val filename = txtEncFileName.text.toString()
            if(filename.toString()!=null && filename.toString().trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(filename)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

                var text: String? = null
                val stringBuilder: StringBuilder = StringBuilder()
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
                //Displaying data on EditText
                txtEncrypted.setText(null)
                txtEncrypted.setText(stringBuilder.toString()).toString()
            }else{
                Toast.makeText(applicationContext,"file name cannot be blank", Toast.LENGTH_LONG).show()
            }
        }
        btnEncrypt.setOnClickListener{
            // Read the file
            var filename = txtEncFileName.text.toString()
            val stringBuilder: StringBuilder = StringBuilder()
            if(filename.toString()!=null && filename.toString().trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(filename)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
//                //Displaying data on EditText
//                txtEncrypted.setText(null)
//                txtEncrypted.setText(stringBuilder.toString()).toString()
            }else{
                Toast.makeText(applicationContext,"file name cannot be blank", Toast.LENGTH_LONG).show()
            }

            //Encrypt the string
            val inp=stringBuilder.toString().encodeToByteArray()
            filename="output.txt"
            val file= File(filesDir, filename)
            if(!file.exists()){
                file.createNewFile()
            }
            val fos=FileOutputStream(file)
            cm.encrypt(inp, fos)
            txtEncrypted.text="File Encrypted Successfully saved as "+filename
        }

        btnView1.setOnClickListener{
            val filename = txtDecFileName.text.toString()
            if(filename.toString()!=null && filename.toString().trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(filename)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

                var text: String? = null
                val stringBuilder: StringBuilder = StringBuilder()
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
                //Displaying data on EditText
                txtDecrypted.setText(null)
                txtDecrypted.setText(stringBuilder.toString()).toString()
            }else{
                Toast.makeText(applicationContext,"file name cannot be blank", Toast.LENGTH_LONG).show()
            }
        }
        btnDecrypt.setOnClickListener{
            val filename = txtDecFileName.text.toString()
            val inputFile=File(filesDir, filename)
            val fis=FileInputStream(inputFile)
            val data:String=cm.decrypt(fis).decodeToString()

            // Save the file in plain_filename.text
            val file:String = "plaintext.txt"
//            val data:String = fileData.text.toString()
            val fileOutputStream:FileOutputStream
            try {
                fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
                fileOutputStream.write(data.toByteArray())
            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }catch (e: NumberFormatException){
                e.printStackTrace()
            }catch (e: IOException){
                e.printStackTrace()
            }catch (e: Exception){
                e.printStackTrace()
            }
            Toast.makeText(applicationContext,"data save",Toast.LENGTH_LONG).show()
            txtDecFileName.text.clear()
            txtDecrypted.setText("File Decrypted Successfully saved as "+file)
        }

        btnDigest.setOnClickListener{
            // Read the file
            var filename = txtEncFileName.text.toString()
            val stringBuilder: StringBuilder = StringBuilder()
            if(filename.toString()!=null && filename.toString().trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(filename)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
//                //Displaying data on EditText
//                txtEncrypted.setText(null)
//                txtEncrypted.setText(stringBuilder.toString()).toString()
            }else{
                Toast.makeText(applicationContext,"file name cannot be blank", Toast.LENGTH_LONG).show()
            }

            //Encrypt the string
            val inp=stringBuilder.toString().encodeToByteArray()
            filename="digest.txt"
            val file= File(filesDir, filename)
            if(!file.exists()){
                file.createNewFile()
            }
          //  val fos=FileOutputStream(file)
            val digest=cm.hash(inp)
            file.writeText(digest)
            txtEncrypted.text="File Digest Successfully Created and saved as "+filename
        }

        btnVerify.setOnClickListener{
            // Read the file
            var filename = "input.txt"
            val stringBuilder: StringBuilder = StringBuilder()
            if(filename.toString()!=null && filename.toString().trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(filename)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
//                //Displaying data on EditText
//                txtEncrypted.setText(null)
//                txtEncrypted.setText(stringBuilder.toString()).toString()
            }else{
                Toast.makeText(applicationContext,"file name cannot be blank", Toast.LENGTH_LONG).show()
            }

            //Encrypt the string
            val inp=stringBuilder.toString().encodeToByteArray()
//            filename="digest.txt"
//            val file= File(filesDir, filename)
//            if(!file.exists()){
//                file.createNewFile()
//            }
            //  val fos=FileOutputStream(file)
            val digestCalculated=cm.hash(inp)

            val file=File(filesDir, "digest.txt")
            val digestReceived=file.readText()
            if(digestCalculated == digestReceived)
                txtDecrypted.text="Verified"
            else
                txtDecrypted.text="Tempered"
        }
    }
}