package com.example.chatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Script
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password_reset.*

class PasswordResetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)
        if(!LoginActivity.UserLoggedIn()) {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        val oldPass = oldPass_et_passReset.text.toString()
        val newPass = newPass_et_PasswordReset.text.toString()
        reset_btn.setOnClickListener {
            try {
                FirebaseAuth.getInstance().confirmPasswordReset(oldPass,newPass)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            Toast.makeText(this@PasswordResetActivity,"PasswordReset successful",Toast.LENGTH_SHORT).show()
                            Log.d("PasswordReset","PasswordReset successfully")
                        }
                    }
                    .addOnFailureListener{
                        Toast.makeText(this@PasswordResetActivity,it.message,Toast.LENGTH_SHORT).show()
                    }
            }  catch (e:Exception) {
               println(e.message)
            }

        }
    }
}
