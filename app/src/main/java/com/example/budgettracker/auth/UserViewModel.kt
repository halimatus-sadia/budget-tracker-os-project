package com.example.budgettracker.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.registerUser(user)
                onSuccess()
            } catch (e: Exception) {
                onFailure()
            }
        }
    }

    fun loginUser(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.loginUser(email, password)
            onResult(user)
        }
    }
}
