package com.example.sms

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast

class AddStudentActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        dbHelper = DatabaseHelper(this)

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etRoll = findViewById<TextInputEditText>(R.id.etRoll)
        val etCourse = findViewById<TextInputEditText>(R.id.etCourse)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPhone = findViewById<TextInputEditText>(R.id.etPhone)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val roll = etRoll.text.toString().trim()
            val course = etCourse.text.toString().trim()

            if (name.isEmpty() || roll.isEmpty() || course.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = etEmail.text.toString().trim().ifEmpty { null }
            val phone = etPhone.text.toString().trim().ifEmpty { null }

            val student = Student(name = name, rollNumber = roll, course = course, email = email, phone = phone)
            val id = dbHelper.insertStudent(student)
            if (id > 0) {
                Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
