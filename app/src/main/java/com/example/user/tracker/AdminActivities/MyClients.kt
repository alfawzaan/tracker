package com.example.user.tracker.AdminActivities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.user.tracker.AdminActivities.UserListInfo.Companion.userListViewInfo
import com.example.user.tracker.R
import com.example.user.tracker.Recycler.UserListRecyclerViewAdapter
import com.example.user.tracker.Recycler.UserListView
import com.example.user.tracker.Shared.AllMyUsers
import com.example.user.tracker.storage.SharedPrefrencesManager
import com.google.firebase.database.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MyClients : Fragment(), UserListRecyclerViewAdapter.OnViewHolderClick {


    private var listener: OnMyClientFragIntractionListener? = null
    private var userRecyclerView: RecyclerView? = null
    private var userRecyclerViewAdapter: UserListRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_my_clients, container, false)
        userRecyclerView = view?.findViewById(R.id.my_client_recycler_view)
        //userListViewInfo = arrayListOf()
        //userListViewInfo.add(userListView)
        //userRecyclerViewAdapter?.notifyDataSetChanged()

        userRecyclerView?.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        userRecyclerViewAdapter = UserListRecyclerViewAdapter(UserListInfo.userListViewInfo, view?.context!!, this@MyClients)
        userRecyclerView?.adapter = userRecyclerViewAdapter
        return view
    }

    fun onButtonPressed() {
        listener?.onMyClientFragIntractionListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMyClientFragIntractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnMyClientFragIntractionListener {
        fun onMyClientFragIntractionListener()
    }

    override fun userPostion(position: Int) {

    }
}
