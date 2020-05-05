package com.example.user.tracker.Recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.user.tracker.AdminActivities.Users
import com.example.user.tracker.ClientActivities.UserInfo
import com.example.user.tracker.R

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    var itemsInList: ArrayList<Users>
    var list: Int
    var itemClicked: ItemClicked? = null


    interface ItemClicked {
        fun onClick(userInfo: UserInfo)
        fun doPostion(p: Int)
    }

    /*var itemClickListener: ItemClickListener? = null
    interface ItemClickListener{
        //fun itemClick(users: Users)
        fun doPostion(postion: Int)
    }*/

    constructor(itemsInList: ArrayList<Users>, list: Int, itemClicked: ItemClicked) : super() {
        this.itemsInList = itemsInList
        this.list = list
        this.itemClicked = itemClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        var convertView = inflater.inflate(list, parent, false)

        return RecyclerViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return itemsInList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.Image.setImageResource(itemsInList[position].imageId)
        holder.name.text = itemsInList[position].userName
        holder.status.text = itemsInList[position].userStatus
    }

    inner class RecyclerViewHolder : RecyclerView.ViewHolder, View.OnClickListener {


        var Image: ImageView
        var name: TextView
        var status: TextView

        constructor(itemView: View) : super(itemView) {
            this.Image = itemView.findViewById(R.id.userImage)
            this.name = itemView.findViewById(R.id.name)
            this.status = itemView.findViewById(R.id.status)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (itemClicked != null) {
                itemClicked?.doPostion(adapterPosition)
            }
        }
        /*override fun onClick(v: View?) {
            itemClickListener?.doPostion(adapterPosition)
        }*/
    }
}

