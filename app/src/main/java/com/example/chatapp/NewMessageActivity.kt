package com.example.chatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.chatapp.Adapters.NewMessag_recyle_adapter
import com.example.chatapp.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    companion object {
        var toUser:Users? = null
        var fromUser:Users? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"

     val  userData =ArrayList<Users>()
        val adapter = NewMessag_recyle_adapter(this@NewMessageActivity,userData,
            { view, position ->
                val user = userData[position]
                NewMessageActivity.toUser = user
                Log.d("UserObj ", fromUser?.userName)
                Log.d("UerObj", toUser?.userName)
            startActivity(Intent(this@NewMessageActivity,ChatLogActivity::class.java))
        })
        val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("Users").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
//                val user = dataSnapshot.getValue(Users::class.java)
//                Toast.makeText(ctx,user?.userName,Toast.LENGTH_SHORT).show()

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val userId = FirebaseAuth.getInstance().uid
                val user = dataSnapshot.getValue(Users::class.java)
                when(user?.userId) {
                    userId -> NewMessageActivity.fromUser = user
                    else -> userData.add(user!!)
                }
                Log.d("Users",user.toString())
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            } })

        recycleView_newMessageActivity.layoutManager = LinearLayoutManager(this)
        recycleView_newMessageActivity.adapter = adapter
        adapter.notifyDataSetChanged()

    }
}
