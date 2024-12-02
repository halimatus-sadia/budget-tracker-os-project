package com.example.budgettracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.budgettracker.databinding.ActivityAddTransactionBinding
import com.example.budgettracker.db.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    // Declare a ViewBinding instance
    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle text changes for label input
        binding.labelInput.addTextChangedListener {
            if (it!!.isNotEmpty()) {
                binding.labelLayout.error = null
            }
        }

        // Handle text changes for amount input
        binding.amountInput.addTextChangedListener {
            if (it!!.isNotEmpty()) {
                binding.amountLayout.error = null
            }
        }

        // Handle the add transaction button click
        binding.addTransactionBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty()) {
                binding.labelLayout.error = "Please enter a valid label"
            } else if (amount == null) {
                binding.amountLayout.error = "Please enter a valid amount"
            } else {
                val transaction = Transaction(0, label, amount, description)
                insert(transaction)
            }
        }

        // Close the activity when close button is clicked
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun insert(transaction: Transaction) {
        val db = AppDatabase.getDatabase(this)
        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}
