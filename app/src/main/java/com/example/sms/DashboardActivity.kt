package com.example.sms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dbHelper = DatabaseHelper(this)
        val rvStudents = findViewById<RecyclerView>(R.id.rvStudents)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        // Setup RecyclerView
        rvStudents.layoutManager = LinearLayoutManager(this)

        // Handle Item Click: Navigate to Detail Activity
        adapter = StudentAdapter(dbHelper.getAllStudents()) { student ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("STUDENT_ID", student.id)
            startActivity(intent)
        }
        rvStudents.adapter = adapter
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }

        val fabStats = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabStats)
        val fabProfile = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabProfile)
        fabStats.setOnClickListener { startActivity(Intent(this, StatsActivity::class.java)) }
        fabProfile.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }

        // Optionally add a long-press or menu to open stats — menu handled by onOptionsItemSelected

        // Setup SearchView (filter students by name or roll number)
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        searchView?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = dbHelper.getAllStudents().filter {
                    it.name.contains(newText ?: "", ignoreCase = true) ||
                            it.rollNumber.contains(newText ?: "", ignoreCase = true)
                }
                adapter.updateData(filteredList)
                return true
            }
        })
    }
    // Refresh list when returning to this screen
    override fun onResume() {
        super.onResume()
        adapter.updateData(dbHelper.getAllStudents())
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_stats -> {
                startActivity(Intent(this, StatsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}