package com.example.user.tracker.AdminActivities


//import android.app.Fragment
//import android.app.FragmentTransaction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.example.user.tracker.ClientActivities.UserInfo
import com.example.user.tracker.NetworkActivity.NetworkServices
import com.example.user.tracker.R
import com.example.user.tracker.R.id.*
import com.example.user.tracker.Recycler.UserListView
import com.example.user.tracker.Shared.AllMyUsers
import com.example.user.tracker.Shared.MainActivity
import com.example.user.tracker.Shared.MyUser
import com.example.user.tracker.Shared.OnlineUsers
import com.example.user.tracker.storage.SharedPrefrencesManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import javax.net.ssl.HttpsURLConnection


class MainActivity2 : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        ChatFragment.OnFragmentInteractionListener,
        MapActions.OnFragmentInteractionListen, MapLocator.OnMapLocatorListener, NetworkServices.OndoNetAct, MyClients.OnMyClientFragIntractionListener {


    val PLAY_SERVICES_RESOLUTION_REQUEST = 232
    var map: MapLocator? = null
    var mapLocator: MapLocator? = null
    val firbase: FirebaseDatabase = FirebaseDatabase.getInstance()

    var admi: ValueEventListener? = null
    var action: FragmentTransaction? = null
    var locationComit: FragmentTransaction? = null
    var progressBar: ContentLoadingProgressBar? = null
    var progressInfo: TextView? = null
    var networkBroadcastReceiver: BroadcastReceiver? = null
    var task = 0
    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_main2)
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        Crashlytics.setUserIdentifier("Admin")

        //longToast(username)

        //var adminRef = firbase.getReference(username)
        //var hello =adminRef.child("MembersOnline").child("hello").setValue("hellooooo")
        //fetchUserList()
        //if (savedInstanceState == null) {
        //nav_view.menu.getItem(1).setChecked(true)

        //onNavigationItemSelected(nav_view.menu.getItem(0))
        //nav_view.setCheckedItem(nav_chat)
        //}
        /*doAsync {
            loadFBUsers()
            uiThread {

            }
        }*/


            if (!checkPlayServices()) {
                longToast("Please install google play services")
            }


        progressBar = findViewById(R.id.loadingProgress)
        progressInfo = findViewById(R.id.progress_info)


        val sharedPreferences = SharedPrefrencesManager.getInstance(this)
        val userInfor = sharedPreferences.getUserInfo()
        val fName = userInfor.firstname
        val lName = userInfor.lastname
        val email = userInfor.email

        val navView = findViewById<NavigationView>(R.id.nav_view)
        val header = navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.logged_in_fullname).text = lName + " " + fName
        header.findViewById<TextView>(R.id.logged_in_email).text = email


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


        //To listen for network changes
        networkBroadcastReceiver = object:  BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val conn = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo: NetworkInfo? = conn.activeNetworkInfo

                if(networkInfo != null && networkInfo?.isConnected!!){
                    when(task){
                        0 -> {
                            //toast("Task 1")

                        }
                        1 -> {
                            //toast("Task 2")
                        }
                    }
                } else {

                    toast("Disconnected")
                }
            }

        }
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        applicationContext.registerReceiver(networkBroadcastReceiver, intentFilter)


    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else
            //return
            //finish()
                return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(networkBroadcastReceiver)
    }
    fun isConnected(): Boolean {
        var connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var info = connectivityManager.activeNetworkInfo


        return info != null && info.isConnected
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


/*
    override fun onAttachFragment(fragment: Fragment) {
        //super.onAttachFragment(fragment)

            //fb().execute("locate")

    }
*/

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //fb().execute("locate")

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            nav_locate -> {
                //admi

                loadFragment("locate")
/*
                doAsyncResult {

                    loadFBUsers()

                    uiThread {
                        findViewById<LinearLayout>(R.id.fragment_container).removeAllViewsInLayout()
                        //map = supportFragmentManager.findFragmentById(R.id.fragment_container) as? MapLocator
                        toolbar.title = "Locator"
                        var mapActions: FragmentTransaction = supportFragmentManager.beginTransaction()
                        var action = mapActions.add(R.id.fragment_container, MapActions())
                        var location = supportFragmentManager.beginTransaction()
                        var locationComit = location.add(R.id.fragment_container, MapLocator())

                        action.commitAllowingStateLoss()
                        locationComit.commitAllowingStateLoss()
                    }
                }
*/

                /*
                doAsync {
                    loadFBUsers()
                    uiThread {
                        findViewById<LinearLayout>(R.id.fragment_container).removeAllViewsInLayout()
                        //map = supportFragmentManager.findFragmentById(R.id.fragment_container) as? MapLocator
                        toolbar.title = "Locator"
                        var mapActions: FragmentTransaction = supportFragmentManager.beginTransaction()
                        var action = mapActions.add(R.id.fragment_container, MapActions())
                        var location = supportFragmentManager.beginTransaction()
                        var locationComit = location.add(R.id.fragment_container, MapLocator())

                        action.commitAllowingStateLoss()
                        locationComit.commitAllowingStateLoss()
                    }
                }
                */
            }
            nav_chat -> {
                //admi
                loadFragment("chat")
/*
                doAsync {
                    loadFBUsers()
                    uiThread {
                        findViewById<LinearLayout>(R.id.fragment_container).removeAllViewsInLayout()
                        toolbar.title = "Chat"
                        var chat = supportFragmentManager.beginTransaction()
                        chat.replace(R.id.fragment_container, ChatFragment()).commit()
                    }
                }
*/

            }
            nav_my_client -> {
                //doAsync {
                fetchUserList()
                //uiThread {


                //}
                //}

            }
            nav_email -> {

            }
            nav_share -> {

            }
            nav_signout -> {
                SharedPrefrencesManager.getInstance(this).clearUserInfo()
                var intent: Intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    inner class fb : AsyncTask<String, Unit, String>() {

        override fun doInBackground(vararg params: String?): String {
            Log.i("async", "in background")
            loadFBUsers()
            return params[0]!!
        }

        override fun onPostExecute(result: String?) {
            Log.i("async", "in post Exec")

            loadFragment(result!!)
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }


    }

    fun loadFragment(fragType: String) {

        if (!isConnected()) {
            if (fragType == "locate"){
                task = 0
            }else if (fragType == "chat"){
                task = 1
            }
            progressBar?.hide()
            //longToast("Please Check Your Internet Connection")
            snackbar =Snackbar.make(findViewById(R.id.coordinatorlayout), "Please Check Your Internet Connection", Snackbar.LENGTH_LONG)

            snackbar?.show()
            return
        } else {

            progressBar?.show()
            progressInfo?.visibility = View.VISIBLE
            var username = SharedPrefrencesManager.getInstance(this).getUserInfo().username
            Log.i("async", "loadFBusers 1")
            var memebersOnline = firbase.getReference(username).child("MembersOnline")
            memebersOnline.keepSynced(true)

            //=======================THIS PART IS CALLED LATELY, NEED A ROBUST IMPLEMENTATION============
            memebersOnline.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }


                override fun onDataChange(onlineMembersDS: DataSnapshot) {
                    Log.i("async", "loadFBusers 2")

                    Log.i("SNAPSHOT", onlineMembersDS.key)
                    OnlineUsers.onlines.clear()
                    var count = 0


                    doAsync {

                        for (j in onlineMembersDS.children) {

                            //doAsync {

                            var username = j.key

                            Log.i("SNAPSHOT", j.key)


                            //longToast(j.child("Email").value.toString())
                            Log.i("SNAPSHOT", j.child("Email").getValue(String::class.java))
                            Log.i("SNAPSHOT", j.child("Firstname").getValue(String::class.java))
                            Log.i("SNAPSHOT", j.child("Lastname").getValue(String::class.java))
                            Log.i("SNAPSHOT", j.child("Email").getValue(String::class.java))


                            var email = j.child("Email").getValue(String::class.java)
                            var firstname = j.child("Firstname").getValue(String::class.java)
                            var lastname = j.child("Lastname").getValue(String::class.java)
                            var lat = j.child("Latitude").getValue(Double::class.java)
                            var longi = j.child("Longitude").getValue(Double::class.java)
                            var pNo = j.child("PhoneNo").getValue(String::class.java)
                            var sIn = j.child("Signed In").getValue(String::class.java)
                            var sOut = j.child("Signed Out").getValue(String::class.java)


                            var userInfo = UserInfo()
                            userInfo.email = email!!
                            userInfo.firstName = firstname!!
                            userInfo.lastName = lastname!!
                            userInfo.pNo = pNo!!
                            userInfo.username
                            userInfo.longitude = longi!!
                            userInfo.latitude = lat!!
                            userInfo.username = username!!
                            OnlineUsers.onlines.add(userInfo)

                            Log.i("userInfo", OnlineUsers.onlines[count].firstName)
                            count++

                        }

                        uiThread {
                            when (fragType) {

                                "locate" -> {

                                    Log.i("Loading", "Loading Fragment")
                                    findViewById<LinearLayout>(R.id.fragment_container).removeAllViewsInLayout()
                                    //map = supportFragmentManager.findFragmentById(R.id.fragment_container) as? MapLocator
                                    toolbar.title = "Locator"
                                    var mapActions: FragmentTransaction = supportFragmentManager.beginTransaction()
                                    var action = mapActions.add(R.id.fragment_container, MapActions())

                                    var location = supportFragmentManager.beginTransaction()
                                    var locationComit = location.add(R.id.fragment_container, MapLocator())

                                    action.commitAllowingStateLoss()
                                    locationComit.commitAllowingStateLoss()
                                    progressBar?.hide()
                                    progressInfo?.visibility = View.GONE

                                }
                                "chat" -> {
                                    findViewById<LinearLayout>(R.id.fragment_container).removeAllViewsInLayout()
                                    toolbar.title = "Chat"
                                    var chat = supportFragmentManager.beginTransaction()
                                    chat.replace(R.id.fragment_container, ChatFragment()).commit()
                                    progressBar?.hide()
                                    progressInfo?.visibility = View.GONE


                                }
                            }
                        }
                    }

                    Log.i("Final Size", "${OnlineUsers.onlines.size}")

                }
            })
        }

    }


    override fun userObject(userInfo: UserInfo, checkBox: CheckBox) {
        //retrieve from firebase
        var lat = userInfo.latitude
        var longi = userInfo.longitude
        var latLng: LatLng = LatLng(lat, longi)

        var username = SharedPrefrencesManager.getInstance(this).getUserInfo().username
        longToast(username)
        var userObj = firbase.getReference(username).child("MembersOnline").child(userInfo.username)

        userObj.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                latLng = LatLng(p0.child("Latitude").getValue(Double::class.java)!!, p0.child("Longitude").getValue(Double::class.java)!!)

                toast(latLng.latitude.toString())
                toast(latLng.longitude.toString())
            }
        })
        println("position in MainActivity2 ${lat}, ${longi}")
        //create the map object
        map = supportFragmentManager.findFragmentById(R.id.fragment_container) as? MapLocator
        // mapLocator = MapLocator()
        //create the map object
        map?.addMakerAndMove(latLng)

        //map?.addMakerAndMove(selectedUserLocation)

        //map?.addMakerAndMove(selectedUserLocation)
        //map.mMap.addMarker(MarkerOptions().position(selectedUserLocation))
        //map.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedUserLocation, 10f))
    }

    override fun mapType(checkBox: CheckBox) {
        if (checkBox.isChecked) {
            map?.mMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        } else {
            map?.mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }


    override fun retrieveLocation(checkBox: CheckBox, userInfo: UserInfo, startDate: String, endDate: String) {
        if (!checkBox.isChecked) {
            var networkServices: NetworkServices = NetworkServices(this, this)
            if (startDate != "" && endDate != "") {
                networkServices.execute("RetrieveLocation", userInfo.email, startDate, endDate)
            } else {
                longToast("Please Select a Date")
            }

            //the returned value will be used to populate the map

        }
    }

    override fun onResume() {
        super.onResume()
        //onNavigationItemSelected(nav_view.menu.getItem(0))
        // onNavigationItemSelected(nav_view.menu.getItem(0))
        //nav_view.setCheckedItem(nav_locate)

    }


    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun mapLocatorImplementation(userInfo: UserInfo) {

    }

    override fun doNetAct(result: String?) {

        if (result != "" && result != null) {
            var jsonResult = JSONObject(result)
            if (jsonResult.getString("actionType") == "RetrieveAllUsers") {
                var allUsers = jsonResult.getJSONArray("result")
                //AllMyUsers.allUsers.removeAll(AllMyUsers.allUsers)
                AllMyUsers.allUsers.clear()
                doAsync {

                    for (i in 0..allUsers.length() - 1) {
                        var user = allUsers[i] as JSONObject
                        var fName = user.getString("lastname") + " " + user.getString("firstname")
                        var uName = user.getString("username")
                        var phone = user.getString("phone_no")
                        var email = user.getString("email")
                        var myUser = MyUser(fName, uName, email, phone)
                        AllMyUsers.allUsers.add(myUser)
                    }
                    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

                    var databaseReference: DatabaseReference? = null
                    //doAsync {
                    //view = inflater.inflate(R.layout.fragment_my_clients, container, false)
                    databaseReference = firebaseDatabase.getReference(SharedPrefrencesManager.getInstance(this@MainActivity2).getUserInfo().username).child("MembersOnline")

                    UserListInfo.userListViewInfo.clear()

                    for (i in 0..AllMyUsers.allUsers.size - 1) {
                        Log.i("USERNAMESTR", AllMyUsers.allUsers[i].userN)

                        var userFBData = databaseReference?.child(AllMyUsers.allUsers[i].userN)?.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                //Log.i("SNAPSHOT", p0.child("Signed In").getValue(String::class.java))
                                //Log.i("SNAPSHOT", p0.child("Signed Out").getValue(String::class.java))
                                var signedIn = ""
                                var signedOut = ""
                                var latitude = 0.0
                                var longitude = 0.0

                                if (p0.child("Signed In")?.getValue(String::class.java) != null) {
                                    signedIn = p0.child("Signed In").getValue(String::class.java)!!

                                }
                                if (p0.child("Signed Out")?.getValue(String::class.java) != null) {
                                    signedOut = p0.child("Signed Out").getValue(String::class.java)!!
                                }

//===============================MAY BE USEED TO INDICATE LAST LOCATION CAPTURED=======================
                                if (p0.child("Latitude")?.getValue(Double::class.java) != null) {
                                    latitude = p0.child("Latitude").getValue(Double::class.java)!!
                                }
                                if (p0.child("Longitude")?.getValue(Double::class.java) != null) {
                                    longitude = p0.child("Longitude").getValue(Double::class.java)!!
                                }
//===============================MAY BE USEED TO INDICATE LAST LOCATION CAPTURED=======================


                                var userListView = UserListView(AllMyUsers.allUsers[i].fullName, AllMyUsers.allUsers[i].userN, signedIn, signedOut)
                                UserListInfo.userListViewInfo.add(userListView)
                            }
                        })

                    }

                    uiThread {
                        findViewById<LinearLayout>(R.id.fragment_container).removeAllViewsInLayout()
                        toolbar.title = "My Clients"
                        var myClients = supportFragmentManager.beginTransaction()
                        myClients.replace(R.id.fragment_container, MyClients()).commitAllowingStateLoss()
                    }
                }


            }

        }

        //Retrieve Users Location History

    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = SharedPrefrencesManager.getInstance(this)
        if (!sharedPreferences.isLoggedIn()) {
            var intent: Intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            //if (savedInstanceState == null) {



            onNavigationItemSelected(nav_view.menu.getItem(0))
            nav_view.setCheckedItem(nav_locate)

            //nav_view.menu.getItem(1)
            //}
        }
    }

    fun loadFBUsers() {
        var username = SharedPrefrencesManager.getInstance(this).getUserInfo().username
        Log.i("async", "loadFBusers 1")
        var memebersOnline = firbase.getReference(username).child("MembersOnline")
        memebersOnline.keepSynced(true)
/*
        memebersOnline.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.i("child","Changed")

                Log.i("SNAPSHOT", p0.child("Email").getValue(String::class.java))
                Log.i("SNAPSHOT", p0.child("Firstname").getValue(String::class.java))
                Log.i("SNAPSHOT", p0.child("Lastname").getValue(String::class.java))
                Log.i("SNAPSHOT", p0.child("Email").getValue(String::class.java))
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.i("child","Added")
                Log.i("SNAPSHOT", p0.child("Email").getValue(String::class.java))
                Log.i("SNAPSHOT", p0.child("Firstname").getValue(String::class.java))
                Log.i("SNAPSHOT", p0.child("Lastname").getValue(String::class.java))
                Log.i("SNAPSHOT", p0.child("Email").getValue(String::class.java))
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
*/
        //=======================THIS PART IS CALLED LATELY, NEED A ROBUST IMPLEMENTATION============
        memebersOnline.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }


            override fun onDataChange(onlineMembersDS: DataSnapshot) {
                Log.i("async", "loadFBusers 2")

                Log.i("SNAPSHOT", onlineMembersDS.key)
                OnlineUsers.onlines.clear()
                var count = 0
                for (j in onlineMembersDS.children) {

                    //doAsync {

                    var username = j.key

                    Log.i("SNAPSHOT", j.key)

                    /*
                    //longToast(j.child("Email").value.toString())
                    Log.i("SNAPSHOT", j.child("Email").getValue(String::class.java))
                    Log.i("SNAPSHOT", j.child("Firstname").getValue(String::class.java))
                    Log.i("SNAPSHOT", j.child("Lastname").getValue(String::class.java))
                    Log.i("SNAPSHOT", j.child("Email").getValue(String::class.java))
                    */

                    var email = j.child("Email").getValue(String::class.java)
                    var firstname = j.child("Firstname").getValue(String::class.java)
                    var lastname = j.child("Lastname").getValue(String::class.java)
                    var lat = j.child("Latitude").getValue(Double::class.java)
                    var longi = j.child("Longitude").getValue(Double::class.java)
                    var pNo = j.child("PhoneNo").getValue(String::class.java)
                    var sIn = j.child("Signed In").getValue(String::class.java)
                    var sOut = j.child("Signed Out").getValue(String::class.java)


                    var userInfo = UserInfo()
                    userInfo.email = email!!
                    userInfo.firstName = firstname!!
                    userInfo.lastName = lastname!!
                    userInfo.pNo = pNo!!
                    userInfo.username
                    userInfo.longitude = longi!!
                    userInfo.latitude = lat!!
                    userInfo.username = username!!
                    OnlineUsers.onlines.add(userInfo)

                    Log.i("userInfo", OnlineUsers.onlines[count].firstName)
                    count++
                    //   uiThread {
                    //if (savedInstanceState == null) {

                    //nav_view.
                    //=========RETRIEVE ONLINE USERS AND POPULATE THE MAP============

                    //if (savedInstanceState == null) {

                    /*var mapActions: FragmentTransaction = supportFragmentManager.beginTransaction()
                    var action = mapActions.add(R.id.fragment_container, MapActions())
                    var commitAction = action.commit()

                    var location = supportFragmentManager.beginTransaction()
                    location.add(R.id.fragment_container, MapLocator()).commit()

                    map = supportFragmentManager.findFragmentById(R.id.fragment_container) as? MapLocator*/
                    // }

                    //}
                    //}
                    //}
                }

                Log.i("Final Size", "${OnlineUsers.onlines.size}")

            }
        })
    }

    fun fetchUserList() {
        var networkServices: NetworkServices = NetworkServices(this, this)
        networkServices.execute("RetrieveAllUsers")
    }

    override fun onMyClientFragIntractionListener() {
    }

}
