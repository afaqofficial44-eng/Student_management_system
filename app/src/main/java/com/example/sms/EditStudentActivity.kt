package com.example.sms

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast

class EditStudentActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var studentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        dbHelper = DatabaseHelper(this)

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etRoll = findViewById<TextInputEditText>(R.id.etRoll)
        val etCourse = findViewById<TextInputEditText>(R.id.etCourse)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPhone = findViewById<TextInputEditText>(R.id.etPhone)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        studentId = intent.getIntExtra("STUDENT_ID", -1)
        etName.setText(intent.getStringExtra("STUDENT_NAME"))
        etRoll.setText(intent.getStringExtra("STUDENT_ROLL"))
        etCourse.setText(intent.getStringExtra("STUDENT_COURSE"))
        etEmail.setText(intent.getStringExtra("STUDENT_EMAIL"))
        etPhone.setText(intent.getStringExtra("STUDENT_PHONE"))

        btnUpdate.setOnClickListener {
            val name = etName.text.toString().trim()
            val roll = etRoll.text.toString().trim()
            val course = etCourse.text.toString().trim()
            val email = etEmail.text.toString().trim().ifEmpty { null }
            val phone = etPhone.text.toString().trim().ifEmpty { null }

            if (name.isEmpty() || roll.isEmpty() || course.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(id = studentId, name = name, rollNumber = roll, course = course, email = email, phone = phone)
            val updated = dbHelper.updateStudent(student)
            if (updated > 0) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete student")
                .setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Delete") { _, _ ->
                    val deleted = dbHelper.deleteStudent(studentId)
                    if (deleted > 0) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
