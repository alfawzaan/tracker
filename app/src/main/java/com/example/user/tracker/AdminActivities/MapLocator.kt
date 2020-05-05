package com.example.user.tracker.AdminActivities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.user.tracker.R
import com.example.user.tracker.ClientActivities.UserInfo
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MapLocator.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapLocator.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MapLocator : Fragment() {


    //public lateinit var mapView: MapView
    public lateinit var mMap: GoogleMap
    public var onMapLocatorListener: OnMapLocatorListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_map_locator, container, false)


        //mapView = view.findViewById<MapView>(R.id.map)
        //mapView.onCreate(savedInstanceState)
        //mapView.getMapAsync(this)


        // Inflate the layout for this fragment
        return view
    }

    fun onButtonPressed(uri: Uri) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMapLocatorListener) {
            onMapLocatorListener = context


        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

    }

    override fun onResume() {
        super.onResume()
        //mapView.onResume()

    }

    interface OnMapLocatorListener {
        // TODO: Update argument type and name
        fun mapLocatorImplementation(userInfo: UserInfo)
    }
    /*override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isBuildingsEnabled = true
        mMap.isTrafficEnabled = true
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        addMakerAndMove(sydney)

    }*/

    fun addMakerAndMove(latLng: LatLng){
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        supportMapFragment?.getMapAsync(object : OnMapReadyCallback{
            override fun onMapReady(googleMap: GoogleMap) {
                mMap = googleMap
                //mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                mMap.isBuildingsEnabled = true
                mMap.isTrafficEnabled = true
                mMap.uiSettings.isZoomControlsEnabled = true
                mMap.uiSettings.isIndoorLevelPickerEnabled = true
                mMap.uiSettings.setAllGesturesEnabled(true)
                // Add a marker in Sydney and move the camera
                //val sydney = LatLng(-34.0, 151.0)
                //addMakerAndMove(sydney)

                mMap.clear()
                println("position in MapLocator ${latLng.latitude}, ${latLng.longitude}")
                var title = MarkerOptions().position(latLng).title
                mMap.addMarker(MarkerOptions().position(latLng).title(title))
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
            }

        })

/*
        if(mMap != null) {
            mMap.clear()
            println("position in MapLocator ${latLng.latitude}, ${latLng.longitude}")
            var title = MarkerOptions().position(latLng).title
            mMap.addMarker(MarkerOptions().position(latLng).title(title))
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
        }
*/

    }
    /*interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }*/

}
