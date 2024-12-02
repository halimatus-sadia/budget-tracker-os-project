package com.example.budgettracker.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgettracker.R
import com.example.budgettracker.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize ViewModel
        val dao = AppDatabase.getDatabase(applicationContext).userDao()
        val repository = UserRepository(dao)
        userViewModel = UserViewModel(repository)

        // Get references to the input fields
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etDob = findViewById<EditText>(R.id.etDob)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        // Get reference to the Register button
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Set click listener
        btnRegister.setOnClickListener {
            // Gather user input
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val dob = etDob.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a User object
            val user = User(
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dob,
                username = username,
                email = email,
                password = password
            )

            // Register the user using ViewModel
            CoroutineScope(Dispatchers.IO).launch {
                userViewModel.registerUser(user, {
                    runOnUiThread {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration Successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close the activity or navigate to the login screen
                    }
                }, {
                    runOnUiThread {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration Failed. Try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }
}