package com.example.crud_route;

import androidx.annotation.NonNull;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.codebyashish.googledirectionapi.AbstractRouting;
import com.codebyashish.googledirectionapi.ErrorHandling;
import com.codebyashish.googledirectionapi.RouteDrawing;
import com.codebyashish.googledirectionapi.RouteInfoModel;
import com.codebyashish.googledirectionapi.RouteListener;
import com.example.crud_route.route.CreateRoute;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class Map extends AppCompatActivity implements OnMapReadyCallback, RouteListener {
    Button btnShowRoute;
    public static int LOCATION_REQUEST_CODE = 100;
    private GoogleMap map;
    private Double latitude;
    private Double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng destinationLocation, userLocation;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        destinationLocation = new LatLng(latitude, longitude);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        dialog = new ProgressDialog(Map.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnShowRoute = findViewById(R.id.btnShowRoute);
        btnShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Route is generating, please wait");
                dialog.show();
                //Get LatLngBounds to include both userLocation and destinationLocation
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(userLocation);
                builder.include(destinationLocation);
                LatLngBounds bounds = builder.build();
                //Animate camera to show both locations and route
                int padding = 100;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                map.animateCamera(cu);

                //Generate route between userLocation and destinationLocation if data is not null
                if (userLocation != null && destinationLocation != null) {
                    getRoute(userLocation, destinationLocation);
                } else {
                    Toast.makeText(getApplication(), "Error: " + "User:" + userLocation + "\n" + "Dest: " + destinationLocation, Toast.LENGTH_LONG).show();
                }
            }
        });

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            requestPermissions();
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Obtains lat and long from our position
                    double userLat = location.getLatitude();
                    double userLong = location.getLongitude();
                    userLocation = new LatLng(userLat, userLong);
                    Toast.makeText(getApplication(), "Latitude : " + userLat + "\nLongitude : " + userLong, Toast.LENGTH_LONG).show();

                    // If we insert a custom marker, it'will refresh meanwhile we move
                    if (map != null) {
                        map.clear();
                        // Insert a custom marker here (optional)
                    }
                }
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        map.addMarker(new MarkerOptions().position(destinationLocation));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 15.0f));
    }

    private void getRoute(LatLng userLocation, LatLng destinationLocation) {
        RouteDrawing routeDrawing = new RouteDrawing.Builder()
                .context(Map.this)
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(Map.this).alternativeRoutes(true)
                .waypoints(userLocation, destinationLocation)
                .build();
        routeDrawing.execute();
    }


    @Override
    public void onRouteFailure(ErrorHandling e) {
        Toast.makeText(this, "Route Failed by: " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteStart() {

    }

    @Override
    public void onRouteSuccess(ArrayList<RouteInfoModel> list, int indexing) {
        PolylineOptions polylineOptions = new PolylineOptions();
        ArrayList<Polyline> polylines = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i++) {
            if (i == indexing) {
                polylineOptions.color(getResources().getColor(R.color.darkRed));
                polylineOptions.width(12);
                polylineOptions.addAll(list.get(indexing).getPoints());
                polylineOptions.startCap(new RoundCap());
                Polyline polyline = map.addPolyline(polylineOptions);
                polylines.add(polyline);
            }
        }
        dialog.dismiss();
    }

    @Override
    public void onRouteCancelled() {

    }
}