package com.app.internalexternalstorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.app.internalexternalstorage.databinding.ActivityInternalStorageBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class InternalStorageActivity : AppCompatActivity() {
    var filename = "dixit.txt"

    lateinit var binding: ActivityInternalStorageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInternalStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSaveFile.setOnClickListener {
            println("file directorynu:$filesDir")
            println("cache directory:$cacheDir")
            println("external file directory:${getExternalFilesDir(null)}")
            println("external cache directory:$externalCacheDir")
            println("external storage directory:${Environment.getExternalStorageDirectory()}")
            var message =binding.etMessage.text.toString().trim()
            writedata(message)
        }
        binding.btnReadData.setOnClickListener {
            readdata()
        }
    }

    private fun writedata(message:String) {
        try {
            var file=File(externalCacheDir,filename)
            var out=FileOutputStream(file)
            out.write(message.toByteArray())
            out.close()
            Toast.makeText(this, "message saved", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun readdata() {
        try {
            var file = File(externalCacheDir, filename)
            var inn = FileInputStream(file)
            var reader = InputStreamReader(inn)
            var buffer = BufferedReader(reader)
            var message = buffer.useLines {
                it.fold("") { some, text ->
                    "$some\n$text"

                }

            }
            Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}
