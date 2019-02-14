package com.example.chatapp.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.chatapp.Models.Users
import com.example.chatapp.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class NewMessag_recyle_adapter(var ctx:Context, var userData:ArrayList<Users>, var viewClick:(view:View, position:Int) ->Unit): RecyclerView.Adapter<NewMessag_recyle_adapter.customViewHoler>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): customViewHoler {
        val view = LayoutInflater.from(ctx).inflate(R.layout.newmessage_recycle_layout,null)
        return customViewHoler(view)
    }

    override fun getItemCount(): Int {
      return userData.size
    }

    override fun onBindViewHolder(holder: customViewHoler, position: Int) {
        holder.userName.text = userData[position].userName
        Picasso.get().load(userData[position].userImageeUrl).into(holder.userImage)
         holder.cardView.setOnClickListener{
            viewClick(it,position)
        }

    }

    inner class customViewHoler(view: View) : RecyclerView.ViewHolder(view) {
    val userName:TextView = view.findViewById(R.id.userName_tv_recycleView_newMessageActivity)
    val userImage = view.findViewById<CircleImageView>(R.id.user_image_NewMessageActivity)
    val cardView = view.findViewById<CardView>(R.id.card_view)
}
}

