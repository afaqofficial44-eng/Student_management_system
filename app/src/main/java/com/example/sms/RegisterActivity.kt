package com.example.sms

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etName)
        val etEmail = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etPassword)
        val btnRegister = findViewById<android.widget.Button>(R.id.btnLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString()

            if (validateInput(name, email, pass)) {
                // Save credentials to SQLite only
                val dbHelper = DatabaseHelper(this)
                val id = dbHelper.insertUser(name, email, pass)
                if (id > 0) {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registration failed (email may already exist)", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                finish()
            }
        }
    }

    private fun validateInput(name: String, email: String, pass: String): Boolean {
        return when {
            name.isEmpty() -> {
                Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Valid email is required", Toast.LENGTH_SHORT).show()
                false
            }
            pass.length < 4 -> {
                Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}