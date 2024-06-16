package com.example.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense_table WHERE id = :id")
    fun getExpenseById(id: Int): Flow<ExpenseEntity?>

    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    fun getAllExpensesOrderedByDateDesc(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense_table WHERE type = :expenseType ORDER BY date DESC")
    fun getExpensesByType(expenseType: String): Flow<List<ExpenseEntity>>

    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    @Query("DELETE FROM expense_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)
}