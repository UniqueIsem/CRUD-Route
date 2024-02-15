package com.example.crud_route

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_route.more.ExplorePage
import com.example.crud_route.profile.Profile
import com.example.crud_route.route.CreateRoute

class HomePage : AppCompatActivity() {
    private lateinit var btnMore: Button
    private lateinit var btnCreateRoute: Button
    private lateinit var btnProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val listFragment = RouteList()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, listFragment)
            commit()
        }

        btnMore = findViewById(R.id.btnMore)
        btnCreateRoute = findViewById(R.id.btnCreateRoute)
        btnProfile = findViewById(R.id.btnProfile)

        btnMore.setOnClickListener({
            startActivity(Intent(this, ExplorePage::class.java))
        })
        btnCreateRoute.setOnClickListener({
            startActivity(Intent(this, CreateRoute::class.java))
        })
        btnProfile.setOnClickListener({
            startActivity(Intent(this, Profile::class.java))
        })

    }
}
