package com.example.crud_route

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class NewRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_route)
    }

    /*fun map() {
        googleMap = gmap

        // Add a marker
        val markerOptions = MarkerOptions()
            .position(LatLng(37.7749, -122.4194))
            .title("San Francisco")
            .snippet("Ciudad hermosa")
        val marker = googleMap.addMarker(markerOptions)

        // Move camarea to a specific position
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(37.7749, -122.4194))
            .zoom(15.0f)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        // Ejemplo 3: Dibujar una Línea entre dos Puntos
        val polylineOptions = PolylineOptions()
            .add(LatLng(37.7749, -122.4194))
            .add(LatLng(37.7749, -122.3894))
            .width(5f)
            .color(Color.RED)

        val polyline = googleMap.addPolyline(polylineOptions)

        // Add an event click on the map
        googleMap.setOnMapClickListener { latLng ->
            // Realizar acciones cuando se hace clic en el mapa
            println("Latitud: ${latLng.latitude}, Longitud: ${latLng.longitude}")
        }
    }*/
}