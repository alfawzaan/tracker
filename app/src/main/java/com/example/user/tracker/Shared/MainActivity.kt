package com.example.user.tracker.Shared

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.example.user.tracker.AdminActivities.AdminUser
import com.example.user.tracker.AdminActivities.MainActivity2
import com.example.user.tracker.ClientActivities.ChatActivity
import com.example.user.tracker.NetworkActivity.NetworkServices
import com.example.user.tracker.R
import com.example.user.tracker.storage.SharedPrefrencesManager
import com.google.firebase.crash.FirebaseCrash
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SignInPart.DataPasser, NetworkServices.OndoNetAct {

    val LOGGED_IN: Int = 1
    val LOGGED_OUT: Int = 0
    var loginStatus: Int = 0
    lateinit var fragEmail: String
    lateinit var fragPass: String
    var email: String? = null
    var password: String? = null
    var userType: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())
        Crashlytics.log(Log.DEBUG, "tag", "message")
        //startService(Intent(this, ClientService::class.java))
        //if (loginStatus == LOGGED_OUT) {
            //Add Sign in and on Success, open navigation drawer
           // println("Adding Fragment")

        //} else {
            //startActivity(intentFor<MainActivity2>())
            //openMapView()
        //}
    }

    fun isConnected(): Boolean{
        var connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var info = connectivityManager.activeNetworkInfo

        return info != null && info.isConnected
    }
    //implement fragmentInterface when the login button is clicked
    override fun onLoginClicked(email: String, pass: String, userType: Boolean) {

        if (email != null && pass != null) {
            this.email = email
            this.password = pass
            //For internal testing==========
            //startActivity(intentFor<MainActivity2>())
            //For internal testing==========
            if (isConnected()){
                var sharedPrefrencesManager = SharedPrefrencesManager.getInstance(this)
                if (userType) {
                    sharedPrefrencesManager.userType(userType)
                } else {
                    sharedPrefrencesManager.userType(userType)
                }
                var networkServices: NetworkServices = NetworkServices(this, this)
                networkServices.execute("Login", email, pass)
            }else{
                longToast("Please Check Your Internet Connection")
            }


        } else {
            Toast.makeText(this, "Both Field Should be Filled", Toast.LENGTH_SHORT)
        }

    }

    override fun onStart() {
        super.onStart()
        if (SharedPrefrencesManager.getInstance(this).isLoggedIn()) {
            var sharedPrefrencesManager = SharedPrefrencesManager.getInstance(this)
            if(sharedPrefrencesManager.getUserType()){
                var intent: Intent = Intent(this, MainActivity2::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }else{
                var intent: Intent = Intent(this, ChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

        }else{
            addSingInFragment()
        }
    }
    /*override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if(fragment == SignInPart() ){

            var signInPart: SignInPart = fragment as SignInPart
            signInPart.setOnButtonPressedListener(this)

        }
    }*/

    override fun doNetAct(result: String?) {
        longToast(result.toString())
        longToast("doNetAct")

        p?.visibility = ProgressBar.GONE
        if (result != null && result != "") {
            //==========SHARED PREFERENCE==================
            var jsonObj = JSONObject(result)
            var loggedIn: Boolean = jsonObj.getBoolean("login")
            //toast(loggedIn)
            if (loggedIn) {
                var arrayUserInfo = jsonObj.getJSONArray("userInfo")
                var jobj = arrayUserInfo[0] as JSONObject
                var email = jobj?.getString("email")
                var username = jobj?.getString("username")
                var firstname = jobj?.getString("firstname")
                var lastname = jobj?.getString("lastname")
                var adminUser: AdminUser = AdminUser(email!!, password!!, username!!, firstname, lastname)
                //adminUser.email =
                //adminUser.password =
                var sharedPrefrencesManager = SharedPrefrencesManager.getInstance(this)
                sharedPrefrencesManager.saveUserInfo(adminUser)
                if (sharedPrefrencesManager.getUserType()) {
                    //sharedPrefrencesManager.userType(true)
                    startActivity<MainActivity2>()
                } else {
                    //sharedPrefrencesManager.userType(false)
                    startActivity<ChatActivity>()
                }
            }
        }
    }

    var p: ProgressBar? =  null
    override fun progressbarVisiblity(pBar: ProgressBar) {
        p = pBar
        if(pBar.visibility == ProgressBar.VISIBLE){
            pBar.visibility = ProgressBar.GONE
        }else {
            pBar.visibility = ProgressBar.VISIBLE
        }
    }

    fun addSingInFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        var signInPart: SignInPart = SignInPart()
        fragmentTransaction.add(R.id.linearChild, signInPart)
        fragmentTransaction.commit()
        //fragmentTransaction.show(signInPart)
        linearChild.scrollTo(0, LinearLayout.FOCUS_DOWN)
    }
}
