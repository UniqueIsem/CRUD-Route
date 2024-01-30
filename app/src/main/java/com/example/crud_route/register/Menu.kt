package com.example.crud_route.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.crud_route.R

class Menu : AppCompatActivity() {
    private lateinit var btnLogin:Button
    private lateinit var btnSignup:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)

        btnLogin.setOnClickListener({
            val intentLogin = Intent(this, Login::class.java)
            startActivity(intentLogin)
        })
        btnSignup.setOnClickListener({
            val intentLogin = Intent(this, Signup::class.java)
            startActivity(intentLogin)
        })
    }
}