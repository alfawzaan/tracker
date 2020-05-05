package com.example.user.tracker

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.example.user.tracker.ClientActivities.ChatActivity
import com.example.user.tracker.NetworkActivity.NetworkServices
import com.example.user.tracker.storage.SharedPrefrencesManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.annotations.Nullable
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ClientService : Service(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, NetworkServices.OndoNetAct {

    var REQUEST_LOCATION = 11
    var mLocation: Location? = null
    var mGoogleApiClient: GoogleApiClient? = null
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000


    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL: Long = 60000 * 20 /* 20 mins */
    private val FASTEST_INTERVAL: Long = 60000 * 5 /* 5 mins */

    private val permissionsToRequest: Array<String>? = null
    private val permissionsRejected: Array<String>? = null
    private var permissions: Array<String>? = null
    lateinit var lati: DatabaseReference
    lateinit var longi: DatabaseReference
    var chatActivity = ChatActivity()
    var networkServices: NetworkServices = NetworkServices(this, this)


    lateinit var signedOutTime: DatabaseReference

    @Override
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)
        //chatActivity.firebase.setPersistenceEnabled(true)
        permissions = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)


        //chatActivity.requestPermission()

        /*doAsync {
            networkServices.execute("RetrieveUserMe")
            uiThread {*/
        connectClient()
        //}
        //}


        lati = ChatActivity.lati
        longi = ChatActivity.longi

        Log.i("LoSStr", "$longi")
        Log.i("LaSStr", "$lati")


        //lati = (intent!!.extras["LONGI"] as DatabaseReference)
        //longi = (intent!!.extras["LATI"] as DatabaseReference)


        if (!checkPlayServices()) {
            Toast.makeText(applicationContext, "Please install google play services", Toast.LENGTH_LONG).show()
        }

        return START_REDELIVER_INTENT
    }

    override fun doNetAct(result: String?) {
        if (result != null && result != "") {

            var jsonResult = JSONObject(result)
            if (jsonResult.getBoolean("result")) {
                longToast("Location Updated")
            }
            /*if (jsonResult.getString("actionType") == "SendMessage") {
                if (jsonResult.getBoolean("result")) {
                    return
                } else {

                }
            }*/

            /*var array = jsonResult.getJSONArray("userInfo")

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
            userInfo.a_id = jsonObj.getString("a_id")*/


            //var sharedPrefrencesManager: SharedPrefrencesManager = SharedPrefrencesManager.getInstance(this)

            /*ChatActivity.userInfo = userInfo

            doAsync {
                var admin = firebase.getReference(userInfo.a_id)
                var addMeOnline = admin.child("MembersOnline").child(userInfo.username)
                addMeOnline.child("Firstname").setValue(userInfo.firstName)
                addMeOnline.child("Lastname").setValue(userInfo.lastName)
                addMeOnline.child("PhoneNo").setValue(userInfo.pNo)
                addMeOnline.child("Email").setValue(userInfo.email)
                longi = addMeOnline.child("Longitude")
                lati = addMeOnline.child("Latitude")
                signedOutTime = addMeOnline.child("Signed Out")
                messageFbR = admin.child("MemberMessage").child(userInfo.username)

                var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                var time = timeformat.format(Calendar.getInstance().timeInMillis)
                addMeOnline.child("Signed In").setValue(time)
                uiThread {
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
                }
            }*/
        }
    }



    fun connectClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build()
        mGoogleApiClient?.connect()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        /*if (!SharedPrefrencesManager.getInstance(this).isLoggedIn()) {
            var intent: Intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            var networkServices: NetworkServices = NetworkServices(this, this)
            networkServices.execute("RetrieveUserMe")
        }*/
        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.chatActivity, permissions!!, REQUEST_LOCATION)
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
                apiAvailability.getErrorDialog(this.chatActivity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else
            //return
            //finish()
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
    }

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_LOCATION)
            }
            return
        }
    }*/

    protected fun startLocationUpdates() {
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
        if (lati != null && longi != null) {
            lati.setValue(location?.latitude)
            longi.setValue(location?.longitude)
            Log.i("LocStr", "${location?.latitude}  ${location?.longitude}")
            if (SharedPrefrencesManager?.getInstance(this).getUserInfo().email != null) {
                var date: Calendar = Calendar.getInstance()
                var year = date.get(Calendar.YEAR)
                var month = date.get(Calendar.MONTH)
                var day = date.get(Calendar.DAY_OF_MONTH)
                var locLogDate = "$year + $month + $day"
                doAsync {
                    networkServices.execute("SaveLocation", location?.longitude, location?.latitude, SharedPrefrencesManager?.getInstance(this@ClientService).getUserInfo().email, locLogDate)

                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()

        var timeformat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        var time = timeformat.format(Calendar.getInstance().timeInMillis)
        signedOutTime.setValue(time)

    }

    fun stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient!!.isConnected()) {
                LocationServices.FusedLocationApi
                        .removeLocationUpdates(mGoogleApiClient, this)
                mGoogleApiClient?.disconnect()
            }
        }
    }


}