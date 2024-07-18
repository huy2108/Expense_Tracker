package com.example.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing and processing data related to expenses.
 *
 * @param dao ExpenseDao instance for accessing expense data from the database.
 */
class HomeViewModel(private val dao: ExpenseDao) : ViewModel() {

    // MutableStateFlow to hold expenses filtered by type
    private val _expensesByType = MutableStateFlow<List<ExpenseEntity>>(emptyList())

    // Exposed Flow of expenses filtered by type
    val expensesByType: Flow<List<ExpenseEntity>> = _expensesByType

    // Flow of all expenses ordered by date descending
    val allExpenses = dao.getAllExpensesOrderedByDateDesc()

    // Flow of expenses where bookmark is true
    val expensesByBookmarkTrue: Flow<List<ExpenseEntity>> = dao.getExpensesByBookmarkTrue()

    /**
     * Calculate the balance based on the given list of expenses.
     *
     * @param expenses List of expenses to calculate balance from.
     * @return Double representing the balance (total income - total expense).
     */
    fun getBalance(expenses: List<ExpenseEntity>): Double {
        val totalIncome = getTotalIncome(expenses)
        val totalExpense = getTotalExpense(expenses)
        return totalIncome - totalExpense
    }

    /**
     * Calculate total expense amount from the list of expenses.
     *
     * @param expenses List of expenses to calculate total expense from.
     * @return Double representing the total expense amount.
     */
    fun getTotalExpense(expenses: List<ExpenseEntity>): Double {
        return expenses.filter { it.type == "Expense" }.sumByDouble { it.amount }
    }

    /**
     * Calculate total income amount from the list of expenses.
     *
     * @param expenses List of expenses to calculate total income from.
     * @return Double representing the total income amount.
     */
    fun getTotalIncome(expenses: List<ExpenseEntity>): Double {
        return expenses.filter { it.type == "Income" }.sumByDouble { it.amount }
    }

    /**
     * Get the icon resource ID based on the expense category.
     *
     * @param it ExpenseEntity object containing the expense category.
     * @return Int representing the resource ID of the icon.
     */
    fun getItemIcon(it: ExpenseEntity): Int {
        return when (it.category) {
            "Netflix" -> R.drawable.netflix
            "Paypal" -> R.drawable.paypal
            "Upwork" -> R.drawable.upwork
            else -> R.drawable.transfer
        }
    }

    /**
     * Fetch all expenses of a specific type asynchronously and update _expensesByType.
     *
     * @param type String representing the type of expenses to filter.
     */
    fun getAllExpensesByType(type: String) {
        viewModelScope.launch {
            allExpenses.collect { allExpenses ->
                // Update _expensesByType based on all expenses filtered by type
                val filteredExpenses = allExpenses.filter { it.type == type }
                _expensesByType.value = filteredExpenses
            }
        }
    }
}

/**
 * ViewModelFactory responsible for creating instances of HomeViewModel.
 *
 * @param context Application context to access resources and database.
 */
class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the specified ViewModel class.
     *
     * @param modelClass Class of the ViewModel to create.
     * @return T Newly created instance of ViewModel.
     * @throws IllegalArgumentException if the ViewModel class is unknown.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            // Create ExpenseDao instance using the application context
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
