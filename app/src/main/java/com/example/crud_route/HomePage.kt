package com.example.crud_route

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.codebyashish.googledirectionapi.AbstractRouting
import com.codebyashish.googledirectionapi.ErrorHandling
import com.codebyashish.googledirectionapi.RouteDrawing
import com.codebyashish.googledirectionapi.RouteInfoModel
import com.codebyashish.googledirectionapi.RouteListener
import com.example.crud_route.fragments.FragmentDetail
import com.example.crud_route.fragments.FragmentList
import com.example.crud_route.more.ExplorePage
import com.example.crud_route.profile.Profile
import com.example.crud_route.route.CreateRoute
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap

class HomePage : AppCompatActivity(), OnMapReadyCallback, RouteListener {
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var btnMore: Button
    private lateinit var btnCreateRoute: Button
    private lateinit var btnProfile: Button

    private lateinit var destinationLocation: LatLng
    private lateinit var userLocation:LatLng
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val listFragment = FragmentList()
        val detailFragment = FragmentDetail()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, listFragment)
            commit()
        }

        /*en los click listeners se utliza esto para que regrese al fragment anterior
        despues de presionar el btn anterior en vez de cerrar la app
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, detailFragment)
            addToBackStack(null)
            commit()
        }*/

        btnMore = findViewById(R.id.btnMore)
        btnCreateRoute = findViewById(R.id.btnCreateRoute)
        btnProfile = findViewById(R.id.btnProfile)

        btnMore.setOnClickListener({
            startActivity(Intent(this, ExplorePage::class.java))
        })
        btnCreateRoute.setOnClickListener({
            startActivity(Intent(this, CreateRoute::class.java))
        })
        btnProfile.setOnClickListener({
            startActivity(Intent(this, Profile::class.java))
        })

    }

    override fun onMapReady(gmap: GoogleMap) {
        googleMap = gmap

        // Verify and ask for permition and location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            // Configurate location callback
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        // Move camera to the new location
                        val cameraPosition = CameraPosition.Builder()
                            .target(LatLng(location.latitude, location.longitude))
                            .zoom(15.0f)
                            .build()

                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
            }
            //Request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            // Ask por location permitions if they are not setted
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onPause() { //Stop location updates when ncessesary
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun setItemRoute(latitudeB: Double, longitudeB: Double ) {
        dialog.setMessage("Route is generating, please wait")
        dialog.show()
        googleMap.clear()
        destinationLocation = getLatLng(latitudeB, longitudeB)
        googleMap.addMarker(MarkerOptions().position(destinationLocation))

        //Set values from latB and longB
        val latB = findViewById<TextView>(R.id.routeLatitudeB)
        val longB = findViewById<TextView>(R.id.routeLongitudeB)
        latB.setText(destinationLocation.latitude.toString())
        longB.setText(destinationLocation.longitude.toString())

        //Get LatLngBounds to include both userLocation and destinationLocation
        val builder = LatLngBounds.Builder()
        builder.include(userLocation)
        builder.include(destinationLocation)
        val bounds = builder.build()
        //Animate camera to show both locations and route
        val padding = 100
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cu)

        //Generate route between userLocation and destinationLocation
        getRoute(userLocation, destinationLocation)
    }

    fun getLatLng(latitudeB: Double, longitudeB: Double): LatLng {
        return LatLng(latitudeB, longitudeB)
    }

    private fun getRoute(userLocation: LatLng, destinationLocation: LatLng) {
        val routeDrawing = RouteDrawing.Builder()
            .context(this)
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this).alternativeRoutes(true)
            .waypoints(userLocation, destinationLocation)
            .build()
        routeDrawing.execute()
    }

    override fun onRouteFailure(e: ErrorHandling?) {
        Toast.makeText(this, "Route Failed by: " + e.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onRouteStart() {
        TODO("Not yet implemented")
    }

    override fun onRouteSuccess(list: ArrayList<RouteInfoModel>?, indexing: Int) {
        val polylineOptions = PolylineOptions()
        val polylines = java.util.ArrayList<Polyline>()
        for (i in list!!.indices) {
            if (i == indexing) {
                polylineOptions.color(resources.getColor(R.color.darkRed))
                polylineOptions.width(12f)
                polylineOptions.addAll(list!![indexing].points)
                polylineOptions.startCap(RoundCap())
                val polyline: Polyline = googleMap.addPolyline(polylineOptions)
                polylines.add(polyline)
            }
        }
        dialog.dismiss()
    }

    override fun onRouteCancelled() {
        TODO("Not yet implemented")
    }

}
