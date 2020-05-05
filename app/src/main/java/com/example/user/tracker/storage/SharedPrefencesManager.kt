package com.example.user.tracker.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.user.tracker.AdminActivities.AdminUser

class SharedPrefrencesManager {
    var context: Context? = null
    val SHARED_PREF_NAME = "my_shared_preference"

    constructor(context: Context) {
        this.context = context
    }


    companion object {
        var sharedPrefencesManager: SharedPrefrencesManager? = null
        fun getInstance(context: Context): SharedPrefrencesManager {
            if (sharedPrefencesManager == null) {
                sharedPrefencesManager = SharedPrefrencesManager(context)
            }
            return sharedPrefencesManager!!
        }

    }

    fun userType(boolean: Boolean) {
        var sharedPrefrences: SharedPreferences = context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
        var sharedPrefEditor: SharedPreferences.Editor = sharedPrefrences.edit()
        sharedPrefEditor.putBoolean("admin", boolean)
        sharedPrefEditor.apply()
    }

    fun getUserType(): Boolean {
        var sharedPreferences = context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences?.getBoolean("admin", false)!!
    }

    fun saveUserInfo(adminUser: AdminUser) {
        var sharedPrefrences: SharedPreferences = context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
        var sharedPrefEditor: SharedPreferences.Editor = sharedPrefrences.edit()


        sharedPrefEditor.putString("email", adminUser.email)

        sharedPrefEditor.putString("password", adminUser.password)
        sharedPrefEditor.putString("username", adminUser.username)
        sharedPrefEditor.putString("firstname", adminUser.firstname)
        sharedPrefEditor.putString("lastname", adminUser.lastname)
        /*sharedPrefEditor.putString("username", UserInfo.username)

        sharedPrefEditor.putString("phoneno", UserInfo.pNo)
        sharedPrefEditor.putString("latitude", UserInfo.latitude.toString())
        sharedPrefEditor.putString("longitude", UserInfo.longitude.toString())*/

        sharedPrefEditor.apply()
    }

    fun isLoggedIn(): Boolean {
        var sharedPrefrences: SharedPreferences = context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
        return sharedPrefrences.getString("email", null) != null
                && sharedPrefrences.getString("email", null) != null
                && sharedPrefrences.getString("password", null) != null
                && sharedPrefrences.getString("username", null) != null
                && sharedPrefrences.getString("firstname", null) != null
                && sharedPrefrences.getString("lastname", null) != null

    }

    fun getUserInfo(): AdminUser {
        var sharedPrefrences: SharedPreferences = context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
        var email = sharedPrefrences.getString("email", null)
        var password = sharedPrefrences.getString("password", null)
        var username = sharedPrefrences.getString("username", null)
        var firstname = sharedPrefrences.getString("firstname", null)
        var lastname = sharedPrefrences.getString("lastname", null)

        /*var userInfo: UserInfo = UserInfo()
        userInfo.email = sharedPrefrences.getString("email", null)
        userInfo.username = sharedPrefrences.getString("username", null)
        userInfo.password = sharedPrefrences.getString("password", null)
        userInfo.firstName = sharedPrefrences.getString("firstname", null)
        userInfo.lastName = sharedPrefrences.getString("lastname", null)
        userInfo.pNo = sharedPrefrences.getString("phoneno", null)
        userInfo.latitude = sharedPrefrences.getString("latitude", null)?.toDouble()!!
        userInfo.longitude = sharedPrefrences.getString("longitude", null)?.toDouble()!!*/
        return AdminUser(email, password, username, firstname, lastname)
    }

    fun clearUserInfo() {
        var sharedPreferences: SharedPreferences = context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
        var sharedPrefEditor: SharedPreferences.Editor = sharedPreferences.edit()

        sharedPrefEditor.clear()
        sharedPrefEditor.apply()

    }
}