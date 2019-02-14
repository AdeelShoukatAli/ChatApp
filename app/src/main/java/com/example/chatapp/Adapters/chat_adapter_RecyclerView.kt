package com.example.chatapp.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.chatapp.Models.ChatMessage
import com.example.chatapp.NewMessageActivity
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.StringBuilder

class chat_adapter_RecyclerView(val ctx: Context, var chatData: ArrayList<ChatMessage>) :
    RecyclerView.Adapter<chat_adapter_RecyclerView.customView>(){
  var  fromId = FirebaseAuth.getInstance().uid
    override fun getItemViewType(position: Int): Int {
        when (chatData[position].fromId) {
            fromId -> return 0
        }
        return 1
    }
//        if(chatData[position].fromId==fromId){
//
//            return 0
//        }
//        chatData.forEach {
//            when(it.fromId) {
//                fromId -> return 0
//            }
//        }




    override fun onCreateViewHolder(p0: ViewGroup, position: Int): customView {
        val fromMsgView = LayoutInflater.from(ctx).inflate(R.layout.row_from_msg, null)
        val toMessageview = LayoutInflater.from(ctx).inflate(R.layout.row_too_msg, null)
        when(position) {
            0 -> return customView(fromMsgView)
            else -> return customView(toMessageview)
        }

//        chatData.forEach{
//            if(it.fromId == fromId)
//            { Log.d("fromIdCheck","db ID : ${it.fromId},Logged user ID : ${fromId} ")
//                return customView(fromMsgView)}
//        }


//        chatData.forEach {
////            if (it.fromId == fromId) {
////
////                customView(fromMsgView)
////            } else {
////
////                return customView(toMessageview)
////            }
//        }
//return customView(toMessageview)
    }

    override fun getItemCount(): Int {
        return chatData.size
    }

    override fun onBindViewHolder(holder: customView, position: Int) {
        // setting image & msgs which sent by logged in user
        holder.fromMsgTv?.text = chatData[position].msg
        try {
            Picasso.get().load(NewMessageActivity.fromUser?.userImageeUrl).into(holder.fromUserImg!!)
        } catch (e:NullPointerException) {
            print(e.message)
        }

        // setting image & msgs which sent by other user
        holder.toMsgTv?.text = chatData[position].msg
        try {
            Picasso.get().load(NewMessageActivity.toUser?.userImageeUrl).into(holder.toUserImg!!)
        }
        catch (e:java.lang.NullPointerException) {
            println(e.message)
        }

      // var msgs = chatData[position].fromId +" \n" + chatData[position].msg
//      when(holder.itemViewType) {
//          0-> {
//              holder.fromMsgTv?.text = chatData[position].msg
//          } else -> {
//          holder.toMsgTv?.text = chatData[position].msg
//      }
//      }


//            chatData.forEach{
//                if(it.fromId == fromId) {
//
//                } else {
//
//                }
//            }


//        if(holder.itemViewType == R.layout.row_from_msg) {
//
//        } else {
//            holder.toMsgTv?.text = chatData[position].msg
//        }
    }

    inner class customView(view:View) : RecyclerView.ViewHolder(view) {
        //MessageTextView
       var fromMsgTv:TextView? = view.findViewById(R.id.from_Msg_Tv)
        val toMsgTv:TextView? = view.findViewById(R.id.to_msg_tv)
        //UsersImagesView
        val fromUserImg:CircleImageView?=view.findViewById(R.id.fromcircleImageView)
       val toUserImg:CircleImageView? = view.findViewById(R.id.toCircleImageView)
    }
}