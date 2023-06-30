package com.app.internalexternalstorage

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.app.internalexternalstorage.databinding.ActivityImagePickerCemeraPickerBinding

class ImagePickerCemeraPickerActivity : AppCompatActivity() {
    //permission dene ke liye
    val gallerpermissioncontract=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            picimagefromgallery()

        }else{

        }
    }
    // ye code bina permission ke sath wala
   val galleryresult=registerForActivityResult(ActivityResultContracts.GetContent()){
        if (it!=null){
            binding.ivCemeraGallery.setImageURI(it)

        }
    }



    lateinit var binding: ActivityImagePickerCemeraPickerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerCemeraPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivCemeraGallery.setOnClickListener {
            //ye code permission ke sath wala he
         //  gallerpermissioncontract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            //ye code bina permisson ke sath wala he
           // galleryresult.launch("image/*")
            //ye line dialog opne karwa de ke liye
            showoptiondialog()
        }

    }

    private fun showoptiondialog() {
        AlertDialog.Builder(this)
            .setTitle("Select Image")
            .setItems(arrayOf("From Gallery","From Cemera"),DialogInterface.OnClickListener { dialog, i ->
                if (i==0){
                    //gallery open
                     galleryresult.launch("image/*")


                }else{
                    //cemera open
                    var intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent,200)


                }

            }).show()

    }

    private fun picimagefromgallery() {
        var intent = Intent()
        // ye line sidhi gallery open karne ke liye
        intent.action = Intent.ACTION_PICK
        intent.action=Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 100)

    }
    // ye function galler se photo apne application me lane ke liye he

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100){

            if (resultCode== RESULT_OK && data!=null){
                var imageuri=data.data
                binding.ivCemeraGallery.setImageURI(imageuri)
            }

        }else{
            if (requestCode==200){
                if (resultCode== RESULT_OK && data!=null){
                    var imageuri=data.data
                    binding.ivCemeraGallery.setImageURI(imageuri)
                  val imagebitmap=data?.extras?.get("data")as Bitmap
                    binding.ivCemeraGallery.setImageBitmap(imagebitmap)


                }


            }
        }

    }

}




