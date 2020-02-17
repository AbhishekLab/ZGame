package com.zgame.zgame.permissionPractice

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.os.Parcelable
import android.util.Log.d
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zgame.zgame.R
import kotlinx.android.synthetic.main.permission_demo_activity.*
import java.io.File

class PermissionDemo : AppCompatActivity() {

    lateinit var mBluetoothAdapter: BluetoothAdapter
    private var uriArray = ArrayList<Parcelable>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_demo_activity)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        storage_permission.setOnClickListener {
            if (permissionGranted()) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                requestNewPermissions()
            }
        }

        bluetooth_open.setOnClickListener {
            if(bluetoothIsOpen()){
                Toast.makeText(this, "Bluetooth Enabled", Toast.LENGTH_SHORT).show()
            }else{
                openBluetooth()
            }
        }

        send_email.setOnClickListener {
            sendEmail()
        }

        pdf_picker.setOnClickListener {
            pdfPicker()
        }

        clear_data.setOnClickListener {
            deleteAppData(this)
            //(getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
        }
    }


    private fun permissionGranted(): Boolean {
        val storageReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val storageWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if(checkCallingOrSelfPermission(storageReadPermission) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(storageWritePermission)==PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestNewPermissions() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), 100 )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){

            100 -> checkAllowOrNot(permissions)

            102 -> {Toast.makeText(this,"PdfPicker",Toast.LENGTH_SHORT).show()}
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAllowOrNot(permissions: Array<out String>) {

        for(i in permissions.indices){
            val rotationPermission = shouldShowRequestPermissionRationale(permissions[i])
            if(!rotationPermission){
                Toast.makeText(this,"User Select Never ask again ",Toast.LENGTH_SHORT).show()
            }else{
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), 100 )
            }
        }
    }

    private fun bluetoothIsOpen(): Boolean {
        return mBluetoothAdapter.isEnabled
    }
    private fun openBluetooth() {
        mBluetoothAdapter.enable()
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("abhi.cs005@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "body text")
        val root = getExternalStorageDirectory()

        d("RootDirectory",root.absolutePath)
        val pathToMyAttachedFile = "aa.txt"
        val file = File(root, pathToMyAttachedFile)
        if (!file.exists()) {
            d("Filessdfsdf",file.absolutePath)
            return
        }
        val uri = Uri.fromFile(file)
        uriArray.add(uri)
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArray)
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"))
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun pdfPicker() {
        val openIntent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            openIntent.action = (Intent.ACTION_GET_CONTENT)
            openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        openIntent.type = "application/pdf"
        openIntent.action = Intent.ACTION_GET_CONTENT
        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(openIntent, 102)
    }


    private fun deleteAppData(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
        }

    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children!!.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        } else return if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }

    }
}