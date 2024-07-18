package com.example.expensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ExpenseEntity represents an expense item stored in the database.
 *
 * @property id Unique identifier for the expense item. Generated automatically if null.
 * @property title Title or description of the expense.
 * @property amount Amount of the expense.
 * @property date Timestamp of when the expense occurred.
 * @property category Category or classification of the expense.
 * @property type Type of the expense (e.g., "Income" or "Expense").
 * @property bookmark Indicates if the expense is bookmarked or not (default: false).
 */
@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val amount: Double,
    val date: Long,
    val category: String,
    val type: String,
    val bookmark: Boolean = false
)
