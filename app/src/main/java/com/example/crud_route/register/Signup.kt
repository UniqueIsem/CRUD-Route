package com.example.crud_route.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import com.example.crud_route.R
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var newEmail: EditText
    private lateinit var newPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var toggleShowPswrd: ToggleButton
    private lateinit var btnSignup: Button
    private lateinit var btnLogin: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        newEmail = findViewById(R.id.newEmail)
        newPassword = findViewById(R.id.createPassword)
        confirmPassword = findViewById(R.id.confirmPassword)
        toggleShowPswrd = findViewById(R.id.toggleBtn)
        btnSignup = findViewById(R.id.btnSignup)
        btnLogin = findViewById(R.id.btnLogin)
        firebaseAuth = FirebaseAuth.getInstance()

        toggleShowPswrd.setOnClickListener { view ->
            val isChecked = toggleShowPswrd.isChecked
            if (isChecked){
                newPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                confirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                newPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                confirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        btnSignup.setOnClickListener({
            val email = newEmail.text.toString()
            val password = newPassword.text.toString()
            val confirmPassword = confirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.equals(confirmPassword)) { //succesfull sign up
                    signUp(email, password)
                } else {
                    Toast.makeText(this, "The password doesn't match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show()
            }
        })

        btnLogin.setOnClickListener({
            startActivity(Intent(this, Login::class.java))
        })
    }

    private fun signUp(email: String, password: String) {
        if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successful Register", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Login::class.java))
                    } else {
                        Toast.makeText(this, "Register failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // invalid format email
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
        }
    }

}