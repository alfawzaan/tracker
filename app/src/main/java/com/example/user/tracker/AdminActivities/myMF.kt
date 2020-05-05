package com.example.user.tracker.AdminActivities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.user.tracker.R
import com.example.user.tracker.Recycler.Message
import java.text.SimpleDateFormat
import java.util.*


class myMF : Fragment() {

    lateinit var uName: TextView
    lateinit var msg: TextView
    lateinit var time: TextView

    lateinit var userNam: String
    lateinit var textM: String
    lateinit var timeSt: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_my_m, container, false)
        uName = view.findViewById(R.id.me)
        msg = view.findViewById(R.id.txtMyMessage)
        time = view.findViewById(R.id.txtMyMessageTime)
        setTextInView(userNam, textM, timeSt)

        return view
    }

    fun onButtonPressed(msg: Message) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }



    fun setTextInView(un: String, msg: String, tSt: String)
    {
        uName.setText(un)
        this.msg.text =msg
        this.time.text = tSt
    }
}
