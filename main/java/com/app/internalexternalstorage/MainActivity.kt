package com.app.internalexternalstorage

import android.Manifest.permission.CAMERA
import android.Manifest.permission.MANAGE_MEDIA
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.app.internalexternalstorage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val multiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val grantedPermissions = mutableListOf<String>()
            val deniedPermissions = mutableListOf<String>()

            // Iterate over each permission and check if it is granted or denied
            for (permission in permissions.entries) {
                val permissionName = permission.key
                val isGranted = permission.value

                if (isGranted) {
                    grantedPermissions.add(permissionName)
                } else {
                    deniedPermissions.add(permissionName)
                }
            }

            // Handle the results
            if (deniedPermissions.isEmpty()) {
                // All permissions are granted
                Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                // Some permissions are denied
                Toast.makeText(
                    this, "Some permissions are denied: $deniedPermissions", Toast.LENGTH_SHORT).show()

                // Show a permission dialog for denied permissions
                for (permission in deniedPermissions) {
                    showPermissionDialog(permission)
                }
            }
        }

    private fun showPermissionDialog(permission: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Permission Required")
            setMessage("All permission.")
            setPositiveButton("Setting",DialogInterface.OnClickListener { dialog, which ->
                val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri:Uri=Uri.fromParts("package",packageName,null)
                intent.data=uri
                startActivity(intent)
            })
        }.show()

    }


    var cameraconract=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
        }else{
            showcamerapermissiondialog()
        }
    }
    var storagepermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
        }else{
          //  showstoragepermissiondialog()
        }
    }

   private fun showcamerapermissiondialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Permission Required")
            setMessage("This app needs access to your camera so you can take awesome pictures.")
            setPositiveButton("Setting",DialogInterface.OnClickListener { dialog, which ->
                val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri:Uri=Uri.fromParts("package",packageName,null)
                intent.data=uri
                startActivity(intent)
            })
        }.show()
    }


     private fun showstoragepermissiondialog() {
        AlertDialog.Builder(this).apply {
            setTitle("permisson Required")
            setMessage("Allow to access photo media and file on your device ")
            setPositiveButton("Setting", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)

            })
        }.show()
    }





    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCemeraPermission.setOnClickListener {
            cameraconract.launch(android.Manifest.permission.CAMERA)

        }
        binding.btnStoragePermission.setOnClickListener {
            storagepermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        }
        binding.btnMultiplePermission.setOnClickListener {
            multiplePermissions.launch(arrayOf(android.Manifest.permission.READ_CONTACTS,android.Manifest.permission.READ_SMS))

        }

    }
}