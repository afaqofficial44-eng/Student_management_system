package com.example.sms

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatsActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var tvTotalCount: TextView
    private lateinit var tvCourseACount: TextView
    private lateinit var tvCourseBCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        db = DatabaseHelper(this)
        tvTotalCount = findViewById(R.id.tvTotalCount)
        tvCourseACount = findViewById(R.id.tvCourseACount)
        tvCourseBCount = findViewById(R.id.tvCourseBCount)

        loadStats()
    }

    private fun loadStats() {
        val total = db.getTotalCount()
        val csCount = db.getCountByCourse("Computer Science")
        val baCount = db.getCountByCourse("Business Administration")

        tvTotalCount.text = total.toString()
        tvCourseACount.text = csCount.toString()
        tvCourseBCount.text = baCount.toString()
    }
}
