package com.example.crud_route

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.example.crud_route.fragments.FragmentDetail
import com.example.crud_route.fragments.FragmentList
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class HomePage : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var btnMore: Button
    private lateinit var btnCreateRoute: Button
    //private lateinit var btnProfile: Button

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
            replace(R.id.flFragment, listFragment)
            ----> addToBackStack(null) <----
            commit()
        }
        */

        btnMore = findViewById(R.id.btnMore)
        btnCreateRoute = findViewById(R.id.btnCreateRoute)

        btnMore.setOnClickListener({
            startActivity(Intent(this, ExplorePage::class.java))
        })

        btnCreateRoute.setOnClickListener({
            startActivity(Intent(this, CreateRoute::class.java))
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

}
