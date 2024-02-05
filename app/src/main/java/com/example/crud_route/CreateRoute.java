package com.example.crud_route;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CreateRoute extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    daoRoute dao;
    Adapter adapter;
    ArrayList<Route> list;
    Route r;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(this).inflate(R.layout.create_route, null);
        setContentView(rootView);

        TextView latA = rootView.findViewById(R.id.routeLatitudeA);
        TextView longA = rootView.findViewById(R.id.routeLongitudeA);
        TextView latB = rootView.findViewById(R.id.routeLatitudeB);
        TextView longB = rootView.findViewById(R.id.routeLongitudeB);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        double latitude = latLng.latitude;
                        double longitude = latLng.longitude;
                    }
                });
            }
        });

        EditText name = rootView.findViewById(R.id.createRouteName);

        Spinner typeSpinner = rootView.findViewById(R.id.routeTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.routeTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        

        EditText description = rootView.findViewById(R.id.createRouteDescription);
        EditText rate = rootView.findViewById(R.id.createRouteName);
        Button btnAddRouteData = rootView.findViewById(R.id.btnAddRouteData);

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
                }catch (Exception e) {
                    Toast.makeText(getApplication(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(position).toString();
        setType(text);
    }

    public void setType(String typeS) {
        this.type = typeS;
    }
    public String getType(){
        return type;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
