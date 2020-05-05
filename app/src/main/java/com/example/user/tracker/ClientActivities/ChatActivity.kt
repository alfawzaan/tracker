package com.example.user.tracker.ClientActivities

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.crashlytics.android.Crashlytics
import com.example.user.tracker.AdminActivities.myMF
import com.example.user.tracker.AdminActivities.oMF
import com.example.user.tracker.ClientService
import com.example.user.tracker.NetworkActivity.NetworkServices
import com.example.user.tracker.R
import com.example.user.tracker.Recycler.Message
import com.example.user.tracker.Recycler.MessageRecyclerViewAdapter
import com.example.user.tracker.Shared.MainActivity
import com.example.user.tracker.storage.SharedPrefrencesManager
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*
import org.jetbrains.anko.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class ChatActivity : AppCompatActivity(),
        NetworkServices.OndoNetAct {
    /*GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener,*/


    var REQUEST_LOCATION = 11
    private val ALL_PERMISSIONS_RESULT = 101
    private var permissions: Array<String> = arrayOf<String>(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    var location: Location? = null
    var locationManager: LocationManager? = null
    var latitude = 0.0
    var longitude = 0.0
    val firebase: FirebaseDatabase = FirebaseDatabase.getInstance()


    var messageFbR: DatabaseReference? = null

    private lateinit var sendButton: Button
    private lateinit var msgField: EditText
    private lateinit var msgRcycler: RecyclerView
    private lateinit var msgRecyclerViewAdapter: MessageRecyclerViewAdapter
    private lateinit var msgList: ArrayList<Message>
    var textMessage = ""
    var messageHandle = Message()
    var SendFreq = 0
    lateinit var signedOutTime: DatabaseReference

    companion object {
    lateinit var lati: DatabaseReference
    lateinit var longi: DatabaseReference
    //  var userInfo: UserInfo? =  null
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //locationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
        //var googleApiClient: GoogleApiClient = GoogleApiClient.Builder(this)

//        firebase.setPersistenceEnabled(true)
        Crashlytics.setUserIdentifier("Client")

        //========================Initialize View Components========================================
        sendButton = findViewById(R.id.cSendButton)
        msgField = findViewById(R.id.cMyText)
        //msgRcycler = findViewById(R.id.cMessage_recycler)
        msgList = arrayListOf()
        //========================Initialize View Components========================================

        //=====Msgs========
        /*msgRcycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        msgRecyclerViewAdapter = MessageRecyclerViewAdapter(msgList, this)
        msgRcycler.adapter = msgRecyclerViewAdapter*/
        //=====Msgs========
        requestPermission()

        sendButton.setOnClickListener { prepareSendMsg() }
        //


        var networkServices: NetworkServices = NetworkServices(this, this)

        /*requestPermission()*/
        //doAsync {
        networkServices.execute("RetrieveUserMe")
        //uiThread {
        //}
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                var time = timeformat.format(Calendar.getInstance().timeInMillis)
                signedOutTime.setValue(time)
                SharedPrefrencesManager.getInstance(this).clearUserInfo()
                var intent: Intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun prepareSendMsg() {
        if (!msgField.text.isEmpty()) {
            var me = SharedPrefrencesManager.getInstance(this).getUserInfo()
            textMessage = msgField.text.toString()
            messageHandle.id = me.username
            messageHandle.message = textMessage
            messageHandle.time = Calendar.getInstance().timeInMillis

            if (messageFbR != null) {
                messageFbR?.setValue(messageHandle)
            }
            addMyLayout()

            //firebase.setValue(messageHandler)
            msgField.text.clear()
            msgField.clearFocus()

            //doSendMsg()
        }

        /*if (!msgField.text.isEmpty())
        {

            toast("Send btn clicked")
            var me = SharedPrefrencesManager.getInstance(this).getUserInfo()
            var messageHandle = Message()
            messageHandle.id = me.username
            messageHandle.message = textMessage
            messageHandle.time = Calendar.getInstance().timeInMillis
            msgList.add(messageHandle)
            msgRecyclerViewAdapter.notifyDataSetChanged()*/
        //doSendMsg()
        //msgRecyclerViewAdapter.addMessage(messageHandle, 0)
        //doSendMsg()
        //}
    }

    fun addMyLayout() {
        var fragmentManager: android.support.v4.app.FragmentManager = supportFragmentManager
        var fragmentTransaction: android.support.v4.app.FragmentTransaction = fragmentManager.beginTransaction()
        var setFragText = myMF()
        fragmentTransaction.add(R.id.linearLayout, setFragText)
        toast("Send btn clicked")
        var me = SharedPrefrencesManager.getInstance(this).getUserInfo()
        //messageHandle.id = me.username
        //messageHandle.message = msgField.text.toString()
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
        var me = SharedPrefrencesManager.getInstance(this).getUserInfo()

        var networkServices: NetworkServices = NetworkServices(this, this)

        /*var date: Calendar = Calendar.getInstance()
        var year = date.get(Calendar.YEAR)
        var month = date.get(Calendar.MONTH)
        var day = date.get(Calendar.DAY_OF_MONTH)

        var dateString = "$year-$month-$day"*/
        networkServices.execute("SendMessage", textMessage, me.email)
    }

    override fun doNetAct(result: String?) {
        if (result != null && result != "") {

            var jsonResult = JSONObject(result)
            if (jsonResult.getString("actionType") == "SendMessage") {
                if (jsonResult.getBoolean("result")) {
                    return
                } else {

                }
            }

            var array = jsonResult.getJSONArray("userInfo")

            var jsonObj = array[0] as JSONObject
            //var array = jsonResult?.getJSONArray()
            //var userInf = result.getJSONArray("users")
            //var theUser = userInf[0] as JSONObject
            var userInfo: UserInfo = UserInfo()
            userInfo.firstName = jsonObj.getString("firstname")
            userInfo.lastName = jsonObj.getString("lastname")
            userInfo.pNo = jsonObj.getString("phone_no")
            userInfo.email = jsonObj.getString("email")
            userInfo.myAdmin = jsonObj.getString("a_email")
            userInfo.username = jsonObj.getString("username")
            userInfo.a_id = jsonObj.getString("a_id")


            var sharedPrefrencesManager: SharedPrefrencesManager = SharedPrefrencesManager.getInstance(this)

            doAsync {
                var admin = firebase.getReference(userInfo?.a_id!!)
                var addMeOnline = admin.child("MembersOnline").child(userInfo?.username!!)
                addMeOnline.child("Firstname").setValue(userInfo?.firstName)
                addMeOnline.child("Lastname").setValue(userInfo?.lastName)
                addMeOnline.child("PhoneNo").setValue(userInfo?.pNo)
                addMeOnline.child("Email").setValue(userInfo?.email)
                longi = addMeOnline.child("Longitude")
                lati = addMeOnline.child("Latitude")
                Log.i("LOGISTR", "$longi")
                Log.i("LATISTR", "$lati")

                signedOutTime = addMeOnline.child("Signed Out")
                messageFbR = admin.child("MemberMessage").child(userInfo?.username!!)

                var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                var time = timeformat.format(Calendar.getInstance().timeInMillis)
                addMeOnline.child("Signed In").setValue(time)
                signedOutTime.setValue("")

                var servicesIntent = Intent(this@ChatActivity, ClientService::class.java)
                startService(servicesIntent)

                //startService<ClientService>(Pair("LONGI", longi), Pair("LATI", lati))
                messageFbR?.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(dbError: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var messageValue = snapshot.getValue(Message::class.java)
                        if (messageValue?.id != SharedPrefrencesManager.getInstance(this@ChatActivity).getUserInfo().username) {
                            addOtherLayout(messageValue!!)
                        }
                    }
                })
                uiThread {
                    //var bundle = Bundle()

                    // servicesIntent.putExtra("LONGI", longi)
                    //startService(servicesIntent)

                }
            }
        }
    }

    fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@ChatActivity, permissions!!, REQUEST_LOCATION)
        }
    }

    fun addOtherLayout(msg: Message) {
        var fragmentManager: android.support.v4.app.FragmentManager = supportFragmentManager!!
        var fragmentTransaction: android.support.v4.app.FragmentTransaction = fragmentManager.beginTransaction()
        var setFragText = oMF()
        fragmentTransaction.add(R.id.linearLayout, setFragText)
        toast("Send btn clicked")
        var me = SharedPrefrencesManager.getInstance(this).getUserInfo()
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


    /*fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions!!, REQUEST_LOCATION)
        }
    }*/

    /*fun connectClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build()
        mGoogleApiClient?.connect()
    }*/

    override fun onStart() {
        super.onStart()
        if (!SharedPrefrencesManager.getInstance(this).isLoggedIn()) {
            var intent: Intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } /*else {
            requestPermission()
            var networkServices: NetworkServices = NetworkServices(this, this)
            networkServices.execute("RetrieveUserMe")
        }*/

        /*if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }*/
    }


    /*override fun onResume() {
        super.onResume()

        if (!checkPlayServices()) {
            Toast.makeText(applicationContext, "Please install google play services", Toast.LENGTH_LONG).show()
        }
    }*/


    /*override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions!!, REQUEST_LOCATION)
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        startLocationUpdates()
    }

    override fun onConnectionSuspended(response: Int) {
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else
                finish()
            return false
        }
        return true
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }*/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_LOCATION)
            }
            return
        }
    }

    /*protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = UPDATE_INTERVAL
        mLocationRequest?.fastestInterval = FASTEST_INTERVAL
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "Enable Permissions", Toast.LENGTH_LONG).show()
        }

        LocationServices.FusedLocationApi?.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this)
    }

    override fun onLocationChanged(location: Location?) {
        lati.setValue(location?.latitude)
        longi.setValue(location?.longitude)
    }*/

    override fun onDestroy() {
        super.onDestroy()
        //stopLocationUpdates()

        var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        var time = timeformat.format(Calendar.getInstance().timeInMillis)
        signedOutTime.setValue(time)

    }

    override fun onResume() {
        super.onResume()

        /*var networkServices: NetworkServices = NetworkServices(this, this)
        requestPermission()
        //doAsync {
        networkServices.execute("RetrieveUserMe")*/
    }

    override fun onRestart() {
        super.onRestart()
        /*var networkServices: NetworkServices = NetworkServices(this, this)

        requestPermission()
        //doAsync {
        networkServices.execute("RetrieveUserMe")*/
    }

    /*fun stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient!!.isConnected()) {
                LocationServices.FusedLocationApi
                        .removeLocationUpdates(mGoogleApiClient, this)
                mGoogleApiClient?.disconnect()
            }
        }
    }*/


}
