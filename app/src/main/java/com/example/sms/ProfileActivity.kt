package com.example.sms

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Simple placeholder profile (app/user info)
        val tvName = findViewById<TextView>(R.id.tvProfileName)
        val tvEmail = findViewById<TextView>(R.id.tvProfileEmail)

        // Read logged-in user email from SharedPreferences
        val prefs = getSharedPreferences("sms_prefs", MODE_PRIVATE)
        val email = prefs.getString("logged_in_email", null)
        if (email != null) {
            val db = DatabaseHelper(this)
            val user = db.getUserByEmail(email)
            if (user != null) {
                tvName.text = user.first
                tvEmail.text = email
            } else {
                tvName.text = "User"
                tvEmail.text = email
            }
        } else {
            tvName.text = "Guest"
            tvEmail.text = "-"
        }
    }
}
