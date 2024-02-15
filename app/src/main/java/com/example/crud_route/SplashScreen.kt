package com.example.crud_route

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.crud_route.register.Menu

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { false }

        setContentView(R.layout.activity_main)

        //Send us from splash screen to the menu
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }
}