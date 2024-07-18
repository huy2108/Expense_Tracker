package com.example.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for adding, updating, and deleting expense records.
 *
 * @param dao ExpenseDao instance for accessing expense data from the database.
 */
class AddExpenseViewModel(val dao: ExpenseDao) : ViewModel() {

    /**
     * Adds an expense record to the database.
     *
     * @param expenseEntity ExpenseEntity object representing the expense to add.
     * @return Boolean indicating whether the expense was successfully added.
     */
    suspend fun addExpense(expenseEntity: ExpenseEntity): Boolean {
        return try {
            dao.insertExpense(expenseEntity)
            true
        } catch (ex: Throwable) {
            false
        }
    }

    /**
     * Retrieves an expense record by its ID from the database.
     *
     * @param id Int ID of the expense record to retrieve.
     * @return Flow<ExpenseEntity?> Flow emitting the expense record with the given ID, or null if not found.
     */
    fun getExpenseById(id: Int): Flow<ExpenseEntity?> {
        return dao.getExpenseById(id)
    }

    /**
     * Updates an existing expense record in the database.
     *
     * @param expense ExpenseEntity object representing the updated expense.
     */
    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.updateExpense(expense)
        }
    }

    /**
     * Deletes an expense record from the database.
     *
     * @param expense ExpenseEntity object representing the expense to delete.
     */
    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
        }
    }

    /**
     * Deletes all expense records from the database.
     */
    fun deleteAllExpenses() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }
}

/**
 * ViewModelFactory responsible for creating instances of AddExpenseViewModel.
 *
 * @param context Application context to access resources and database.
 */
class AddExpenseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the specified ViewModel class.
     *
     * @param modelClass Class of the ViewModel to create.
     * @return T Newly created instance of ViewModel.
     * @throws IllegalArgumentException if the ViewModel class is unknown.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            // Create ExpenseDao instance using the application context
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
