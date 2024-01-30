package com.example.crud_route.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import com.example.crud_route.R
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var toggleShowPswrd: ToggleButton
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        toggleShowPswrd = findViewById(R.id.toggleBtn)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)
        firebaseAuth = FirebaseAuth.getInstance()

        toggleShowPswrd.setOnClickListener { view ->
            val isChecked = toggleShowPswrd.isChecked
            if (isChecked){
                password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        btnLogin.setOnClickListener({
            val email = email.text.toString()
            val pswrd = password.text.toString()

            if (email.isNotEmpty() && pswrd.isNotEmpty() ) {
                signIn(email, pswrd)
            } else {
                Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show()
            }
        })

        btnSignup.setOnClickListener({
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        })
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfull Log in", Toast.LENGTH_SHORT).show()
                    // Redirect to Home Page
                    finish()
                } else {
                    Toast.makeText(this, "Log in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}