package com.example.chatapp.Adapters


import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.chatapp.Models.ChatMessage
import com.example.chatapp.Models.LatestMsgUser
import com.example.chatapp.Models.Users
import com.example.chatapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class LatestMessageAdapter(var ctx: Context,
                           var latestMsgsData:ArrayList<LatestMsgUser>,
                           var viewClick:(view:View,position:Int) -> Unit,
                           var LongClick:(view:View,position:Int) -> Unit
                           ) : RecyclerView.Adapter<LatestMessageAdapter.customViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): customViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.latest_msg_row,null)
//        Toast.makeText(ctx,latestMsgData.size,Toast.LENGTH_SHORT).show()
        return customViewHolder(view)
    }

    override fun getItemCount(): Int {
       return latestMsgsData.size
    }

    override fun onBindViewHolder(holder:customViewHolder, position: Int) {
        holder.cardView.setOnClickListener {
            viewClick(it,position)
        }
        holder.cardView.setOnLongClickListener {
            LongClick(it,position)
            true
        }
        holder.msg?.text = latestMsgsData[position].msg
//        latestMsgData.values.forEach{
//            holder.msg?.text = it.msg
//        }

        try{
            holder.senderName?.text = latestMsgsData[position].userName
        } catch (e:Exception){}

        try {
            Picasso.get().load(latestMsgsData[position].userImgUrl).into(holder.image)
        } catch (e:Exception){}

    }

    inner class customViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var senderName:TextView? = view.findViewById(R.id.userName_tv_latestMessage)
        var msg:TextView? = view.findViewById(R.id.msg_tv_latestMessage)
        val image:CircleImageView? = view.findViewById(R.id.image_latestMsg)
       val cardView:CardView = view.findViewById(R.id.latestMsg_view)

    }
}