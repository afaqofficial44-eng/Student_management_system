package com.example.sms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private var studentList: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvRoll: TextView = itemView.findViewById(R.id.tvRollNumber)
        val tvCourse: TextView = itemView.findViewById(R.id.tvCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvName.text = student.name
        holder.tvRoll.text = "Roll No: ${student.rollNumber}"
        holder.tvCourse.text = "Course: ${student.course}"

        holder.itemView.setOnClickListener { onItemClick(student) }
    }

    override fun getItemCount(): Int = studentList.size

    // Method to refresh data
    fun updateData(newList: List<Student>) {
        studentList = newList
        notifyDataSetChanged()
    }
}