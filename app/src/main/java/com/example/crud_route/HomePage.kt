package com.example.crud_route

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomePage : AppCompatActivity() {
    private lateinit var btnMore: Button
    private lateinit var btnCreateRoute: Button
    //private lateinit var btnProcess: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        btnMore = findViewById(R.id.btnMore)
        btnCreateRoute = findViewById(R.id.btnCreateRoute)

        btnMore.setOnClickListener({
            startActivity(Intent(this, ExplorePage::class.java))
        })

        btnCreateRoute.setOnClickListener({
            //create new routes and save it into SQLite
            //add routes into home page
        })
    }
}