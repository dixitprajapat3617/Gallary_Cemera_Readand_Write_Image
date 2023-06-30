package com.app.internalexternalstorage

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.app.internalexternalstorage.databinding.ActivitySaveAndReadBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class SaveAndReadActivity : AppCompatActivity() {

    var imageuri:Uri?=null
    var gallerycontract=registerForActivityResult(ActivityResultContracts.GetContent()){

        if (it!=null){
            imageuri=it
            binding.ivImage.setImageURI(imageuri)
        }
    }
    var cemeracontract=registerForActivityResult(ActivityResultContracts.TakePicture()){

        if (it){
            binding.ivImage.setImageURI(imageuri)
        }
    }




    lateinit var binding: ActivitySaveAndReadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveAndReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageuri=CreateImageUri()
        binding.ivImage.setOnClickListener {
            ShowOptionDialog()
        }
        binding.btnSave.setOnClickListener {
            if (imageuri!=null){
                var bitmap=UriToBitmap(imageuri!!)
                bitmap?.let {
                    SaveImageToInternalStorage(this,it)

                }
            }
        }
        binding.btnGetSave.setOnClickListener {
            var file=File(filesDir,"image/*")
            var bitmap=FileToBitmap(file)
            bitmap?.let {
                binding.image1.setImageBitmap(it)
            }
        }

    }

    private fun FileToBitmap(file: File): Bitmap? {
        try {
            return BitmapFactory.decodeFile(file.path)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
        return null
    }


    private fun SaveImageToInternalStorage(context: Context,bitmap: Bitmap) {
        var filename="IMG_${System.currentTimeMillis()}.png"
        var directory=File(filesDir,"image")
        if (!directory.exists()){
            directory.mkdir()
        }
        try {
            var file=File(directory,filename)
            var fout=FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout)
            fout.close()
            Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show()

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun UriToBitmap(imageuri: Uri): Bitmap? {
        var inputstream:InputStream?=null
        try {
            inputstream=contentResolver.openInputStream(imageuri)
            return BitmapFactory.decodeStream(inputstream)

        }catch (e:IOException){
            e.printStackTrace()
        }finally {
            inputstream!!.close()
        }
        return null

    }


    private fun ShowOptionDialog() {
        AlertDialog.Builder(this)
        .setTitle("Image")
            .setItems(arrayOf("Take Photo","From Gallery"),DialogInterface.OnClickListener { dialog, which ->

                if (which==0){
                    //cemera open
                    cemeracontract.launch(imageuri)
                }else{
                    gallerycontract.launch("image/*")
                }
            }).show()


    }

    private fun CreateImageUri(): Uri? {

        var filename="${System.currentTimeMillis()}.png"
        var file=File(filesDir,filename)
        return FileProvider.getUriForFile(this,"com.app.internalexternalstorage.fileprovider",file)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100 &&resultCode== RESULT_OK){
            data?.let {
                binding.ivImage.setImageURI(it.data)
            }
        }
    }

}

