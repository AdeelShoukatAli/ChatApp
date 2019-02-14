package com.example.chatapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_change_user_info.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class ChangeUserInfo : AppCompatActivity() {
    val ctx:Context = this@ChangeUserInfo
     val GALLERY_IMAGE_REQ_CODE:Int = 55
    var SELECTED_PHOTO_URI: Uri? = null
    val userId = FirebaseAuth.getInstance().uid
    val UserdbRef = FirebaseDatabase.getInstance().getReference("/Users/"+userId)
    var userEmail:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_info)
        val userAvailable = LoginActivity.UserLoggedIn()
        if(userAvailable) {
            val userName:String = userName_et_changeInfo.text.toString()
            fetchUser()
            changeInfo_btn.setOnClickListener{
                if(userName.isNotEmpty()) {
                    if(SELECTED_PHOTO_URI !=null) {
                        uploadImageToDb()
                    } else{Toast.makeText(ctx,"Please select Image",Toast.LENGTH_SHORT).show()}
                } else userName_et_changeInfo.setError("User name can not left be blank")
            }
            userImg.setOnClickListener{
                getImage()

            }
//            updatePassword("")
//            val uri:Uri?= null
//            uploadImageToDb(uri)

        }else {
            startActivity(Intent(this@ChangeUserInfo, LoginActivity::class.java))
        }

    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,GALLERY_IMAGE_REQ_CODE)
    }

    private fun fetchUser() {
        UserdbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                val user = dataSnapShot.getValue(Users::class.java)
                try {
                    Picasso.get().load(user!!.userImageeUrl).into(userImg)
                    userName_et_changeInfo.setText(user.userName)
                    userEmail = user.userEmail
                } catch (e:Exception){

                }

            }

        })
    }

    private fun uploadImageToDb() {
        if(SELECTED_PHOTO_URI == null) return
        val storageRef = FirebaseStorage.getInstance().getReference("/Images/"+userId)
        storageRef.putFile(SELECTED_PHOTO_URI!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    updateUser(it)
                }
                    .addOnFailureListener {
                        Toast.makeText(ctx,it.message,Toast.LENGTH_SHORT).show()
                    }
            }
    }

    private fun updateUser(ImageUri: Uri?) {
        val userName:String? = userName_et_changeInfo.text.toString()
      //  val userEmail:String = email_et_changeInfo.text.toString()
        val user = Users(userId!!,userName!!,userEmail!!,ImageUri.toString())
        UserdbRef.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(ctx,"User updated Successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(ctx,it.message,Toast.LENGTH_SHORT).show()
            }
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            GALLERY_IMAGE_REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageuri = data!!.getData()
                    //     val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                    userImg.setImageURI(imageuri)
                    SELECTED_PHOTO_URI = imageuri
                }
            }
        }
    }
}