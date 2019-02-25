package com.example.chatapp

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.chatapp.Adapters.LatestMessageAdapter
import com.example.chatapp.Models.ChatMessage
import com.example.chatapp.Models.LatestMsgUser
import com.example.chatapp.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : AppCompatActivity() {
    lateinit var adapter: LatestMessageAdapter
    lateinit var latestMessagesData: ArrayList<LatestMsgUser>
    lateinit var userData: ArrayList<Users>
    val latestMsgsHashMap = HashMap<String, LatestMsgUser>()
    lateinit var clickedUser: (it: View, position: Int) -> Unit
    lateinit var longClick: (it: View, position: Int) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        loggedInCheck()
        latestMessagesData = ArrayList()
        userData = ArrayList()
        clickedUser = { it, position ->
             val clickedUser = latestMessagesData[position].userID
            getUserbyId(clickedUser)

        }
        longClick = { it, position ->
            confirmDialogView(position)

        }
        adapter = LatestMessageAdapter(this@LatestMessagesActivity, latestMessagesData, clickedUser, longClick)
        recyclerview_latestMsg.layoutManager = LinearLayoutManager(this@LatestMessagesActivity)
        recyclerview_latestMsg.adapter = adapter
        fetchLatestMessages()
        adapter.notifyDataSetChanged()
    }

    private fun confirmDialogView(position:Int) {
        val dialogBuilder = AlertDialog.Builder(this@LatestMessagesActivity)
        val create = dialogBuilder.create()
        dialogBuilder.setTitle("Delete chat")
        dialogBuilder.setMessage("Are you sure want to permanently remove chat history?")
        dialogBuilder.setNegativeButton("No",object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                create.dismiss()
            }

        })
        dialogBuilder.setPositiveButton("Yes", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                deletechatData(position)

            }

        })

    }

    private fun deletechatData(position: Int) {

    }


    private fun refreshLatestMsgs() {
        latestMessagesData.clear()
        latestMsgsHashMap.values.forEach {
            latestMessagesData.add(it)
            adapter.notifyDataSetChanged()
            Log.d("HashMaps", it.toString())
        }

    }

    private fun fetchLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val dbRef = FirebaseDatabase.getInstance().getReference("/latest_msgs/$fromId")
        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                val latestMsg = dataSnapshot.getValue(ChatMessage::class.java) ?: return
                updateLatestMsgsRecylerView(latestMsg)
                adapter.notifyDataSetChanged()
//                latestMsgsHashMap[dataSnapshot.key!!] = latestMsg
                //             refreshLatestMsgs()
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val latestMsg = dataSnapshot.getValue(ChatMessage::class.java) ?: return
                updateLatestMsgsRecylerView(latestMsg)
                adapter.notifyDataSetChanged()
//                putUsersDetail(latestMsg)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun updateLatestMsgsRecylerView(latestMsg: ChatMessage) {
        val fromId = FirebaseAuth.getInstance().uid
        var chatPartnerId: String = ""
        if (latestMsg.fromId == fromId) {
            chatPartnerId = latestMsg.toId
        } else {
            chatPartnerId = latestMsg.fromId
        }
        val userdbRef = FirebaseDatabase.getInstance().getReference("Users/$chatPartnerId")
        userdbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(Users::class.java) ?: return
                val userId = FirebaseAuth.getInstance().uid
                if (userId == user.userId) {
                    NewMessageActivity.fromUser = user
                }
                val latestMsgs = LatestMsgUser(user.userId, user.userName, latestMsg.msg, user.userImageeUrl)
                latestMsgsHashMap[user.userId] = latestMsgs
                refreshLatestMsgs()
                adapter.notifyDataSetChanged()
            }
        })
    }

//    private fun putUsersDetail(msgData:ChatMessage) {
//        val userList:ArrayList<String> = ArrayList()
//        val fromId = FirebaseAuth.getInstance().uid
//        var chatPartnerId:String = ""
//        if(msgData.fromId == fromId) {
//            chatPartnerId = msgData.toId
//            if(userList.equals(chatPartnerId)) {
//                return
//            } else {
//                userList.add(chatPartnerId)
//            }
//        }
//        else {
//            chatPartnerId = msgData.fromId
//            if(userList.equals(chatPartnerId)) {
//
//            } else {
//                userList.add(chatPartnerId)
//            }
//        }
//        val usersIds = userList.distinct()
//        Log.d("Id Added",chatPartnerId)
//        getUsersbyId(usersIds)
//    }

    private fun getUserbyId(userId: String) {
        val userdbRef = FirebaseDatabase.getInstance().getReference("Users/$userId")
        userdbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                val user = dataSnapShot.getValue(Users::class.java)
                NewMessageActivity.toUser = user
                startActivity(Intent(this@LatestMessagesActivity, ChatLogActivity::class.java))
            }

        })

    }


//        val currentUser = FirebaseAuth.getInstance().uid
//        val dbRef = FirebaseDatabase.getInstance().getReference("Users/$currentUser")
//        dbRef.addListenerForSingleValueEvent(object :ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//               NewMessageActivity.fromUser = p0.getValue(Users::class.java)
//            }
//
//        })
//    }

    private fun loggedInCheck() {
        val userId = FirebaseAuth.getInstance().uid
        if(userId == null) {
         //   intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(Intent(this@LatestMessagesActivity, RegisterActivity::class.java))
            return
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.new_msg_menu -> {
                val intent = Intent(this@LatestMessagesActivity, NewMessageActivity::class.java)
              //  intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
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
