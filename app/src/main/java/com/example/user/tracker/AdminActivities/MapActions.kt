package com.example.user.tracker.AdminActivities

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.tracker.R
import com.example.user.tracker.ClientActivities.UserInfo
import com.example.user.tracker.Shared.OnlineUsers
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.collections.ArrayList



class MapActions : Fragment() {

    private lateinit var listener: OnFragmentInteractionListen
    lateinit var spinner: Spinner
    lateinit var fromDate: TextView
    lateinit var toDate: TextView
    var fDate = ""
    var tDate = ""
    lateinit var mapType: CheckBox
    lateinit var rOrH: CheckBox
    lateinit var onFromDateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var onToDateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var selectedUser: UserInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_map_actions, container, false)
        spinner = view.findViewById(R.id.user_spinner) as Spinner
        fromDate = view.findViewById(R.id.fromDate) as TextView
        toDate = view.findViewById(R.id.toDate) as TextView
        mapType = view.findViewById(R.id.mapType) as CheckBox
        rOrH = view.findViewById(R.id.real_hist) as CheckBox

        fromDate.text = "pick a date"
        toDate.text = "pick a date"

        toDate.setOnClickListener { view ->
            run {
                var date: Calendar = Calendar.getInstance()
                var year = date.get(Calendar.YEAR)
                var month = date.get(Calendar.MONTH)
                var day = date.get(Calendar.DAY_OF_MONTH)

                var dateString = "$day/$month/$year"
                var dialog: DatePickerDialog = DatePickerDialog(this.context,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        onToDateSetListener,
                        year, month, day)
                dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

        }

        onToDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                //month + 1
                tDate = "$year-${month +1}-$dayOfMonth"

                var date = "$dayOfMonth/${month+1}/$year"
                toDate.text = date
            }

        }


        //Retrieve users locatin info acording the selected date store in an arraylist and iterate through the list to move within the location
        fromDate.setOnClickListener { view ->
            run {
                var date: Calendar = Calendar.getInstance()
                var year = date.get(Calendar.YEAR)
                var month = date.get(Calendar.MONTH)
                var day = date.get(Calendar.DAY_OF_MONTH)

                var dateString = "$day/$month/$year"
                var dialog: DatePickerDialog = DatePickerDialog(this.context,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        onFromDateSetListener,
                        year, month, day)
                dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

        }

        onFromDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                month + 1
                fDate = "$year-${month +1}-$dayOfMonth"
                var date = "$dayOfMonth/${month+1}/$year"
                fromDate.text = date
            }

        }

        mapType.setOnClickListener { view -> listener.mapType(mapType) }

        rOrH.isChecked = true
        rOrH.setOnClickListener {
            listener.retrieveLocation(rOrH, selectedUser, fromDate.text.toString(), toDate.text.toString())
        }
        //Retrive from firebase
        //Retrive user info from database

        var spinnerAdapter = ArrayAdapter<String>(this.context, R.layout.my_spinner_customization, onlineUsersName)
        spinner?.adapter = spinnerAdapter
        spinner?.backgroundColor = Color.TRANSPARENT

        onlineUsersName.clear()
        for (i in 0 .. OnlineUsers.onlines.size - 1) {
            onlineUsersName.add(OnlineUsers.onlines[i].firstName)
            Log.i("onlineUsers", OnlineUsers.onlines[i].firstName)
        }


        Log.i("onlineUsers", "After")
        spinnerAdapter?.notifyDataSetChanged()
        spinnerAdapter.setNotifyOnChange(true)

        //spinner.setSelection(1)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //spinner?.setSelection(spinnerAdapter?.getPosition(OnlineUsers.onlines[position].firstName))

                //toast("${OnlineUsers.onlines[position].firstName}")
                selectedUser = OnlineUsers.onlines[position]
                if (rOrH.isChecked){
                    listener.userObject(OnlineUsers.onlines[position], rOrH)
                }else{
                   listener.retrieveLocation(rOrH, selectedUser, fDate, tDate)
                }
            }
        }


        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListen) {
            listener = context



        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }



    }

    override fun onStart() {
        super.onStart()

        spinner.setSelection(0)
    }

    override fun onDetach() {
        super.onDetach()
        // listener = null
    }


    interface OnFragmentInteractionListen {
        fun userObject(userInfo: UserInfo, checkBox: CheckBox)

        fun mapType(checkBox: CheckBox)
        fun retrieveLocation(checkBox: CheckBox, userInfo: UserInfo, startDate: String, endDate: String)
    }

    companion object {
        var onlineUsersName = ArrayList<String>()
    }

}
