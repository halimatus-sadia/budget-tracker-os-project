package com.example.budgettracker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.budgettracker.db.AppDatabase
import com.example.budgettracker.databinding.ActivityDetailedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction: Transaction
    private lateinit var binding: ActivityDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewBinding instance
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        // Set initial values using ViewBinding
        binding.labelInput.setText(transaction.label)
        binding.amountInput.setText(transaction.amount.toString())
        binding.descriptionInput.setText(transaction.description)

        binding.rootView.setOnClickListener {
            this.window.decorView.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.labelInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if (it!!.isNotEmpty()) {
                binding.labelLayout.error = null
            }
        }

        binding.amountInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if (it!!.isNotEmpty()) {
                binding.amountLayout.error = null
            }
        }

        binding.descriptionInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
        }

        binding.updateBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty()) {
                binding.labelLayout.error = "Please enter a valid label"
            } else if (amount == null) {
                binding.amountLayout.error = "Please enter a valid amount"
            } else {
                val updatedTransaction = Transaction(transaction.id, label, amount, description)
                update(updatedTransaction)
            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(transaction: Transaction) {
        val db = AppDatabase.getDatabase(this)
        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}
