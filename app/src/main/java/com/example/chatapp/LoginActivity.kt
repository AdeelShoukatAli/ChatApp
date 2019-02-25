package com.example.chatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        fun UserLoggedIn() :Boolean {
            val user = FirebaseAuth.getInstance().uid
            if(user!!.isNotEmpty()) {
                return true
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signin_btn.setOnClickListener{
            var userEmail = email_et_Login.text.toString()
            var password = password_et_Login.text.toString()
            signinUser(userEmail,password)
        }
        logInLink_tv_Register.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        logInLink_tv_forgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity,PasswordResetActivity::class.java))
        }
    }


    private fun signinUser(userEmail: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,password)
            .addOnCompleteListener{
                if(!it.isSuccessful) {
                    Log.d("LoginActivity","Logged in failure")
                    return@addOnCompleteListener}
                Toast.makeText(this@LoginActivity,"Logged in",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, LatestMessagesActivity::class.java)
                startActivity(intent)
                this.finish()
                Log.d("LoginActivity","User ${userEmail} logged in")
            }
            .addOnFailureListener{
                Log.d("LoginActivity",it.message)
                Toast.makeText(this@LoginActivity,it.message,Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }

            }


    }

