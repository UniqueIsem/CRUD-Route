package com.example.crud_route;

import android.Manifest;
import com.google.android.gms.tasks.OnSuccessListener;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class CreateRoute extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private GoogleMap map;
    public static int LOCATION_REQUEST_CODE = 100;
    Handler handler;
    long refreshTime = 5000;
    Runnable runnable;
    private FusedLocationProviderClient fusedLocationClient;
    daoRoute dao;
    Adapter adapter;
    ArrayList<Route> list;
    Route r;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_route);

        dao = new daoRoute(this);
        adapter = new Adapter(this, list, dao);

        // Get current location for google maps API
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Refresh location to get it continuously
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, refreshTime);
                checkLocationPermission();
            }
        }, refreshTime);
        checkLocationPermission();

        TextView latA = findViewById(R.id.routeLatitudeA);
        TextView longA = findViewById(R.id.routeLongitudeA);
        TextView latB = findViewById(R.id.routeLatitudeB);
        TextView longB = findViewById(R.id.routeLongitudeB);
        EditText name = findViewById(R.id.createRouteName);

        // Get values from Spinner
        Spinner typeSpinner = findViewById(R.id.routeTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.routeTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        EditText description = findViewById(R.id.createRouteDescription);
        EditText rate = findViewById(R.id.createRouteName);
        Button btnAddRouteData = findViewById(R.id.btnAddRouteData);

        // Save data into Route DB-SQLite
        btnAddRouteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    r = new Route(Double.parseDouble(latA.getText().toString()),
                            Double.parseDouble(longA.getText().toString()),
                            Double.parseDouble(latB.getText().toString()),
                            Double.parseDouble(longB.getText().toString()),
                            name.getText().toString(),
                            getType(),
                            description.getText().toString(),
                            Double.parseDouble(rate.getText().toString()));
                    dao.insert(r);
                    adapter.notifyDataSetChanged();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplication(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng userLocation = new LatLng(lat, longitude);

                    map.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(position).toString();
        setType(text);
    }

    public void setType(String typeS) {
        this.type = typeS;
    }

    public String getType() {
        return type;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }
}
