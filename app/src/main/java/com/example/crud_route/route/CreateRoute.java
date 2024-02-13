package com.example.crud_route.route;

import android.Manifest;

import com.codebyashish.googledirectionapi.AbstractRouting;
import com.codebyashish.googledirectionapi.ErrorHandling;
import com.codebyashish.googledirectionapi.RouteDrawing;
import com.codebyashish.googledirectionapi.RouteInfoModel;
import com.codebyashish.googledirectionapi.RouteListener;
import com.example.crud_route.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class CreateRoute extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback, RouteListener {
    private GoogleMap map;
    public static int LOCATION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng destinationLocation, userLocation;
    daoRoute dao;
    Adapter adapter;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    ArrayList<Route> list;
    ArrayList<String> fileUris = new ArrayList<>();
    private FileAdapter fileAdapter;
    private ProgressDialog dialog;
    Route r;
    String type;
    int rate = 0;

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
        dialog = new ProgressDialog(CreateRoute.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();

        TextView latA = findViewById(R.id.routeLatitudeA);
        TextView longA = findViewById(R.id.routeLongitudeA);
        TextView latB = findViewById(R.id.routeLatitudeB);
        TextView longB = findViewById(R.id.routeLongitudeB);
        EditText name = findViewById(R.id.createRouteName);
        //Get value from rate
        ImageButton rateStar1 = findViewById(R.id.rateStar1);
        ImageButton rateStar2 = findViewById(R.id.rateStar2);
        ImageButton rateStar3 = findViewById(R.id.rateStar3);
        ImageButton rateStar4 = findViewById(R.id.rateStar4);
        ImageButton rateStar5 = findViewById(R.id.rateStar5);
        View.OnClickListener starClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.rateStar1) {
                    rate = 1;
                } else if (v.getId() == R.id.rateStar2) {
                    rate = 2;
                } else if (v.getId() == R.id.rateStar3) {
                    rate = 3;
                } else if (v.getId() == R.id.rateStar4) {
                    rate = 4;
                } else if (v.getId() == R.id.rateStar5) {
                    rate = 5;
                }
                updateStarIcons();
            }

            private void updateStarIcons() {
                rateStar1.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                rateStar2.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                rateStar3.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                rateStar4.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                rateStar5.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                switch (rate) {
                    case 5:
                        rateStar5.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    case 4:
                        rateStar4.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    case 3:
                        rateStar3.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    case 2:
                        rateStar2.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    case 1:
                        rateStar1.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                }
                Toast.makeText(getApplication(), "rate: " + rate, Toast.LENGTH_SHORT).show();
            }
        };
        rateStar1.setOnClickListener(starClickListener);
        rateStar2.setOnClickListener(starClickListener);
        rateStar3.setOnClickListener(starClickListener);
        rateStar4.setOnClickListener(starClickListener);
        rateStar5.setOnClickListener(starClickListener);

        // Get values from Spinner
        Spinner typeSpinner = findViewById(R.id.routeTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.routeTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(this);

        EditText description = findViewById(R.id.createRouteDescription);
        // Select Files (Img/Video)
        Button btnSelectFile = findViewById(R.id.btnSelectFile);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_img);

        //Select videos or images
        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Open galery
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            }
        });
        fileAdapter = new FileAdapter(CreateRoute.this, fileUris);
        recyclerView.setAdapter(fileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

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
                            type,
                            description.getText().toString(),
                            rate,
                            fileUris);
                    dao.insert(r);
                    adapter.notifyDataSetChanged();
                    Log.d("ISEM", "Route created");
                    finish();
                    list = dao.viewAll();
                } catch (Exception e) {
                    Toast.makeText(getApplication(), "ERROR" + e, Toast.LENGTH_LONG).show();
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
                    //Set values from latA and longA
                    double userLat = location.getLatitude();
                    double userLong = location.getLongitude();
                    TextView latA = findViewById(R.id.routeLatitudeA);
                    TextView longA = findViewById(R.id.routeLongitudeA);
                    latA.setText(String.valueOf(userLat));
                    longA.setText(String.valueOf(userLong));

                    //Update TextView values of latA and longA
                    userLocation = new LatLng(userLat, userLong);
                    LatLng userLocation = new LatLng(userLat, userLong);

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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                dialog.setMessage("Route is generating, please wait");
                dialog.show();
                map.clear();
                destinationLocation = latLng;
                map.addMarker(new MarkerOptions().position(latLng));

                //Set values from latB and longB
                TextView latB = findViewById(R.id.routeLatitudeB);
                TextView longB = findViewById(R.id.routeLongitudeB);
                latB.setText(String.valueOf(destinationLocation.latitude));
                longB.setText(String.valueOf(destinationLocation.longitude));

                //Get LatLngBounds to include both userLocation and destinationLocation
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(userLocation);
                builder.include(destinationLocation);
                LatLngBounds bounds = builder.build();
                //Animate camera to show both locations and route
                int padding = 100;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                map.animateCamera(cu);

                //Generate route between userLocation and destinationLocation
                getRoute(userLocation, destinationLocation);
            }
        });

    }

    private void getRoute(LatLng userLocation, LatLng destinationLocation) {
        RouteDrawing routeDrawing = new RouteDrawing.Builder()
                .context(CreateRoute.this)
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this).alternativeRoutes(true)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        fileUris.add(uri.toString()); // Save URI on list
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    fileUris.add(uri.toString()); // Save URI on list
                } else {
                    Toast.makeText(this, "Upload files please", Toast.LENGTH_SHORT).show();
                }
                fileAdapter.notifyDataSetChanged(); // Notify adapter about change in data
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(position).toString();
        type = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
