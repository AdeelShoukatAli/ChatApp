package com.example.chatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class LatestMessagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        loggedInCheck()
    }

    private fun loggedInCheck() {
        val userId = FirebaseAuth.getInstance().uid
        if(userId == null) {
            val intent = Intent(this@LatestMessagesActivity, RegisterActivity::class.java)
         //   intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.new_msg_menu -> {
                val intent = Intent(this@LatestMessagesActivity, NewMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.log_out_menu -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@LatestMessagesActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.changeInfoMenu -> {
                startActivity(Intent(this@LatestMessagesActivity, ChangeUserInfo::class.java))
            }
            R.id.reset_password -> {
                startActivity(Intent(this,PasswordResetActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_res,menu)
        return super.onCreateOptionsMenu(menu)
    }

}
