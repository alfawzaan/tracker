package com.example.user.tracker.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.user.tracker.R

class UserListRecyclerViewAdapter: RecyclerView.Adapter<UserListRecyclerViewAdapter.UserViewHolder>{

    var userList: ArrayList<UserListView>
    var context: Context
    var onViewHolderClick: OnViewHolderClick

    constructor(userList: ArrayList<UserListView>, context: Context, onViewHolderClick: OnViewHolderClick) : super() {
        this.userList = userList
        this.context = context
        this.onViewHolderClick = onViewHolderClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UserViewHolder {
        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.client_list_layout, parent, false)
        return  UserViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userfullName.text = userList[position].fullName
        holder.userUsername.text = userList[position].username
        holder.signedIn.text = userList[position].signedIn
        holder.signedOut.text = userList[position].signedOut
    }

    interface OnViewHolderClick{
        fun userPostion(position: Int)
    }

    inner class UserViewHolder: RecyclerView.ViewHolder, View.OnClickListener{


        var userfullName: TextView
        var userUsername: TextView
        var signedIn: TextView
        var signedOut: TextView

        constructor(itemView: View) : super(itemView) {
            userfullName = itemView.findViewById(R.id.client_f_name)
            userUsername = itemView.findViewById(R.id.client_user_name)
            signedIn = itemView.findViewById(R.id.signed_in_time)
            signedOut = itemView.findViewById(R.id.signed_out_time)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onViewHolderClick?.userPostion(adapterPosition)
        }
    }
}