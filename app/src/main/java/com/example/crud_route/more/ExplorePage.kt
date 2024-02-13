package com.example.crud_route.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.crud_route.R
import com.example.crud_route.profile.Profile
import com.example.crud_route.route.CreateRoute

class ExplorePage : AppCompatActivity() {
    private lateinit var btnCreateRoute: Button
    private lateinit var btnProfile: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore_page)

        btnCreateRoute = findViewById(R.id.btnCreateRoute)
        btnProfile = findViewById(R.id.btnProfile)

        btnCreateRoute.setOnClickListener({
            startActivity(Intent(this, CreateRoute::class.java))
        })
        btnProfile.setOnClickListener({
            startActivity(Intent(this, Profile::class.java))
        })
    }

    fun onCardClick(view: View) {
        when (view.id) {
            R.id.cardInclination -> {
                startActivity(Intent(this, Inclination::class.java))
            }
            R.id.cardMechanics -> {
                //startActivity(Intent(this, Mechanics::class.java))
            }
        }
    }
}