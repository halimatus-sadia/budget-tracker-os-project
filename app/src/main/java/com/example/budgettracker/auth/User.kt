package com.example.budgettracker.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val username: String,
    val email: String,
    val password: String
)
