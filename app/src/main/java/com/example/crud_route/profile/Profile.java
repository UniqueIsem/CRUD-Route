package com.example.crud_route.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crud_route.R;
import com.example.crud_route.more.ExplorePage;
import com.example.crud_route.route.CreateRoute;

public class Profile extends AppCompatActivity {
    ImageView userPhoto;
    TextView userName, userDescription, userMotorcycle;
    Button btnMore, btnCreateRoute, btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Profile
        userPhoto = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userDescription = findViewById(R.id.userDescription);
        userMotorcycle = findViewById(R.id.userMotorcycle);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnMore = findViewById(R.id.btnMore);
        btnCreateRoute = findViewById(R.id.btnCreateRoute);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), EditProfile.class));
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), ExplorePage.class));
            }
        });
        btnCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), CreateRoute.class));
            }
        });

    }
}
