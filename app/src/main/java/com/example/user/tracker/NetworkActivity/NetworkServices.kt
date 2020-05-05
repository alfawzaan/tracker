package com.example.user.tracker.NetworkActivity

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.example.user.tracker.storage.SharedPrefrencesManager
import com.google.firebase.crash.FirebaseCrash
import org.jetbrains.anko.longToast
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class NetworkServices : AsyncTask<Any, Unit, String> {


    var ondoNetAct: OndoNetAct

    var context: Context

    constructor(ondoNetAct: OndoNetAct, context: Context) {
        this.ondoNetAct = ondoNetAct
        this.context = context
    }


    var userType = "client"

    override fun onProgressUpdate(vararg values: Unit?) {
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: String?) {
       context.longToast("Post Exc")

        context.longToast(result!!)
        Log.i("TAGCONVERSTR", "[" + result + "]");
        //var jsonObject: JSONObject = JSONObject(result.toString())
        ondoNetAct.doNetAct(result)
        //var queue = Volley.newRequestQueue(this.context)
        // queue.add(result)
    }

    override fun doInBackground(vararg params: Any?): String {
        var type = params[0]
        var result: String? = null
        //var userInfo = SharedPrefrencesManager.getInstance(this.context).getUserInfo()
        if (SharedPrefrencesManager.getInstance(this.context).getUserType()) {
            userType = "admin"
        }
        //For Login Action
        if (type!!.equals("Login")) {

            var username = params[1] as String
            var password = params[2] as String
            result = login(username, password)


        }

        //For SaveLocation Action
        if (type!!.equals("SaveLocation")) {

            var longitude: Double = params[1] as Double
            var latitude: Double = params[2] as Double
            var c_email: String = params[3] as String
            var logdate: String = params[4] as String
            result = saveLocation(longitude, latitude, c_email, logdate)
        }

        //For SendMessage Action
        if (type!!.equals("SendMessage")) {
            var message: String? = params[1] as String
            var recipient: String? = params[2] as String
            //var sentDate: String? = params[3] as String
            result = sendMessage(message, recipient)
        }

        //For RetrieveLocation Action
        if (type!!.equals("RetrieveLocation")) {
            var c_email: String? = params[1] as String
            var start_date: String? = params[2] as String
            var end_date: String? = params[3] as String
            result = retriveLocaion(c_email, start_date, end_date)

        }

        //For UpdateStatus Action
        if (type!!.equals("UpdateStatus")) {

        }

        if (type!!.equals("RetrieveUserMe")) {
            var action = params[0] as String
            result = retrieveUserMe(action)
        }

        if(type!!.equals("RetrieveAllUsers")){
            var action: String = params[0] as String
            result = retriveAllUsers(action)
        }
        return result!!
        //context.longToast(result!!)
    }

    override fun onPreExecute() {
        context.longToast("Pre Exc")

    }

    fun login(email: String?, password: String?): String {
        var result: String? = null

        /*var map: JSONObject = JSONObject()
        map.put("email", email!!)
        map.put("password", password!!)
        map.put("login_action", "login"!!)
        map.put("user", userType)*/
        var loginRequest = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" + URLEncoder.encode("login_action", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8") + "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(userType, "UTF-8")

        result = doDBComm(loginRequest)

        return result
    }

    fun sendMessage(message: String?, recipient: String?): String {
        var result: String? = null
        var userInfo = SharedPrefrencesManager.getInstance(this.context).getUserInfo()
        if (SharedPrefrencesManager.getInstance(this.context).getUserType()) {
            userType = "admin"
        }

        var sendMessageRequest = URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8") + "&" + URLEncoder.encode("recipient", "UTF-8") + "=" + URLEncoder.encode(recipient, "UTF-8") + "&" + URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("SendMessage", "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(userInfo.email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(userInfo.password, "UTF-8") + "&" + URLEncoder.encode("login_action", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8") + "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(userType, "UTF-8")
        result = doDBComm(sendMessageRequest)
        return result!!
    }

    fun retriveLocaion(client_email: String?, start_date: String?, end_date: String?): String {
        var result: String? = null
        var userInfo = SharedPrefrencesManager.getInstance(this.context).getUserInfo()
        var retriveLocationRequest = URLEncoder.encode("client_email", "UTF-8") + "=" + URLEncoder.encode(client_email, "UTF-8") + "&" + URLEncoder.encode("start_date", "UTF-8") + "=" + URLEncoder.encode(start_date, "UTF-8") + "&" + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(end_date, "UTF-8") + "&" + URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("RetrieveLocation", "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(userInfo.email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(userInfo.password, "UTF-8") + "&" + URLEncoder.encode("login_action", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8") + "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(userType, "UTF-8")
        result = doDBComm(retriveLocationRequest)
        return result!!

    }

    fun saveLocation(longitude: Double, latitude: Double, c_email: String, logdate: String): String {
        var result: String? = null
        var userInfo = SharedPrefrencesManager.getInstance(this.context).getUserInfo()
        var retriveLocationRequest = URLEncoder.encode("client_email", "UTF-8") + "=" + URLEncoder.encode(c_email, "UTF-8") + "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude.toString(), "UTF-8") + "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude.toString(), "UTF-8") + "&" + URLEncoder.encode("logdate", "UTF-8") + "=" + URLEncoder.encode(logdate, "UTF-8") + "&" + URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("SaveLocation", "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(userInfo.email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(userInfo.password, "UTF-8") + "&" + URLEncoder.encode("login_action", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8") + "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(userType, "UTF-8")
        result = doDBComm(retriveLocationRequest)
        return result!!
    }

    fun retrieveUserMe(action: String): String {
        var result: String? = null
        var userInfo = SharedPrefrencesManager.getInstance(this.context).getUserInfo()
        var retrieveUserInfo = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(userInfo.email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(userInfo.password, "UTF-8") + "&" + URLEncoder.encode("login_action", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8") + "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode("client", "UTF-8")
        result = doDBComm(retrieveUserInfo)
        return result
    }

    fun retriveAllUsers(action: String): String{
        var result: String? = null
        var userInfo = SharedPrefrencesManager.getInstance(this.context).getUserInfo()
        var retriever = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(userInfo.email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(userInfo.password, "UTF-8") + "&" + URLEncoder.encode("login_action", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8") + "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode("admin", "UTF-8")
        result = doDBComm(retriever)
        return  result
    }
    fun doDBComm(request: String): String {

        var site: String = "https://alfawzaani.000webhostapp.com/tracker/index.php"
        var url: URL = URL(site)
        var result: String = ""

        try {
            var httpUrlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpUrlConnection.requestMethod = "POST"
            httpUrlConnection.doInput = true
            httpUrlConnection.doOutput = true

            var outputStream: OutputStream = httpUrlConnection.outputStream
            var bufferedWriter: BufferedWriter = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))

            bufferedWriter.write(request)
            bufferedWriter.flush()
            bufferedWriter.flush()
            outputStream.close()
            var inputStream: InputStream = httpUrlConnection.inputStream
            var bufferedReader: BufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

            var line = bufferedReader.readLine()
            //context.longToast(line)
            while (line != null) {
                result += line
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            inputStream.close()
            httpUrlConnection.disconnect()
        }catch (e: Exception){
            Crashlytics.logException(e)
            Log.i("Connection Error: ", e.message)
        }
        //context.longToast(result)
        return result
    }

    interface OndoNetAct {
        fun doNetAct(result: String?)
    }
}