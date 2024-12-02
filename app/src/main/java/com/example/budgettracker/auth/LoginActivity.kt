package com.example.budgettracker.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgettracker.MainActivity
import com.example.budgettracker.R
import com.example.budgettracker.db.AppDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dao = AppDatabase.getDatabase(applicationContext).userDao()
        val repository = UserRepository(dao)
        userViewModel = UserViewModel(repository)

        // Reference to tvRegister TextView
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        // Set click listener to navigate to RegisterActivity
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            userViewModel.loginUser(email, password) { user ->
                if (user != null) {
                    // redirect to dashboard activity'
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}