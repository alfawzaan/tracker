package com.example.user.tracker.Shared

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.user.tracker.R
import com.example.user.tracker.R.id.progressBar
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ProgressBar






class SignInPart : Fragment() {

    lateinit var email: TextView
    lateinit var pass: TextView
    lateinit var loginBtn: Button
    lateinit var uType: CheckBox
    lateinit var dataPasser: DataPasser
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //Inflate the Fragment
        var view = inflater.inflate(R.layout.fragment_sign_in_part, container, false)

        //Initialize All Views
        email = view.findViewById<TextView>(R.id.userEmail)
        pass = view.findViewById<TextView>(R.id.userPassword)
        uType = view.findViewById(R.id.userType)
        loginBtn = view.findViewById<Button>(R.id.singInButton)
        progressBar = view.findViewById(R.id.progressBar)

        //val progressDrawable = progressBar.progressDrawable.mutate()
        //progressDrawable.setColorFilter( Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
        //progressBar.setProgressDrawable(progressDrawable)

       // progressBar.background = getProgressBarAnimation()

        //progressBar.
        /*progressBar.indeterminateDrawable.setColorFilter(-0x340000,
                android.graphics.PorterDuff.Mode.MULTIPLY)*/

        loginBtn.setOnClickListener {
            event -> run {

            println("Button Clicked")
                if (!email.text.toString().isEmpty() && !pass.text.toString().isEmpty()) {

                    dataPasser.progressbarVisiblity(progressBar)
                    var email = email.text.toString().trim()
                    var password = pass.text.toString().trim()
                    var checked = uType.isChecked
                    dataPasser.onLoginClicked(email, password, checked)
                    //dataPasser.progressbarVisiblity(progressBar)
                } else {
                    Toast.makeText(dataPasser as Context, "Both Field Should be Filled", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //Return the Fragment for display
        return view
    }

    //setter for any activity that will implement the interfaces
    fun setOnButtonPressedListener(activity: AppCompatActivity) {
        dataPasser = activity as DataPasser
    }

    interface DataPasser{
       fun onLoginClicked(email:String, pass:String, userType: Boolean)
        fun progressbarVisiblity( pBar: ProgressBar)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as DataPasser


    }

    override fun onDetach() {
        super.onDetach()
        //listener = null
    }

    /*fun getProgressBarAnimation(): AnimationDrawable {

        val rainbow1 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW))

        val rainbow2 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN))

        val rainbow3 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN))

        val rainbow4 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE))

        val rainbow5 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA))

        val rainbow6 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED))


        val gds = arrayListOf(rainbow1, rainbow2, rainbow3, rainbow4, rainbow5, rainbow6)

        val animation = AnimationDrawable()

        for (gd in gds) {
            animation.addFrame(gd, 100)
        }

        animation.isOneShot = false

        return animation
    }*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */


}
