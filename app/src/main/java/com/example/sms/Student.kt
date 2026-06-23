package com.example.sms

data class Student(
    val id: Int = 0,
    val name: String = "",
    val rollNumber: String = "",
    val course: String? = null,
    val email: String? = null,
    val phone: String? = null
)