package com.example.sms

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB"
        // INCREMENTED VERSION to 2 because we changed the table structure
        private const val DATABASE_VERSION = 2

        // Table and Column Names
        private const val TABLE_STUDENTS = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ROLL = "roll_number"
        private const val COLUMN_COURSE = "course"
        private const val COLUMN_EMAIL = "email" // ADDED
        private const val COLUMN_PHONE = "phone" // ADDED

        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USER_NAME = "user_name"
        private const val COLUMN_USER_EMAIL = "user_email"
        private const val COLUMN_USER_PASS = "user_pass"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_STUDENTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_ROLL TEXT,"
                + "$COLUMN_COURSE TEXT,"
                + "$COLUMN_EMAIL TEXT," // ADDED
                + "$COLUMN_PHONE TEXT)") // ADDED
        db?.execSQL(createTable)

        val createUsers = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_USER_NAME TEXT,"
                + "$COLUMN_USER_EMAIL TEXT UNIQUE,"
                + "$COLUMN_USER_PASS TEXT)")
        db?.execSQL(createUsers)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // --- USER TABLE CRUD ---
    fun insertUser(name: String, email: String, pass: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_NAME, name)
            put(COLUMN_USER_EMAIL, email)
            put(COLUMN_USER_PASS, pass)
        }
        val res = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return res
    }

    fun getUserByEmail(email: String): Pair<String, String>? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_USER_NAME, $COLUMN_USER_PASS FROM $TABLE_USERS WHERE $COLUMN_USER_EMAIL = ?", arrayOf(email))
        var res: Pair<String, String>? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME))
            val pass = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASS))
            res = Pair(name, pass)
        }
        cursor.close()
        db.close()
        return res
    }

    // --- STUDENT CRUD FUNCTIONS ---

    fun insertStudent(student: Student): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_ROLL, student.rollNumber)
            put(COLUMN_COURSE, student.course)
            put(COLUMN_EMAIL, student.email) // ADDED
            put(COLUMN_PHONE, student.phone) // ADDED
        }
        val success = db.insert(TABLE_STUDENTS, null, contentValues)
        db.close()
        return success
    }

    fun getAllStudents(): List<Student> {
        val studentList = mutableListOf<Student>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENTS", null)

        if (cursor.moveToFirst()) {
            do {
                val student = Student(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    rollNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLL)),
                    course = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)), // FIXED
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))  // FIXED
                )
                studentList.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }

    fun updateStudent(student: Student): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_ROLL, student.rollNumber)
            put(COLUMN_COURSE, student.course)
            put(COLUMN_EMAIL, student.email) // ADDED
            put(COLUMN_PHONE, student.phone) // ADDED
        }
        val success = db.update(TABLE_STUDENTS, contentValues, "$COLUMN_ID=?", arrayOf(student.id.toString()))
        db.close()
        return success
    }

    fun deleteStudent(studentId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_STUDENTS, "$COLUMN_ID=?", arrayOf(studentId.toString()))
        db.close()
        return success
    }

    fun getStudentById(id: Int): Student? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            "$COLUMN_ID=?",
            arrayOf(id.toString()),
            null, null, null
        )

        var student: Student? = null
        if (cursor != null && cursor.moveToFirst()) {
            student = Student(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                rollNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLL)),
                course = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            )
            cursor.close()
        }
        db.close()
        return student
    }

    // STATISTICS HELPERS
    fun getTotalCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_STUDENTS", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }

    fun getCountByCourse(courseName: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_STUDENTS WHERE $COLUMN_COURSE = ?", arrayOf(courseName))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }
}