package com.example.crud_route

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class HomePage : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btnMore: Button
    private lateinit var btnCreateRoute: Button
    //private lateinit var btnProcess: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnMore = findViewById(R.id.btnMore)
        btnCreateRoute = findViewById(R.id.btnCreateRoute)

        btnMore.setOnClickListener({
            startActivity(Intent(this, ExplorePage::class.java))
        })

        btnCreateRoute.setOnClickListener({
            startActivity(Intent(this, NewRoute::class.java))
        })
    }

    override fun onMapReady(gmap: GoogleMap) {
        googleMap = gmap

        // Verify and ask for permition and location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the last location known
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        // Move camera to the actual location
                        val cameraPosition = CameraPosition.Builder()
                            .target(LatLng(it.latitude, it.longitude))
                            .zoom(15.0f)
                            .build()

                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
        } else {
            // Ask por location permitions if they are not setted
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }
}