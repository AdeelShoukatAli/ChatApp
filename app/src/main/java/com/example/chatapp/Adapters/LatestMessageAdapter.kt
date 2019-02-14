package com.example.chatapp.Adapters


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.chatapp.Models.LatestMsg
import com.example.chatapp.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.latest_msg_row.*

class LatestMessageAdapter(var ctx: Context, var latestMsgData:ArrayList<LatestMsg>) : RecyclerView.Adapter<LatestMessageAdapter.customViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): customViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.latest_msg_row,null)
        return customViewHolder(view)
    }

    override fun getItemCount(): Int {
       return latestMsgData.size
    }

    override fun onBindViewHolder(p0: customViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class customViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var senderName:TextView? = view.findViewById(R.id.userName_tv_latestMessage)
        var msg:TextView? = view.findViewById(R.id.msg_tv_latestMessage)
        val image:CircleImageView? = view.findViewById(R.id.image_latestMsg)

    }
}