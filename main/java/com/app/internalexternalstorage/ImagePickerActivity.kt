package com.app.internalexternalstorage

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.app.internalexternalstorage.databinding.ActivityImagePickerBinding
import java.io.File

class ImagePickerActivity : AppCompatActivity() {
        lateinit var binding: ActivityImagePickerBinding

        var imageuri:Uri?=null


        var storegepermissioncontract=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                picimagefromgallery()
            }else{
                Toast.makeText(this, "Allow permission", Toast.LENGTH_SHORT).show()
            }
        }

        var cemeracontract=registerForActivityResult(ActivityResultContracts.TakePicture()){
            if (it){
                binding.ivImage.setImageURI(imageuri)

            }
        }


    private fun picimagefromgallery() {
        var intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,300)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100&& resultCode== RESULT_OK){
            data?.let {
                binding.ivImage.setImageURI(it.data)
            }
        }
    }

    var gallerycontract=registerForActivityResult(ActivityResultContracts.GetContent()){

            if (it!=null){
                binding.ivImage.setImageURI(it)
            }
        }




        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
          // imageuri=createimageuri()


            binding.ivImage.setOnClickListener {
                showoptiondialog()


               // gallerycontract.launch("image/*")

            }


        }

 /*   private fun createimageuri(): Uri? {
        var filename="${System.currentTimeMillis()}.png"
        var file=File(filesDir,filename)
        return FileProvider.getUriForFile(this,"package = com.app.internalexternalstorage.Fileprovide",file)

    }*/


    private fun showoptiondialog() {
        AlertDialog.Builder(this)
            .setTitle("Image")
            .setItems(arrayOf("Take Photo","From Gallery"),DialogInterface.OnClickListener { dialog, i ->
                if (i==0){
                    //cemera
                    cemeracontract.launch(imageuri)

                }else{
                     gallerycontract.launch("image/*")

                }

            }).show()
    }





}