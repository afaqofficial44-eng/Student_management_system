package com.example.sms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // Add this


class MainActivity : AppCompatActivity() {
    // MainActivity.kt
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Redirect to Login after splash
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Close MainActivity so user can't go back to splash
    }
}