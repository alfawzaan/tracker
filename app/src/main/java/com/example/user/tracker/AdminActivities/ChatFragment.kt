package com.example.user.tracker.AdminActivities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.example.user.tracker.ClientActivities.UserInfo
import com.example.user.tracker.NetworkActivity.NetworkServices
import com.example.user.tracker.R
import com.example.user.tracker.Recycler.Message
import com.example.user.tracker.Recycler.MessageRecyclerViewAdapter
import com.example.user.tracker.Recycler.RecyclerViewAdapter
import com.example.user.tracker.Shared.OnlineUsers
import com.example.user.tracker.storage.SharedPrefrencesManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment(), RecyclerViewAdapter.ItemClicked, NetworkServices.OndoNetAct {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var sendButton: Button
    private lateinit var msgField: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var msgRcycler: RecyclerView
    private lateinit var msgRecyclerViewAdapter: MessageRecyclerViewAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var msgList: ArrayList<Message>
    private lateinit var usersListing: ArrayList<Users>
    private var selectedUserIndex = 0
    var messageHandle: Message = Message()
    var SendFreq = 0
    var firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var messageDB: DatabaseReference
    var textMessage = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_chat, container, false)

        //========================Initialize View Components========================================
        sendButton = view.findViewById(R.id.sendButton)
        msgField = view.findViewById(R.id.myText)
        recyclerView = view.findViewById(R.id.recyclerView)
        // msgRcycler = view.findViewById(R.id.message_recycler)
        //========================Initialize View Components========================================
        //firebaseDatabase.setPersistenceEnabled(true)

        //==========================SETUP RECYCLER VIEW FOR DISPLAY===========================
        //=====Users List======
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewAdapter = RecyclerViewAdapter(usersListing, R.layout.chatuser_item, this)
        recyclerView.adapter = recyclerViewAdapter
        //=====Users List======


        doAsync {
            doPostion(0)
        }

        //=====Msgs========
        /*msgRcycler.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        msgRecyclerViewAdapter = MessageRecyclerViewAdapter(msgList, this.context!!)
        msgRcycler.adapter = msgRecyclerViewAdapter*/
        //=====Msgs========


        //======Send Button========
        sendButton.setOnClickListener {
            if (!msgField.text.isEmpty()) {
                var me = SharedPrefrencesManager.getInstance(this.context!!).getUserInfo()
                textMessage = msgField.text.toString()
                messageHandle.id = me.username
                messageHandle.message = textMessage
                messageHandle.time = Calendar.getInstance().timeInMillis
                messageDB.setValue(messageHandle)
                addMyLayout()


                //firebase.setValue(messageHandler)
                msgField.text.clear()
                msgField.clearFocus()

            }
        }
        //======Send Button========
        //==========================SETUP RECYCLER VIEW FOR DISPLAY===========================

        return view
    }

    fun addMyLayout() {
        var fragmentManager: android.support.v4.app.FragmentManager = getFragmentManager()!!
        var fragmentTransaction: android.support.v4.app.FragmentTransaction = fragmentManager.beginTransaction()
        var setFragText = myMF()
        fragmentTransaction.add(R.id.linearLayout, setFragText)
        toast("Send btn clicked")
        var me = SharedPrefrencesManager.getInstance(this.context!!).getUserInfo()
        //messageHandle.id = me.username
        //messageHandle.message = msgField.text.toString()
        messageHandle.time = Calendar.getInstance().timeInMillis

        setFragText.userNam = me.username
        setFragText.textM = textMessage
        var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        setFragText.timeSt = timeformat.format(messageHandle.time)
        fragmentTransaction.commit()
        //setFragText.onButtonPressed(messageHandle)
        //fragmentTransaction.commit()
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        longToast(ScrollView.FOCUS_DOWN.toString())

        //scrollView.scrollTo(0, ScrollView.FOCUS_DOWN/20)
        //scrollView?.isFillViewport = true
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //scrollView.elevation = 3.0f
        //}
        linearLayout.scrollTo(0, LinearLayout.FOCUS_DOWN / 20)
        longToast(linearLayout.height.toString())
        doSendMsg()
    }

    fun doSendMsg() {
        var networkServices: NetworkServices = NetworkServices(this, this.context!!)
        //If executed successful, send to firebase, and save to internal file
        SendFreq++
        networkServices.execute("SendMessage", msgField?.text.toString(), OnlineUsers.onlines[selectedUserIndex].email)

        //******************************CHANGE SELECTED USER TO THE USERS EMAIL*************************
        //******************************CHANGE SELECTED USER TO THE USERS EMAIL*************************
        //******************************CHANGE SELECTED USER TO THE USERS EMAIL*************************
    }

    override fun doNetAct(result: String?) {
        if (result != null && result != "") {
            var jsonResult = JSONObject(result)
            if (jsonResult.getString("actionType") == "SendMessage") {
                if (jsonResult.getBoolean("result")) {
                    //SendFreq = 0
                    return
                } else {

                }
            }
            //msgRecyclerViewAdapter.addMessage(messageHandle, 0) //
        } /*else {
            if (SendFreq == 5) {
                SendFreq = 0
                toast("Message Could Not be Sent")
                return
            }
            doSendMsg()
        }*/

    }

    fun addOtherLayout(msg: Message) {
        var fragmentManager: android.support.v4.app.FragmentManager = getFragmentManager()!!
        var fragmentTransaction: android.support.v4.app.FragmentTransaction = fragmentManager.beginTransaction()
        var setFragText = oMF()
        fragmentTransaction.add(R.id.linearLayout, setFragText)
        toast("Send btn clicked")
        var me = SharedPrefrencesManager.getInstance(view?.context!!).getUserInfo()
        //messageHandle.id = me.username
        //messageHandle.message = msgField.text.toString()
        messageHandle.time = Calendar.getInstance().timeInMillis

        setFragText.userNam = msg.id
        setFragText.textM = msg.message
        var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        setFragText.timeSt = timeformat.format(msg.time)
        fragmentTransaction.commit()
        //setFragText.onButtonPressed(messageHandle)
        //fragmentTransaction.commit()
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        linearLayout.scrollTo(0, LinearLayout.FOCUS_DOWN)
        //doSendMsg()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }

        var size = OnlineUsers.onlines.size
        var count = 0
        usersListing = arrayListOf()
        msgList = arrayListOf()

        while (count < size) {
            usersListing.add(Users(R.drawable.ic_user_24dp, OnlineUsers.onlines[count].firstName, "Online"))
            count++
        }

    }

    override fun onClick(userInfo: UserInfo) {

    }

    //Handles user selection
    override fun doPostion(postion: Int) {
        setSelectedUserIndex(postion)

        //use this userDetail to fetch the chat history from the internal saved chat
//        var userDetail = usersListing[postion].userName
        view?.findViewById<LinearLayout>(R.id.linearLayout)?.removeAllViewsInLayout()
        var sharedPrefrencesManager: SharedPrefrencesManager = SharedPrefrencesManager.getInstance(this.context!!)
        messageDB = firebaseDatabase.getReference(sharedPrefrencesManager.getUserInfo().username).child("MemberMessage").child(OnlineUsers.onlines[selectedUserIndex].username)

        messageDB.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dbError: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var messageValue = snapshot.getValue(Message::class.java)
                Log.i("MSGSTRCOVT",messageValue?.message!!)
                if(snapshot != null){
                    if (messageValue?.id != sharedPrefrencesManager.getUserInfo().username) {
                        addOtherLayout(messageValue!!)
                    }
                }
            }
        })
        //add the item into the file system
        //Clear the screen from
        //read the current user from the file system
        //recyclerViewAdapter.itemsInList.clear()
    }

    fun setSelectedUserIndex(index: Int) {
        selectedUserIndex = index
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}
