package com.example.chatapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.example.chatapp.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.util.*


class RegisterActivity : AppCompatActivity() {
    var CAMERA_IMAGE_REQ_CODE = 55
    var GALLERY_IMAGE_REQ_CODE = 44
    var CAMERA_PERMISSION_CODE = 11
    var GALLERY_PERMISSION_CODE =22
    var SELECTED_PHOTO_URI:Uri? = null
    lateinit var progressBar:ProgressDialog
    var ctx:Context = this@RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = ProgressDialog(this@RegisterActivity)
        imageBtn_register.setOnClickListener {
            selectImageFromGallery()
        }
        register_btn.setOnClickListener{
            val userName = userName_et_Register.text.toString()
            val userEmail = email_et_Register.text.toString()
            val Password = password_et_Register.text.toString()
            if(userName.isNotEmpty()){
                if(userEmail.isNotEmpty()){
                    if(Password.isNotEmpty()){
                        if(Password.length >5) {
                            if(SELECTED_PHOTO_URI !=null){
                                progressBar.show()
                              RegisetToAuthDb(userEmail,Password)
                //   Toast.makeText(this@RegisterActivity,"Unable to add some data to FireBase, Please see Log typing 'RegisterActivity' in search ",Toast.LENGTH_LONG).show()
                            }  else Toast.makeText(this@RegisterActivity,"Please select Image",Toast.LENGTH_SHORT).show()
                        } else password_et_Register.setError("Please fill out Password")
                    } else password_et_Register.setError("Please fill out Password")
                } else email_et_Register.setError("Please fill out your Email")

            } else userName_et_Register.setError("Please fill out Your Good Name")


        }
        logInLink_tv_Register.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

//    private fun getImage() {
//        val chooserBuillder = AlertDialog.Builder(this@RegisterActivity)
//        val selectorArray = arrayOf("Camera","Gallery")
//        chooserBuillder.create()
//        chooserBuillder.setCancelable(false)
//        chooserBuillder.setTitle("Select From")
//        chooserBuillder.setItems(selectorArray, object : DialogInterface.OnClickListener{
//            override fun onClick(p0: DialogInterface?, position: Int) {
//               if(selectorArray[position].equals("Camera")) {
//                   selectImageFromCamera()
//
//               } else if(selectorArray[position].equals("Gallery")) {
//
//                   selectImageFromGallery()
//               }
//            }
//        })
//        chooserBuillder.show()
//
//    }

    private fun selectImageFromGallery() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,GALLERY_IMAGE_REQ_CODE)
            return
        }
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_PERMISSION_CODE)
    }

    private fun selectImageFromCamera() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent,CAMERA_IMAGE_REQ_CODE)
            return
        }
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromCamera()
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromGallery()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_IMAGE_REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

//                    val uri = data?.data
//                    val input:InputStream? = this@RegisterActivity.contentResolver.openInputStream(uri)
//                    val bitmap = BitmapFactory.decodeStream(input)
                   // val urilink = bitmap as Uri
                    //     val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
//                    imageBtn_register.setImageBitmap(bitmap)
//
//                    SELECTED_PHOTO_URI = uri
                }
            }
            GALLERY_IMAGE_REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageuri = data!!.getData()
                    //     val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                    imageBtn_register.setImageURI(imageuri)
                    SELECTED_PHOTO_URI = imageuri
                }
            }
        }
    }

    private fun uploadeImageToDB()  {
        if(SELECTED_PHOTO_URI == null) return
        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/images/"+fileName)
        storageRef.putFile(SELECTED_PHOTO_URI!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Image uploaded to DB Successfully "+ it.metadata?.path)
                storageRef.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","Image Uri Downloaded Successfully "+ it.path)
                    saveUsertoDB(it.toString())
                }
                storageRef.downloadUrl.addOnFailureListener{
                    Log.d("RegisterActivity","Failded to Downloaded Image URL "+ it.message)
                }
            }
            .addOnFailureListener{
                progressBar.dismiss()
                Toast.makeText(this@RegisterActivity,it.message,Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
    }

    private fun saveUsertoDB(userImageUrl: String) {
       val userId = FirebaseAuth.getInstance().uid ?: ""
        val userName = userName_et_Register.text.toString()
        val userEmail:String = email_et_Register.text.toString()
        if(userId.isEmpty()) return
        val fireDbRef = FirebaseDatabase.getInstance().reference
    val user = Users(userId,userName,userEmail,userImageUrl)
        fireDbRef.child("Users").child(userId).setValue(user)
            .addOnSuccessListener {
                progressBar.dismiss()
                Log.d("RegisterActivity","User Data added to Db successfully!")
                Toast.makeText(this@RegisterActivity,"Registered Successfully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
            .addOnFailureListener {
                progressBar.dismiss()
                Log.d("RegisterActivity","Failed to add data to DB, Exception:"+it.message)
                Toast.makeText(this@RegisterActivity,it.message,Toast.LENGTH_SHORT).show()
            }
    }
    private fun RegisetToAuthDb(userEmail:String,password:String) {
        //Saving User to Db
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail,password)
            .addOnSuccessListener{
                Log.d("RegisterActivity", "User registerd successfully with Id: "+it.user.uid)
                uploadeImageToDB()
//                Toast.makeText(this@RegisterActivity,"Registered Successfully",Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                progressBar.dismiss()
//                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "User registeration failded :"+it.message)
                progressBar.dismiss()
                Toast.makeText(this@RegisterActivity,it.message,Toast.LENGTH_SHORT).show()
            }
    }
}
