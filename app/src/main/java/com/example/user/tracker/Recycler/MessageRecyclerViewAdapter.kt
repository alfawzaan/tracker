package com.example.user.tracker.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.user.tracker.R
import com.example.user.tracker.storage.SharedPrefrencesManager
import org.jetbrains.anko.longToast
import java.text.SimpleDateFormat
import java.util.*

class MessageRecyclerViewAdapter : RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder> {


    var msgList: ArrayList<Message>
    val MY_MESSAGE = 0
    val OTHER_MESSAGE = 1
    var mContext: Context? = null
    var type = 0

    constructor(msgList: ArrayList<Message>, context: Context) : super() {
        this.msgList = msgList
        this.mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view: View? = null
        mContext?.longToast("OnCreateViewHolder: Viewtype" + viewType.toString())

        return if (type==MY_MESSAGE) {
            MyMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_message_layout, parent, false), type)
        } else {
            OtherMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.other_message_layout, parent, false), type)
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun onBindViewHolder(viewHolder: MessageViewHolder, position: Int) {

        mContext?.longToast("OnBindViewHolder " + position.toString())

        System.out.print("type in msgvH " + type.toString())
        if (type == 0) {
            viewHolder.sender.text = "You"
            viewHolder.msg.text = msgList[position].message
            var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            timeformat.format(msgList[position].time)
            viewHolder.time.text = timeformat.format(msgList[position].time)
        } else {
            viewHolder.sender.text = msgList[position].id
            viewHolder.msg.text = msgList[position].message
            var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            timeformat.format(msgList[position].time)
            viewHolder.time.text = timeformat.format(msgList[position].time)
        }

        //viewHolder.bind(msgList[position])
        /*viewHolder.sender.text = "You"
        viewHolder.msg.text = msgList[position].message
        var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        timeformat.format(msgList[position].time)
        viewHolder.time.text = timeformat.format(msgList[position].time)*/
        //viewHolder.bind(msgList[position])
    }


    /*override fun getItemViewType(position: Int): Int {
        var me = SharedPrefrencesManager.getInstance(mContext!!).getUserInfo()
        mContext?.longToast("getItemViewType " + position.toString())

        if (msgList[position].id == me.username) {
            return MY_MESSAGE
        } else {
            return OTHER_MESSAGE
        }
    }*/


    /*fun addMessage(message: Message, type: Int) {
        this.type = type
        msgList.add(message)
        //notifyDataSetChanged()
    }*/

    inner class MyMessageViewHolder(itemView: View, type: Int) : MessageViewHolder(itemView, type) {
        /*var sender: TextView = itemView.findViewById(R.id.me)
        var msg: TextView = itemView.findViewById(R.id.txtMyMessage)
        var time: TextView = itemView.findViewById(R.id.txtMyMessageTime)*/

        /*override fun bind(message: Message) {

            super.bind(message)
            /*sender.text = "You"
            msg.text = message.message
            var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            timeformat.format(msgList[position].time)
            time.text = timeformat.format(message.time)*/
        }*/
    }

    inner class OtherMessageViewHolder(itemView: View, type: Int) : MessageViewHolder(itemView, type) {
        /*var sender: TextView = itemView.findViewById(R.id.txtOtherUser)
        var msg: TextView = itemView.findViewById(R.id.txtOtherMessage)
        var time: TextView = itemView.findViewById(R.id.txtOtherMessageTime)*/

        /*override fun bind(message: Message) {
            super.bind(message)
            /*sender.text = message.id
            msg.text = message.message
            var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            timeformat.format(msgList[position].time)
            time.text = timeformat.format(message.time)*/
        }*/
    }

    open class MessageViewHolder : RecyclerView.ViewHolder {

        lateinit var sender: TextView
        lateinit var msg: TextView
        lateinit var time: TextView

        constructor(itemView: View, type: Int) : super(itemView) {
           if(type == 0){
               this.sender = itemView.findViewById(R.id.me)
               this.msg = itemView.findViewById(R.id.txtMyMessage)
               this.time = itemView.findViewById(R.id.txtMyMessageTime)
           }else{
               this.sender = itemView.findViewById(R.id.txtOtherUser)
               this.msg = itemView.findViewById(R.id.txtOtherMessage)
               this.time = itemView.findViewById(R.id.txtMyMessageTime)
           }
        }

        /*open fun bind(message: Message) {
            sender.text = "You"
            msg.text = message.message
            var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            timeformat.format(message.time)
            time.text = timeformat.format(message.time)
        }*/
    }

}