package com.example.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.R
import com.example.expensetracker.Utils
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: ExpenseDao) : ViewModel() {
    private val _expensesByType = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expensesByType: Flow<List<ExpenseEntity>> = _expensesByType

    val allExpenses = dao.getAllExpensesOrderedByDateDesc()

    fun getBalance(expenses: List<ExpenseEntity>): Double {
        val totalIncome = getTotalIncome(expenses)
        val totalExpense = getTotalExpense(expenses)
        return totalIncome - totalExpense
    }

    fun getAllExpensesByType(type: String){
        viewModelScope.launch {
            allExpenses.collect { allExpenses ->
                // Update _expensesByType based on all expenses
                // For example, filter expenses by type "Income"
                val incomeExpenses = allExpenses.filter { it.type == type }
                _expensesByType.value = incomeExpenses
            }
        }
    }

    fun getTotalExpense(expenses: List<ExpenseEntity>): Double {
        return expenses.filter { it.type == "Expense" }.sumByDouble { it.amount }
    }

    fun getTotalIncome(expenses: List<ExpenseEntity>): Double {
        return expenses.filter { it.type == "Income" }.sumByDouble { it.amount }
    }

    fun getItemIcon(it : ExpenseEntity) : Int {
            if(it.category == "Netflix"){
                return R.drawable.netflix
            } else if(it.category == "Paypal"){
                return R.drawable.paypal
            } else if(it.category == "Upwork"){
                return R.drawable.upwork
            } else{
                return R.drawable.transfer
            }
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}