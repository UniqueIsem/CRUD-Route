package com.example.crud_route

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ExplorePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore_page)
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