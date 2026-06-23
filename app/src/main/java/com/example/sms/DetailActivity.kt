package com.example.sms

import android.content.Intent
import android.os.Bundle  // Fixed typo here
import android.widget.Button // Removed the extra line without 'import'
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    // ... the rest of your code is correct

    companion object {
        const val EXTRA_STUDENT_ID = "STUDENT_ID"
        const val REQ_EDIT = 1001
    }

    private lateinit var db: DatabaseHelper
    private var studentId: Int = 0

    private lateinit var tvName: TextView
    private lateinit var tvRoll: TextView
    private lateinit var tvCourse: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = DatabaseHelper(this)

        tvName = findViewById(R.id.tvName)
        tvRoll = findViewById(R.id.tvRoll)
        tvCourse = findViewById(R.id.tvCourse)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        // Using the constant defined in the companion object above
        studentId = intent.getIntExtra(EXTRA_STUDENT_ID, 0)
        if (studentId == 0) {
            Toast.makeText(this, "Invalid student ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadStudent()

        btnEdit.setOnClickListener {
            val i = Intent(this, EditStudentActivity::class.java)
            // FIXED: Removed EditStudentActivity reference and used local EXTRA_STUDENT_ID
            // pass full student details for prefill
            val s = db.getStudentById(studentId)
            if (s != null) {
                i.putExtra(EXTRA_STUDENT_ID, s.id)
                i.putExtra("STUDENT_NAME", s.name)
                i.putExtra("STUDENT_ROLL", s.rollNumber)
                i.putExtra("STUDENT_COURSE", s.course)
                i.putExtra("STUDENT_EMAIL", s.email)
                i.putExtra("STUDENT_PHONE", s.phone)
            }
            startActivityForResult(i, REQ_EDIT)
        }

        btnDelete.setOnClickListener { confirmDelete() }
    }

    private fun loadStudent() {
        try {
            val s = db.getStudentById(studentId)
            if (s == null) {
                Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            tvName.text = s.name
            tvRoll.text = "Roll: ${s.rollNumber}"
            tvCourse.text = "Course: ${s.course ?: "-"}"
            tvEmail.text = "Email: ${s.email ?: "-"}"
            tvPhone.text = "Phone: ${s.phone ?: "-"}"
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading student", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            finish()
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete this student?")
            .setPositiveButton("Delete") { _, _ -> doDelete() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun doDelete() {
        val rows = db.deleteStudent(studentId)
        if (rows > 0) {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_EDIT && resultCode == RESULT_OK) {
            loadStudent()
            setResult(RESULT_OK)
        }
    }
}